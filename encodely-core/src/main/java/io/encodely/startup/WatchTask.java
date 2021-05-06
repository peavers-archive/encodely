/* Licensed under Apache-2.0 */
package io.encodely.startup;

import io.encodely.common.services.ConfigService;
import io.encodely.services.DirectoryWatchService;
import java.io.IOException;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WatchTask {

  private final ConfigService configService;

  private final DirectoryWatchService directoryWatchService;

  public void start() {
    final String input = configService.scan().getInput();

    if (StringUtils.isEmpty(input)) {
      return;
    }

    log.info("Monitoring {} for changes", input);

    try {
      directoryWatchService.watch(Path.of(input));
    } catch (final IOException | InterruptedException e) {
      log.error("Unable to monitor {} for changes: {}", input, e.getMessage(), e);
    }
  }
}
