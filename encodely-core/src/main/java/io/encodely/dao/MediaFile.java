/* Licensed under Apache-2.0 */
package io.encodely.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaFile {

  @Id private String id;

  private String filename;

  @Lob private String path;

  private String extension;

  private long size;

  private boolean processed;

  @OneToOne(cascade = CascadeType.ALL)
  private TranscodeResult transcodeResult;

  @Override
  public boolean equals(final Object o) {

    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    final MediaFile mediaFile = (MediaFile) o;

    return new EqualsBuilder().append(size, mediaFile.size).append(id, mediaFile.id).isEquals();
  }

  @Override
  public int hashCode() {

    return new HashCodeBuilder(17, 37).append(id).append(size).toHashCode();
  }
}
