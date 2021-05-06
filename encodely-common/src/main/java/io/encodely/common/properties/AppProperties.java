/* Licensed under Apache-2.0 */
package io.encodely.common.properties;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("application")
public class AppProperties {

  @Builder.Default @Id private long id = 1L;

  private String transcodeContainer;

  private int cacheDurationInSeconds;
}
