/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.dao.EncoderProfile;
import io.encodely.dao.MediaFile;
import io.encodely.jobs.JobFactory;
import io.encodely.utils.FileSystemUtils;
import io.encodely.utils.MediaFileUtils;
import java.io.IOException;
import java.nio.file.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectoryWatchServiceImpl implements DirectoryWatchService {

  private final EncoderProfileService encoderProfileService;

  private final JobFactory jobFactory;

  @Override
  public void watch(final Path directory) throws IOException, InterruptedException {
    final WatchService watchService = FileSystems.getDefault().newWatchService();
    directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

    WatchKey key;
    while ((key = watchService.take()) != null) {

      key.pollEvents().stream()
          .filter(event -> event.kind().name().equals("ENTRY_CREATE"))
          .map(
              event ->
                  MediaFileUtils.convert(
                      Path.of(directory.toString(), event.context().toString()).toFile()))
          .forEach(this::transcode);
      key.reset();
    }
  }

  @SneakyThrows
  private void transcode(final MediaFile mediaFile) {
    if (FileSystemUtils.isMediaFile(Path.of(mediaFile.getPath()))) {
      final EncoderProfile encoderProfile = encoderProfileService.findActive();
      jobFactory.transcodeJob(mediaFile, encoderProfile);
    }
  }
}
