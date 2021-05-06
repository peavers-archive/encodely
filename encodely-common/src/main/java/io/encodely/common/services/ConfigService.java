/* Licensed under Apache-2.0 */
package io.encodely.common.services;

import io.encodely.common.domain.Config;
import io.encodely.common.properties.AppProperties;
import io.encodely.common.properties.DockerProperties;
import io.encodely.common.properties.ProcessProperties;
import io.encodely.common.properties.ScanProperties;

public interface ConfigService {

  Config save(Config config);

  Config find();

  AppProperties app();

  ScanProperties scan();

  DockerProperties docker();

  ProcessProperties process();
}
