/* Licensed under Apache-2.0 */
package io.encodely.jobmanager.repositories;

import io.encodely.jobmanager.domain.Job;
import io.encodely.jobmanager.domain.JobStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

  List<Job> findAllByOrderByEndTimeDesc();

  List<Job> findAllByStatus(JobStatus status);
}
