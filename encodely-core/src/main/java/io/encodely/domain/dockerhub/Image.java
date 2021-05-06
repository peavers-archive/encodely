/* Licensed under Apache-2.0 */
package io.encodely.domain.dockerhub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image {

  @JsonProperty("architecture")
  public String architecture;

  @JsonProperty("features")
  public String features;

  @JsonProperty("variant")
  public Object variant;

  @JsonProperty("digest")
  public String digest;

  @JsonProperty("os")
  public String os;

  @JsonProperty("os_features")
  public String osFeatures;

  @JsonProperty("os_version")
  public Object osVersion;

  @JsonProperty("size")
  public Integer size;

  @JsonProperty("status")
  public String status;

  @JsonProperty("last_pulled")
  public String lastPulled;

  @JsonProperty("last_pushed")
  public String lastPushed;
}
