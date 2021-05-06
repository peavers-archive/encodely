/* Licensed under Apache-2.0 */
package io.encodely.repositories;

import io.encodely.dao.EncoderProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncoderProfileRepository extends JpaRepository<EncoderProfile, Long> {

  EncoderProfile findDistinctByActiveIsTrue();
}
