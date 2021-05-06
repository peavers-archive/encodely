/* Licensed under Apache-2.0 */
package io.encodely.controllers;

import io.encodely.common.domain.Config;
import io.encodely.common.properties.DockerProperties;
import io.encodely.common.properties.ProcessProperties;
import io.encodely.common.properties.ScanProperties;
import io.encodely.common.services.ConfigService;
import io.encodely.services.DockerImageService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/api/config")
@RequiredArgsConstructor
public class ConfigController {

  private final ConfigService configService;

  private final DockerImageService dockerImageService;

  @GetMapping
  public ResponseEntity<Config> find() {
    return ResponseEntity.ok(configService.find());
  }

  @PostMapping("/docker")
  public ResponseEntity<Config> docker(@RequestBody final DockerProperties dockerProperties) {
    final Config config = configService.find();
    config.setDocker(dockerProperties);

    return ResponseEntity.ok(configService.save(config));
  }

  @PostMapping("/process")
  public ResponseEntity<Config> process(@RequestBody final ProcessProperties processProperties) {
    final Config config = configService.find();
    config.setProcess(processProperties);

    return ResponseEntity.ok(configService.save(config));
  }

  @PostMapping("/scan")
  public ResponseEntity<Config> scan(@RequestBody final ScanProperties scanProperties) {
    final Config config = configService.find();
    config.setScan(scanProperties);

    return ResponseEntity.ok(configService.save(config));
  }

  @GetMapping("/restart")
  public void restart() {
    Restarter.getInstance().restart();
  }

  @GetMapping("/images")
  public ResponseEntity<List<String>> images() {

    final String transcodeContainer = configService.app().getTranscodeContainer();

    final String imageUrl =
        UriComponentsBuilder.fromUriString("https://hub.docker.com")
            .pathSegment("v2")
            .pathSegment("repositories")
            .pathSegment(transcodeContainer)
            .pathSegment("tags")
            .queryParam("page_size", 100)
            .build()
            .toUriString();

    return ResponseEntity.ok(
        dockerImageService.getTags(imageUrl, transcodeContainer).stream()
            .distinct()
            .collect(Collectors.toList()));
  }
}
