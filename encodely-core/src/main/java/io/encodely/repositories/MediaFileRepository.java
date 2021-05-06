/* Licensed under Apache-2.0 */
package io.encodely.repositories;

import io.encodely.dao.MediaFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, String> {

  List<MediaFile> findAllByProcessedIsFalse();
}
