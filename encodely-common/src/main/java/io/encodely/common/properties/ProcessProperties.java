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
public class ProcessProperties {

  @Builder.Default @Id private long id = 1L;

  /** Delete the source video after encoding it. */
  private boolean deleteSource;

  /** Reformat the output name. See for details on what this means. */
  private boolean cleanFilename;

  /** Replace the existing file with the transcode file. */
  private boolean overwrite;
}
