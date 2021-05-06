/* Licensed under Apache-2.0 */
package io.encodely.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EncoderProfile {

  @Id @GeneratedValue private long id;

  private String name;

  private String command;

  private String extension;

  private String dockerImage;

  private String dockerRuntime;

  private boolean active;
}
