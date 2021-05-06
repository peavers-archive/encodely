/* Licensed under Apache-2.0 */
package io.encodely.controllers;

import io.encodely.dao.EncoderProfile;
import io.encodely.jobmanager.domain.Job;
import io.encodely.jobmanager.services.JobService;
import io.encodely.jobs.JobFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/api/job")
@RequiredArgsConstructor
public class JobController {

  private final JobService jobService;

  private final JobFactory jobFactory;

  @GetMapping
  public ResponseEntity<List<Job>> findAll() {

    return ResponseEntity.ok(jobService.findAll());
  }

  @GetMapping("/running")
  public ResponseEntity<List<Job>> findAllRunning() {

    return ResponseEntity.ok(jobService.findAllRunning());
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteAll() {

    jobService.deleteAll();

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancelJob(@PathVariable final long id) {

    jobService.cancel(id);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/transcode-job")
  public ResponseEntity<List<Job>> startTranscode(
      @RequestBody final EncoderProfile encoderProfile) {

    return ResponseEntity.ok(jobFactory.transcodeAllJob(encoderProfile));
  }

  @PostMapping("/scan-job")
  public ResponseEntity<List<Job>> startScan() {

    return ResponseEntity.ok(jobFactory.scanJob());
  }
}
