/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.output;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrameConsumerResultCallback
    extends ResultCallbackTemplate<FrameConsumerResultCallback, Frame> {

  private static final byte[] EMPTY_LINE = new byte[0];

  private static final Pattern ANSI_COLOR_PATTERN = Pattern.compile("\u001B\\[[0-9;]+m");

  private static final String LINE_BREAK_REGEX = "((\\r?\\n)|(\\r))";

  private final Map<OutputFrame.OutputType, Consumer<OutputFrame>> consumers;

  private final CountDownLatch completionLatch = new CountDownLatch(1);

  private final StringBuilder logString = new StringBuilder();

  private OutputFrame brokenFrame;

  public FrameConsumerResultCallback() {

    consumers = new HashMap<>();
  }

  /**
   * Set this callback to use the specified consumer for the given output type. The same consumer
   * can be configured for more than one output type.
   */
  public void addConsumer(
      final OutputFrame.OutputType outputType, final Consumer<OutputFrame> consumer) {

    consumers.put(outputType, consumer);
  }

  @Override
  public void onNext(final Frame frame) {

    if (frame != null) {
      final OutputFrame outputFrame = OutputFrame.forFrame(frame);
      if (outputFrame != null) {
        final Consumer<OutputFrame> consumer = consumers.get(outputFrame.getType());
        if (consumer == null) {
          log.error(
              "got frame with type {}, for which no handler is configured", frame.getStreamType());
        } else if (outputFrame.getBytes() != null && outputFrame.getBytes().length > 0) {
          if (frame.getStreamType() == StreamType.RAW) {
            processRawFrame(outputFrame, consumer);
          } else {
            processOtherFrame(outputFrame, consumer);
          }
        }
      }
    }
  }

  @Override
  public void onError(final Throwable throwable) {
    // Sink any errors
    try {
      close();
    } catch (final IOException ignored) {
    }
  }

  @Override
  public void close() throws IOException {

    OutputFrame lastLine = null;

    if (logString.length() > 0) {
      lastLine = new OutputFrame(OutputFrame.OutputType.STDOUT, logString.toString().getBytes());
    }

    // send an END frame to every consumer... but only once per consumer.
    for (final Consumer<OutputFrame> consumer : new HashSet<>(consumers.values())) {
      if (lastLine != null) {
        consumer.accept(lastLine);
      }
      consumer.accept(OutputFrame.END);
    }
    super.close();

    completionLatch.countDown();
  }

  /**
   * @return a {@link CountDownLatch} that may be used to wait until {@link #close()} has been
   *     called.
   */
  public CountDownLatch getCompletionLatch() {

    return completionLatch;
  }

  private synchronized void processRawFrame(
      final OutputFrame outputFrame, final Consumer<OutputFrame> consumer) {

    String utf8String = outputFrame.getUtf8String();
    byte[] bytes = outputFrame.getBytes();

    // Merging the strings by bytes to solve the problem breaking non-latin unicode symbols.
    if (brokenFrame != null) {
      bytes = merge(brokenFrame.getBytes(), bytes);
      utf8String = new String(bytes);
      brokenFrame = null;
    }
    // Logger chunks can break the string in middle of multibyte unicode character.
    // Backup the bytes to reconstruct proper char sequence with bytes from next frame.
    final int lastCharacterType = Character.getType(utf8String.charAt(utf8String.length() - 1));
    if (lastCharacterType == Character.OTHER_SYMBOL) {
      brokenFrame = new OutputFrame(outputFrame.getType(), bytes);
      return;
    }

    utf8String = processAnsiColorCodes(utf8String, consumer);
    normalizeLogLines(utf8String, consumer);
  }

  private synchronized void processOtherFrame(
      final OutputFrame outputFrame, final Consumer<OutputFrame> consumer) {

    String utf8String = outputFrame.getUtf8String();

    utf8String = processAnsiColorCodes(utf8String, consumer);
    consumer.accept(new OutputFrame(outputFrame.getType(), utf8String.getBytes()));
  }

  private void normalizeLogLines(final String utf8String, final Consumer<OutputFrame> consumer) {
    // Reformat strings to normalize new lines.
    final List<String> lines = new ArrayList<>(Arrays.asList(utf8String.split(LINE_BREAK_REGEX)));
    if (lines.isEmpty()) {
      consumer.accept(new OutputFrame(OutputFrame.OutputType.STDOUT, EMPTY_LINE));
      return;
    }
    if (utf8String.startsWith("\n") || utf8String.startsWith("\r")) {
      lines.add(0, "");
    }
    if (utf8String.endsWith("\n") || utf8String.endsWith("\r")) {
      lines.add("");
    }
    for (int i = 0; i < lines.size() - 1; i++) {
      String line = lines.get(i);
      if (i == 0 && logString.length() > 0) {
        line = logString.toString() + line;
        logString.setLength(0);
      }
      consumer.accept(new OutputFrame(OutputFrame.OutputType.STDOUT, line.getBytes()));
    }
    logString.append(lines.get(lines.size() - 1));
  }

  private String processAnsiColorCodes(
      final String utf8String, final Consumer<OutputFrame> consumer) {

    if (!(consumer instanceof BaseConsumer) || ((BaseConsumer) consumer).isRemoveColorCodes()) {
      return ANSI_COLOR_PATTERN.matcher(utf8String).replaceAll("");
    }
    return utf8String;
  }

  private byte[] merge(final byte[] str1, final byte[] str2) {

    final byte[] mergedString = new byte[str1.length + str2.length];
    System.arraycopy(str1, 0, mergedString, 0, str1.length);
    System.arraycopy(str2, 0, mergedString, str1.length, str2.length);
    return mergedString;
  }
}
