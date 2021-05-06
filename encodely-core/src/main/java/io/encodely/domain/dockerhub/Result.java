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
public class Result {

  @JsonProperty("creator")
  public Integer creator;

  @JsonProperty("id")
  public Integer id;

  @JsonProperty("image_id")
  public Object imageId;

  @JsonProperty("images")
  @Builder.Default
  public List<Image> images = new ArrayList<>();

  @JsonProperty("last_updated")
  public String lastUpdated;

  @JsonProperty("last_updater")
  public Integer lastUpdater;

  @JsonProperty("last_updater_username")
  public String lastUpdaterUsername;

  @JsonProperty("name")
  public String name;

  @JsonProperty("repository")
  public Integer repository;

  @JsonProperty("full_size")
  public Integer fullSize;

  @JsonProperty("v2")
  public Boolean v2;

  @JsonProperty("tag_status")
  public String tagStatus;

  @JsonProperty("tag_last_pulled")
  public String tagLastPulled;

  @JsonProperty("tag_last_pushed")
  public String tagLastPushed;
}
