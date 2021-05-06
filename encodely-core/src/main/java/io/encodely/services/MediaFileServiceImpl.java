/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.dao.MediaFile;
import io.encodely.repositories.MediaFileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl implements MediaFileService {

  private final MediaFileRepository mediaFileRepository;

  @Override
  public List<MediaFile> findAll() {

    return mediaFileRepository.findAll();
  }

  @Override
  public List<MediaFile> findAllByProcessedIsFalse() {

    return mediaFileRepository.findAllByProcessedIsFalse();
  }

  @Override
  public List<MediaFile> saveAll(final Iterable<MediaFile> mediaFiles) {

    return mediaFileRepository.saveAll(mediaFiles);
  }

  @Override
  public MediaFile save(final MediaFile mediaFile) {
    return mediaFileRepository.save(mediaFile);
  }

  @Override
  public void deleteAll() {
    mediaFileRepository.deleteAll();
  }
}
