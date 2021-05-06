/* Licensed under Apache-2.0 */
package io.encodely.jobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.encodely.common.services.ConfigService;
import io.encodely.jobmanager.domain.Job;
import io.encodely.jobmanager.domain.JobStatus;
import io.encodely.jobmanager.services.JobService;
import io.encodely.services.MessageService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Basically a utility class without being a utility class. Has encodely specific job helper
 * functions defined.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class JobHelper {

  protected final MessageService messageService;

  protected final JobService jobService;

  protected final ConfigService configService;

  protected final ObjectMapper objectMapper;

  /**
   * Configure the {@link Job} as the start state. This will also send a notification to the message
   * service.
   */
  protected Job setStartState(final Job job) {

    job.setStatus(JobStatus.RUNNING);
    job.setStartTime(Instant.now().toEpochMilli());

    messageService.sendMessage("/topic/job", job);

    return jobService.save(job);
  }

  /**
   * Configure the {@code Job} as the end state. This will also send a notification to the message
   * service.
   */
  protected Job setEndState(final Job job, final Object output, final boolean error) {

    job.setStatus(error ? JobStatus.ERROR : JobStatus.DONE);
    job.setEndTime(Instant.now().toEpochMilli());

    try {
      job.setOutput(objectMapper.writeValueAsString(output));
    } catch (final JsonProcessingException e) {
      log.warn("Unable to set output value for job: {}", job.getId());
    }

    messageService.sendMessage("/topic/job", job);

    return jobService.save(job);
  }
}
