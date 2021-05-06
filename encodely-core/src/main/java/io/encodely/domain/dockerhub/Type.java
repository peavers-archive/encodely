/* Licensed under Apache-2.0 */
package io.encodely.domain.dockerhub;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Type {

  private String name;

  @Builder.Default private List<Tag> tags = new ArrayList<>();
}
