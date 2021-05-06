/* Licensed under Apache-2.0 */
package io.encodely.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.encodely.dao.MediaFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MediaFileUtilsTest {

  static final String EXTENSION = "mp4";

  @Test
  void outputWithExtension() {

    final MediaFile mediaFile = MediaFile.builder().filename("abc").build();

    final String output = MediaFileUtils.outputFilename(mediaFile, "mp4");

    assertEquals("abc.mp4", output);
  }

  @Test
  void replaceNonStandardChars() {

    final MediaFile mediaFile =
        MediaFile.builder()
            .filename("Ef8mP$NfH1paHuN*UaANlkZ941g27AyY55b6aDQ1wDRjn9szq2sZFfzunBCIYhVz!")
            .build();

    final String actual = MediaFileUtils.outputFilename(mediaFile, "mp4");

    assertEquals("ef8mp.nfh1pahun.uaanlkz941g27ayy55b6adq1wdrjn9szq2szffzunbciyhvz.mp4", actual);
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "..a....bc..:.a.bc.mp4",
        ".abc.:.abc.mp4",
        "abc:abc.mp4",
        "abcН, О, П, �, С, Т, У, Ф, Х, Ц, Ч,:abc.mp4"
      },
      delimiter = ':')
  void nameRemovesDots(final String name, final String expected) {
    final MediaFile mediaFile = MediaFile.builder().filename(name).build();

    final String actual = MediaFileUtils.outputFilename(mediaFile, EXTENSION);

    assertEquals(expected, actual);
  }
}
