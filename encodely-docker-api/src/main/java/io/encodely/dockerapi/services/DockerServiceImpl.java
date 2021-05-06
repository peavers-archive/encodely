/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import io.encodely.common.services.ConfigService;
import io.encodely.dockerapi.domain.CreateContainerRequest;
import io.encodely.dockerapi.domain.StartResult;
import io.encodely.dockerapi.output.FrameConsumerResultCallback;
import io.encodely.dockerapi.output.LoggedPullImageResultCallback;
import io.encodely.dockerapi.output.OutputFrame;
import io.encodely.dockerapi.output.ToStringConsumer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockerServiceImpl implements DockerService {

  private final ConfigService configService;

  private final DockerClient dockerClient;

  @Override
  public StartResult start(final String containerId) throws IOException, InterruptedException {

    log.info("Starting container: {}", containerId);

    dockerClient.startContainerCmd(containerId).exec();

    final ToStringConsumer stdoutConsumer = new ToStringConsumer();
    final ToStringConsumer stderrConsumer = new ToStringConsumer();

    try (final FrameConsumerResultCallback callback = new FrameConsumerResultCallback()) {
      callback.addConsumer(OutputFrame.OutputType.STDOUT, stdoutConsumer);
      callback.addConsumer(OutputFrame.OutputType.STDERR, stderrConsumer);

      dockerClient
          .attachContainerCmd(containerId)
          .withStdErr(Boolean.TRUE)
          .withStdOut(Boolean.TRUE)
          .withFollowStream(Boolean.TRUE)
          .exec(callback)
          .awaitCompletion();
    }

    final Long exitCode = getExitCode(containerId);

    return new StartResult(
        containerId,
        exitCode,
        stdoutConsumer.toString(StandardCharsets.UTF_8),
        stderrConsumer.toString(StandardCharsets.UTF_8));
  }

  @Override
  public void stopAll() {

    log.info("Stopping all containers...");

    dockerClient.listContainersCmd().exec().stream()
        .filter(
            container ->
                container.getNames()[0].contains(configService.docker().getContainerPrefix()))
        .forEach(container -> dockerClient.stopContainerCmd(container.getId()).exec());
  }

  @Override
  public void removeByContainerId(final String containerId) {

    log.info("Removing container: {}", containerId);

    dockerClient.removeContainerCmd(containerId).exec();
  }

  @Override
  public void pullImage(final String image) {

    log.info("Pulling image: {}", image);

    try {
      dockerClient.pullImageCmd(image).exec(new LoggedPullImageResultCallback()).awaitCompletion();
    } catch (final InterruptedException interruptedException) {
      log.error("Unable pull image: {}", interruptedException.getMessage(), interruptedException);

      // Restore interrupted state...
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public long getExitCode(final String containerId) {

    final InspectContainerResponse containerResponse =
        dockerClient.inspectContainerCmd(containerId).exec();
    final InspectContainerResponse.ContainerState state = containerResponse.getState();

    if (StringUtils.isNoneEmpty(state.getError())) {
      log.info(state.getError());
    }

    if (Objects.nonNull(state.getExitCodeLong())) {
      return state.getExitCodeLong();
    } else {
      return -1;
    }
  }

  @Override
  public String createContainer(final CreateContainerRequest request) {

    log.info("Create container request: {}", request.getName());

    // Make sure we have the image to start
    checkAndPullImage(request.getImage());

    // Make sure a container with the same name doesn't exist
    checkAndRemoveContainer(request.getName());

    try (final CreateContainerCmd containerCmd =
        dockerClient
            .createContainerCmd(request.getImage())
            .withName(request.getName())
            .withWorkingDir(request.getInputDir())
            .withCmd(request.getCommand())
            .withAttachStderr(Boolean.TRUE)
            .withAttachStdin(Boolean.TRUE)
            .withAttachStdout(Boolean.TRUE)
            .withHostConfig(
                HostConfig.newHostConfig()
                    .withAutoRemove(Boolean.FALSE)
                    .withBinds(
                        new Bind(request.getInputDir(), new Volume(request.getInputDir())),
                        new Bind(request.getOutputDir(), new Volume(request.getOutputDir()))))) {

      if (StringUtils.isNoneEmpty(request.getRuntime())) {
        Objects.requireNonNull(containerCmd.getHostConfig()).withRuntime(request.getRuntime());
      }

      return containerCmd.exec().getId();
    } catch (final Exception exception) {
      log.error("Unable to create container: {}", exception.getMessage(), exception);
    }

    return null;
  }

  private void checkAndPullImage(final String image) {

    try {
      dockerClient.inspectImageCmd(image).exec();
    } catch (final NotFoundException e) {
      log.info("Image not found locally, going to pull {}", image);

      pullImage(image);
    }
  }

  private void checkAndRemoveContainer(final String container) {

    try {
      dockerClient.removeContainerCmd(container).exec();
    } catch (final NotFoundException e) {
      // ignored
    }
  }
}
