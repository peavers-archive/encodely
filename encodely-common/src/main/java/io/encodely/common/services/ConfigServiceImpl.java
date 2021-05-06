/* Licensed under Apache-2.0 */
package io.encodely.common.services;

import io.encodely.common.domain.Config;
import io.encodely.common.properties.AppProperties;
import io.encodely.common.properties.DockerProperties;
import io.encodely.common.properties.ProcessProperties;
import io.encodely.common.properties.ScanProperties;
import io.encodely.common.repositories.ConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

  private final AppProperties appProperties;

  private final ConfigRepository configRepository;

  @Override
  public Config save(final Config config) {
    return configRepository.save(config);
  }

  @Override
  public Config find() {
    return configRepository.findById(1L).orElseGet(() -> Config.builder().build());
  }

  @Override
  public AppProperties app() {
    return appProperties;
  }

  @Override
  public ScanProperties scan() {
    return find().getScan();
  }

  @Override
  public DockerProperties docker() {
    return find().getDocker();
  }

  @Override
  public ProcessProperties process() {
    return find().getProcess();
  }
}
