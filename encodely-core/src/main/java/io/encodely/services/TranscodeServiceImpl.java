/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.common.services.ConfigService;
import io.encodely.dao.EncoderProfile;
import io.encodely.dao.MediaFile;
import io.encodely.dao.TranscodeResult;
import io.encodely.dockerapi.domain.CreateContainerRequest;
import io.encodely.dockerapi.domain.StartResult;
import io.encodely.dockerapi.services.DockerService;
import io.encodely.repositories.TranscodeResultRepository;
import io.encodely.utils.CommandUtils;
import io.encodely.utils.MediaFileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranscodeServiceImpl implements TranscodeService {

  private final ConfigService configService;

  private final DockerService dockerService;

  private final MediaFileService mediaFileService;

  private final TranscodeResultRepository transcodeResultRepository;

  @Override
  public MediaFile transcode(final MediaFile mediaFile, final EncoderProfile encoderProfile) {

    try {
      final CreateContainerRequest containerRequest =
          getContainerRequest(mediaFile, encoderProfile);

      final String containerId = dockerService.createContainer(containerRequest);
      final StartResult startResult = dockerService.start(containerId);

      mediaFile.setTranscodeResult(
          getTranscodeResult(encoderProfile, containerRequest, startResult));

    } catch (final IOException | InterruptedException e) {
      log.error("Container start error: {}", e.getMessage(), e);

      // Restore interrupted state...
      Thread.currentThread().interrupt();
    } catch (final Exception e) {
      log.error("Container start error: {}", e.getMessage(), e);
    }

    return postTranscode(mediaFile);
  }

  private MediaFile postTranscode(final MediaFile mediaFile) {

    mediaFile.setProcessed(true);

    log.info("Finished transcode for: {}", mediaFile.getPath());

    if (mediaFile.getTranscodeResult().getExitCode() == 0) {
      dockerService.removeByContainerId(mediaFile.getTranscodeResult().getContainerId());

      deleteIfRequired(mediaFile);
    }

    return mediaFileService.save(mediaFile);
  }

  private TranscodeResult getTranscodeResult(
      final EncoderProfile encoderProfile,
      final CreateContainerRequest containerRequest,
      final StartResult startResult) {

    return transcodeResultRepository.save(
        TranscodeResult.builder()
            .exitCode(startResult.getExitCode())
            .stderr(startResult.getStderr())
            .stdout(startResult.getStdout())
            .containerId(startResult.getId())
            .ffmpegCommand(containerRequest.getCommandForPrint())
            .outputPath(containerRequest.getOutputDir())
            .encoderProfile(encoderProfile.getName())
            .build());
  }

  private CreateContainerRequest getContainerRequest(
      final MediaFile mediaFile, final EncoderProfile encoderProfile) {

    return CreateContainerRequest.builder()
        .name(getContainerName(mediaFile))
        .image(encoderProfile.getDockerImage())
        .inputDir(getInputDirectory(mediaFile))
        .outputDir(getOutputDirectory())
        .command(
            getTranscodeCommand(
                mediaFile, encoderProfile, getOutputFilename(mediaFile, encoderProfile)))
        .runtime(encoderProfile.getDockerRuntime())
        .build();
  }

  private List<String> getTranscodeCommand(
      final MediaFile mediaFile, final EncoderProfile encoderProfile, final String outputFilename) {

    return CommandUtils.ffmpeg(
        mediaFile.getPath(),
        Path.of(getOutputDirectory(), outputFilename).toString(),
        encoderProfile.getCommand(),
        configService.process().isOverwrite());
  }

  private String getOutputFilename(final MediaFile mediaFile, final EncoderProfile encoderProfile) {

    return MediaFileUtils.outputFilename(mediaFile, encoderProfile.getExtension());
  }

  private String getOutputDirectory() {

    return configService.scan().getOutput();
  }

  private String getInputDirectory(final MediaFile mediaFile) {

    return new File(mediaFile.getPath()).getParent();
  }

  private String getContainerName(final MediaFile mediaFile) {

    return configService.docker().getContainerPrefix() + mediaFile.getId();
  }

  private void deleteIfRequired(final MediaFile mediaFile) {

    if (configService.process().isDeleteSource()) {
      log.info("Deleting original: {}", mediaFile.getPath());

      FileUtils.deleteQuietly(new File(mediaFile.getPath()));
    }
  }
}
