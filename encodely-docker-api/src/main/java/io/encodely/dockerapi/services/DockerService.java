/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.services;

import io.encodely.dockerapi.domain.CreateContainerRequest;
import io.encodely.dockerapi.domain.StartResult;
import java.io.IOException;

public interface DockerService {

  StartResult start(String containerId) throws IOException, InterruptedException;

  void stopAll();

  void removeByContainerId(String containerId);

  void pullImage(String repository);

  long getExitCode(String containerId);

  String createContainer(CreateContainerRequest createContainerRequest);
}
