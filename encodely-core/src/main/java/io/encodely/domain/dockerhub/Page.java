/* Licensed under Apache-2.0 */
package io.encodely.domain.dockerhub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Page {

  @JsonProperty("count")
  public Integer count;

  @JsonProperty("next")
  public String next;

  @JsonProperty("previous")
  public Object previous;

  @JsonProperty("results")
  @Builder.Default
  public List<Result> results = new ArrayList<>();
}
