/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.output;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import java.nio.charset.StandardCharsets;
import lombok.Getter;

/** Holds a frame of container output (usually one line, possibly more) */
public class OutputFrame {

  public static final OutputFrame END = new OutputFrame(OutputType.END, null);

  @Getter private final OutputType type;

  @Getter private final byte[] bytes;

  public OutputFrame(final OutputType type, final byte[] bytes) {

    this.type = type;
    this.bytes = bytes;
  }

  public static OutputFrame forFrame(final Frame frame) {

    final OutputType outputType = OutputType.forStreamType(frame.getStreamType());
    if (outputType == null) {
      return null;
    }
    return new OutputFrame(outputType, frame.getPayload());
  }

  public String getUtf8String() {

    if (bytes == null) {
      return "";
    }

    return new String(bytes, StandardCharsets.UTF_8);
  }

  public enum OutputType {
    STDOUT,
    STDERR,
    END;

    public static OutputType forStreamType(final StreamType streamType) {

      return switch (streamType) {
        case RAW, STDOUT -> STDOUT;
        case STDERR -> STDERR;
        default -> null;
      };
    }
  }
}
