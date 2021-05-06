/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.common.services.ConfigService;
import io.encodely.dao.MediaFile;
import io.encodely.utils.FileSystemUtils;
import io.encodely.utils.MediaFileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScanServiceImpl implements ScanService {

  private final ConfigService configService;

  @Override
  public ArrayDeque<MediaFile> findMediaFiles(final Path directory) {

    try (final Stream<Path> stream =
        Files.walk(directory)
            .filter(Files::isReadable)
            .filter(this::isGreaterThanMinFileSize)
            .filter(this::isNotExclusion)
            .filter(this::isNotSample)
            .filter(this::isNotDirectory)
            .filter(FileSystemUtils::isMediaFile)) {

      return stream.map(this::convert).collect(Collectors.toCollection(ArrayDeque::new));

    } catch (final IOException ioException) {
      log.warn("Issue scanning {} : {}", directory, ioException.getMessage());

      return new ArrayDeque<>();
    }
  }

  private boolean isNotDirectory(final Path path) {

    return !Files.isDirectory(path);
  }

  private boolean isNotSample(final Path path) {

    return !Pattern.compile(Pattern.quote("sample"), Pattern.CASE_INSENSITIVE)
        .matcher(path.toString())
        .find();
  }

  protected boolean isNotExclusion(final Path path) {

    final String[] exclusions = configService.scan().getExclude().split("\\s*,\\s*");

    return Arrays.stream(exclusions).noneMatch(exclusion -> path.toString().contains(exclusion));
  }

  private boolean isGreaterThanMinFileSize(final Path path) {

    if (configService.scan().getMinFileSize() == -1) {
      return true;
    }

    try {
      return Files.size(path) > configService.scan().getMinFileSize();
    } catch (final IOException e) {
      log.error("Unable to calculate file size {} {}", path, e.getMessage());
      return false;
    }
  }

  private MediaFile convert(final Path path) {

    return MediaFileUtils.convert(path.toFile());
  }
}
