/* Licensed under Apache-2.0 */
package io.encodely.startup;

import io.encodely.common.services.ConfigService;
import io.encodely.exceptions.SocatException;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocatTask {

  private final ConfigService configService;

  private final DefaultExecutor defaultExecutor;

  /**
   * SOCAT exposes the Docker API so we don't need to make changes to the host docker daemon. If
   * this fails to start for any reason, kill the application as it cannot run without SOCAT.
   */
  public void start() throws SocatException {
    ignoreError(defaultExecutor, "docker stop {0}socat");
    ignoreError(defaultExecutor, "docker rm {0}socat");

    try {
      log.info("SOCAT - Starting...");

      defaultExecutor.execute(
          CommandLine.parse(
              MessageFormat.format(
                  configService.docker().getSocatCommand(),
                  configService.docker().getContainerPrefix())));
    } catch (final Exception exception) {
      log.error("Startup error: {}", exception.getMessage(), exception);

      throw new SocatException(
          "Cannot run without SOCAT. Check your SOCAT configuration in settings.", exception);
    }

    log.info("SOCAT - Start completed.");
  }

  /**
   * Execute a command but ignore all errors. For example don't throw an exception if we can't
   * remove a docker container because it doesn't exist.
   */
  private void ignoreError(final DefaultExecutor defaultExecutor, final String command) {
    try {
      defaultExecutor.execute(
          CommandLine.parse(
              MessageFormat.format(command, configService.docker().getContainerPrefix())));
    } catch (final Exception ignored) {
      // Ignored
    }
  }
}
