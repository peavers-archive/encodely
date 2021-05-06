/* Licensed under Apache-2.0 */
package io.encodely.startup;

import io.encodely.exceptions.SocatException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupTask implements ApplicationListener<ApplicationReadyEvent> {

  private final SocatTask socatTask;

  private final WatchTask watchTask;

  private final SetDefaultProfilesTask setDefaultProfilesTask;

  @Override
  public void onApplicationEvent(final @NonNull ApplicationReadyEvent event) {

    setDefaultProfilesTask.setDefaults();

    try {
      socatTask.start();
      watchTask.start();
    } catch (final SocatException exception) {
      log.error(exception.getMessage());
    }
  }
}
