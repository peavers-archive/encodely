/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.dao.EncoderProfile;
import io.encodely.exceptions.NotFoundException;
import java.util.List;

public interface EncoderProfileService {

  List<EncoderProfile> findAll();

  EncoderProfile findById(long id) throws NotFoundException;

  EncoderProfile save(EncoderProfile encoderProfile);

  EncoderProfile findActive();

  void delete(long id);

  EncoderProfile setNewDefault(EncoderProfile encoderProfile);
}
