/* Licensed under Apache-2.0 */
package io.encodely.jobs;

import io.encodely.dao.MediaFile;
import io.encodely.domain.ScanOutput;
import io.encodely.jobmanager.domain.Job;
import io.encodely.services.MediaFileService;
import io.encodely.services.MessageService;
import io.encodely.services.ScanService;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.function.UnaryOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Component;

/** Access this class through the {@link JobFactory}. */
@Slf4j
@Component
@RequiredArgsConstructor
class FunctionScan implements UnaryOperator<Job> {

  private final JobHelper jobHelper;

  private final ScanService scanService;

  private final MediaFileService mediaFileService;

  private final MessageService messageService;

  @Override
  public Job apply(final Job inputJob) {

    final StopWatch stopWatch = StopWatch.createStarted();

    final Path path = Path.of((String) inputJob.getInput());
    final Job startedJob = jobHelper.setStartState(inputJob);
    final ArrayDeque<MediaFile> filesOnDisk = scanService.findMediaFiles(path);
    final List<MediaFile> filesInDatabase = mediaFileService.findAll();

    filesOnDisk.stream()
        .filter(diskFile -> filesInDatabase.stream().noneMatch(mf -> mf.equals(diskFile)))
        .forEach(filesInDatabase::add);

    filesInDatabase.retainAll(filesOnDisk);
    saveAndNotify(filesInDatabase);
    stopWatch.stop();

    return jobHelper.setEndState(
        startedJob,
        ScanOutput.builder()
            .duration(stopWatch.formatTime())
            .found(filesInDatabase.size())
            .path(path.toString())
            .build(),
        false);
  }

  private void saveAndNotify(final List<MediaFile> filesInDatabase) {

    mediaFileService.deleteAll();
    mediaFileService.saveAll(filesInDatabase);

    messageService.sendMessage("/topic/mediaFiles", filesInDatabase);
  }
}
