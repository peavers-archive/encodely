/* Licensed under Apache-2.0 */
package io.encodely.utils;

import com.google.common.hash.Hashing;
import io.encodely.dao.MediaFile;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;

@UtilityClass
public class MediaFileUtils {

  /**
   * This is a lot of personal preference and you may want to completely change this method. At the
   * moment it does the following:
   *
   * <ul>
   *   <li>Replaces everything that isn't a-zA-Z0-9 with '.'
   *   <li>Adds the extension to the file
   *   <li>Makes the filename and extension all lower case
   * </ul>
   */
  public String outputFilename(final MediaFile mediaFile, final String extension) {

    return "%s.%s"
        .formatted(mediaFile.getFilename().replaceAll("[^a-zA-Z0-9]", "."), extension)
        .replaceAll("([.])\\1+", "$1")
        .toLowerCase(Locale.ROOT);
  }

  public MediaFile convert(final File file) {

    return MediaFile.builder()
        .id(getId(file))
        .filename(getName(file))
        .extension(getExtension(file))
        .path(getPath(file))
        .size(getSize(file))
        .build();
  }

  private long getSize(final File file) {

    long size;
    try {
      final Path path = Paths.get(file.getAbsolutePath());
      final BasicFileAttributes fileAttributes =
          Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
      size = fileAttributes.size();
    } catch (final Exception e) {
      size = -1L;
    }
    return size;
  }

  private String getName(final File file) {

    return FilenameUtils.removeExtension(file.getName());
  }

  private String getExtension(final File file) {

    return FilenameUtils.getExtension(file.getName());
  }

  private String getId(final File file) {

    try {
      //noinspection UnstableApiUsage
      return Hashing.murmur3_128()
          .hashString(file.getCanonicalPath(), StandardCharsets.UTF_8)
          .toString();
    } catch (final IOException e) {
      return null;
    }
  }

  private String getPath(final File file) {

    try {
      return file.getCanonicalPath().trim();
    } catch (final IOException e) {
      return null;
    }
  }
}
