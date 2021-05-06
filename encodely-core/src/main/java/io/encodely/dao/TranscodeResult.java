/* Licensed under Apache-2.0 */
package io.encodely.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscodeResult {

  @JsonIgnore @Id @GeneratedValue private long id;

  private long exitCode;

  private String containerId;

  private String outputExtension;

  private String encoderProfile;

  @Lob private String ffmpegCommand;

  @Lob private String stdout;

  @Lob private String stderr;

  @Lob private String outputFilename;

  @Lob private String outputPath;
}
