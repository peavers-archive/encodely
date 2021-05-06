/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.dao.MediaFile;
import java.util.List;

public interface MediaFileService {

  List<MediaFile> findAll();

  List<MediaFile> findAllByProcessedIsFalse();

  List<MediaFile> saveAll(Iterable<MediaFile> mediaFiles);

  MediaFile save(MediaFile mediaFile);

  void deleteAll();
}
