/* Licensed under Apache-2.0 */
package io.encodely;

import io.encodely.dockerapi.services.DockerService;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = "io.encodely.*")
public class Application {

  private final DockerService dockerService;

  public static void main(final String[] args) {

    SpringApplication.run(Application.class, args);
  }

  @PreDestroy
  public void onExit() {
    dockerService.stopAll();
  }
}
