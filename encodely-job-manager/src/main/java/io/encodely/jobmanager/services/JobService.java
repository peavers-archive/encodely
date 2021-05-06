/* Licensed under Apache-2.0 */
package io.encodely.jobmanager.services;

import io.encodely.jobmanager.domain.Job;
import java.util.List;

public interface JobService {

  List<Job> findAll();

  List<Job> findAllRunning();

  Job save(Job job);

  Job start(final Job job);

  void cancel(long id);

  void deleteAll();
}
