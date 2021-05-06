/* Licensed under Apache-2.0 */
package io.encodely.jobs;

import io.encodely.common.services.ConfigService;
import io.encodely.dao.EncoderProfile;
import io.encodely.dao.MediaFile;
import io.encodely.jobmanager.domain.Job;
import io.encodely.jobmanager.domain.JobStatus;
import io.encodely.jobmanager.services.JobService;
import io.encodely.services.MediaFileService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobFactory {

  private final ConfigService configService;

  private final MediaFileService mediaFileService;

  private final JobService jobService;

  private final FunctionScan functionScan;

  private final FunctionTranscode functionTranscode;

  /** Create a job to scan the target directory for all media files. */
  public List<Job> scanJob() {

    final Job job =
        Job.builder()
            .worker(functionScan)
            .name("scan-job")
            .input(configService.scan().getInput())
            .status(JobStatus.READY)
            .build();

    final Job start = jobService.start(job);

    return Collections.singletonList(start);
  }

  public List<Job> transcodeJob(final MediaFile mediaFile, final EncoderProfile encoderProfile) {
    final Job job =
        Job.builder()
            .worker(functionTranscode)
            .name("transcode-job")
            .input(Map.of(mediaFile, encoderProfile))
            .status(JobStatus.READY)
            .build();

    final Job start = jobService.start(job);

    return Collections.singletonList(start);
  }

  /**
   * For each media file that is found during the scan operation, create a job to transcode them.
   */
  public List<Job> transcodeAllJob(final EncoderProfile encoderProfile) {

    return mediaFileService.findAllByProcessedIsFalse().stream()
        .map(
            mediaFile ->
                jobService.start(
                    Job.builder()
                        .worker(functionTranscode)
                        .name("transcode-job")
                        .input(Map.of(mediaFile, encoderProfile))
                        .status(JobStatus.READY)
                        .build()))
        .collect(Collectors.toList());
  }
}
