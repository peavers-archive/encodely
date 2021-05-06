/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.dao.MediaFile;
import java.nio.file.Path;
import java.util.ArrayDeque;

public interface ScanService {

  ArrayDeque<MediaFile> findMediaFiles(Path directory);
}
