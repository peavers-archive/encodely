/* Licensed under Apache-2.0 */
package io.encodely.jobs;

import io.encodely.common.services.ConfigService;
import io.encodely.dao.EncoderProfile;
import io.encodely.dao.MediaFile;
import io.encodely.jobmanager.domain.Job;
import io.encodely.services.TranscodeService;
import io.encodely.utils.FileSystemUtils;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** Access this class through the {@link JobFactory}. */
@Slf4j
@Component
@RequiredArgsConstructor
class FunctionTranscode implements UnaryOperator<Job> {

  private final JobHelper jobHelper;

  private final TranscodeService transcodeService;

  private final ConfigService configService;

  @Override
  public Job apply(final Job inputJob) {

    // Make sure we have somewhere to put the output of this job.
    final Path output = Path.of(configService.scan().getOutput());
    FileSystemUtils.createOutput(output);

    final Job persistedJob = jobHelper.setStartState(inputJob);

    final Properties properties = getProperties(inputJob);

    final MediaFile mediaFile =
        transcodeService.transcode(properties.getMediaFile(), properties.getEncoderProfile());

    return mediaFile.getTranscodeResult().getExitCode() == 0
        ? jobHelper.setEndState(persistedJob, mediaFile.getTranscodeResult(), false)
        : jobHelper.setEndState(persistedJob, mediaFile.getTranscodeResult(), true);
  }

  @SuppressWarnings("unchecked")
  private Properties getProperties(final Job inputJob) {
    final Map<MediaFile, EncoderProfile> input =
        (Map<MediaFile, EncoderProfile>) inputJob.getInput();

    final Map.Entry<MediaFile, EncoderProfile> entry = input.entrySet().iterator().next();

    return Properties.builder().mediaFile(entry.getKey()).encoderProfile(entry.getValue()).build();
  }

  @Data
  @Builder
  private static class Properties {

    private MediaFile mediaFile;

    private EncoderProfile encoderProfile;
  }
}
