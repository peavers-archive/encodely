/* Licensed under Apache-2.0 */
package io.encodely.configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadConfig {

  @Bean
  public ScheduledExecutorService executors() {

    return Executors.newSingleThreadScheduledExecutor();
  }
}
