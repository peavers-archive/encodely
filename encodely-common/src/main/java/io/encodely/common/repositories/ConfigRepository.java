/* Licensed under Apache-2.0 */
package io.encodely.common.repositories;

import io.encodely.common.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {}
