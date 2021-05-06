/* Licensed under Apache-2.0 */
package io.encodely.repositories;

import io.encodely.dao.TranscodeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscodeResultRepository extends JpaRepository<TranscodeResult, Long> {}
