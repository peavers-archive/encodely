/* Licensed under Apache-2.0 */
package io.encodely.utils;

import com.google.common.net.MediaType;
import io.encodely.exceptions.InvalidOutputException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@UtilityClass
public class FileSystemUtils {

  /**
   * Validate the output directory by either creating it if possible, or crashing the application if
   * it can't be created.
   */
  public void createOutput(final Path outputDirectory) {

    try {
      Files.createDirectories(outputDirectory);
    } catch (final IOException exception) {
      log.error("Startup error: {}", exception.getMessage(), exception);

      throw new InvalidOutputException("Cannot create output directory.", exception);
    }
  }

  public boolean isMediaFile(final Path path) {

    final String contentType = probeContentType(path);

    //noinspection UnstableApiUsage
    return StringUtils.isNotEmpty(contentType)
        && MediaType.parse(contentType).is(MediaType.ANY_VIDEO_TYPE);
  }

  public String probeContentType(final Path path) {

    try {
      return Files.probeContentType(path);
    } catch (final IOException e) {
      log.error("Unable to probe {} {}", path, e.getMessage());
      return null;
    }
  }
}
