/* Licensed under Apache-2.0 */
package io.encodely.jobmanager.services;

import io.encodely.jobmanager.domain.Job;
import io.encodely.jobmanager.domain.JobStatus;
import io.encodely.jobmanager.repositories.JobRepository;
import io.encodely.jobmanager.utils.RunningJobs;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

  private final JobRepository jobRepository;

  private final ScheduledExecutorService executor;

  @Override
  public List<Job> findAll() {

    return jobRepository.findAllByOrderByEndTimeDesc();
  }

  @Override
  public List<Job> findAllRunning() {

    return jobRepository.findAllByStatus(JobStatus.RUNNING);
  }

  @Override
  public Job save(final Job job) {

    return jobRepository.save(job);
  }

  @Override
  public void deleteAll() {

    RunningJobs.clear();

    jobRepository.deleteAll();
  }

  @Override
  public Job start(final Job jobInput) {

    final Job savedJob = save(jobInput);

    final ScheduledFuture<Job> schedule =
        executor.schedule(() -> savedJob.getWorker().apply(savedJob), 0, TimeUnit.MILLISECONDS);

    RunningJobs.add(savedJob, schedule);

    return savedJob;
  }

  @Override
  public void cancel(final long id) {

    RunningJobs.stop(id);

    jobRepository
        .findById(id)
        .ifPresent(
            job -> {
              job.setEndTime(Instant.now().toEpochMilli());
              job.setStatus(JobStatus.ABORT);
              jobRepository.save(job);
            });
  }

  @PreDestroy
  public void onExit() {
    executor.shutdownNow();
  }
}
