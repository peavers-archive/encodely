/* Licensed under Apache-2.0 */
package io.encodely.common.properties;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DockerProperties {

  @Builder.Default @Id private long id = 1L;

  @Builder.Default
  private String socatCommand =
      "docker run --rm --name {0}socat -v /var/run/docker.sock:/var/run/docker.sock -p 127.0.0.1:2375:2375 -d bobrik/socat TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock";

  @Builder.Default private String containerPrefix = "encodely-";
}
