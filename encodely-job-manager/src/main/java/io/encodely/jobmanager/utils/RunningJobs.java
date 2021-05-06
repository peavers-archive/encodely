/* Licensed under Apache-2.0 */
package io.encodely.jobmanager.utils;

import io.encodely.jobmanager.domain.Job;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class RunningJobs {

  private final ConcurrentHashMap<Long, ScheduledFuture<?>> JOBS = new ConcurrentHashMap<>();

  public void add(final Job job, final ScheduledFuture<?> schedule) {

    JOBS.put(job.getId(), schedule);
  }

  public void stop(Long id) {

    if (JOBS.containsKey(id)) {
      JOBS.get(id).cancel(true);
      JOBS.remove(id);
    }
  }

  public void clear() {
    JOBS.clear();
  }

  public List<Job> findAll() {

    return JOBS.entrySet().stream()
        .filter(Job.class::isInstance)
        .map(Job.class::cast)
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
