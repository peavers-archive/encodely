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
public class ScanProperties {

  @Builder.Default @Id private long id = 1L;

  /** Root directory to scan from. This is recursive and will find all media files under it. */
  private String input;

  /** Output directory to put newly encoded videos. */
  private String output;

  /** Ignore any paths while scanning. */
  private String exclude;

  /**
   * Ignore all files smaller than this value. If set as -1 this property will be ignored and all
   * files regardless of size will be scanned for further processing. Value is in bytes, for example
   * 10485760 is about 100mb
   */
  @Builder.Default private long minFileSize = -1;
}
