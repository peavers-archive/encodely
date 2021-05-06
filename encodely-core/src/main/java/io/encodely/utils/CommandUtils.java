/* Licensed under Apache-2.0 */
package io.encodely.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandUtils {

  public List<String> ffmpeg(
      final String input,
      final String output,
      final String rawCommand,
      final boolean addOverwrite) {

    final List<String> ffmpegCommand = Lists.newArrayList(Splitter.on(" ").split(rawCommand));

    removeFfmpeg(ffmpegCommand);

    setInputAndOutput(input, output, ffmpegCommand);

    if (addOverwrite) {
      ffmpegCommand.add("-y");
    }

    return ffmpegCommand;
  }

  /** Replace templated strings with real values for the input and output. */
  private void setInputAndOutput(
      final String input, final String output, final List<String> ffmpegCommand) {
    ffmpegCommand.set(ffmpegCommand.indexOf("$input"), input);
    ffmpegCommand.set(ffmpegCommand.indexOf("$output"), output);
  }

  /**
   * The containers we use already include 'ffmpeg' in the runtime command. Remove it if a user
   * enters it.
   */
  private void removeFfmpeg(final List<String> ffmpegCommand) {

    final int ffmpegIndex = ffmpegCommand.indexOf("ffmpeg");

    if (ffmpegIndex != -1) {
      ffmpegCommand.remove(ffmpegIndex);
    }
  }
}
