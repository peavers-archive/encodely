/* Licensed under Apache-2.0 */
package io.encodely.common.domain;

import io.encodely.common.properties.DockerProperties;
import io.encodely.common.properties.ProcessProperties;
import io.encodely.common.properties.ScanProperties;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Config {

  @Builder.Default @Id private long id = 1L;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "scan_id", referencedColumnName = "id")
  @Builder.Default
  private ScanProperties scan = new ScanProperties();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "docker_id", referencedColumnName = "id")
  @Builder.Default
  private DockerProperties docker = new DockerProperties();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "process_id", referencedColumnName = "id")
  @Builder.Default
  private ProcessProperties process = new ProcessProperties();
}
