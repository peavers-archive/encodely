/* Licensed under Apache-2.0 */
package io.encodely.jobmanager.domain;

import java.util.function.Function;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {

  @Id @GeneratedValue private long id;

  private long startTime;

  private long endTime;

  private String name;

  private JobStatus status;

  @Lob private String output;

  @Transient private Object input;

  @Transient private Function<Job, Job> worker;
}
