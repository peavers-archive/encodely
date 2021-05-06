/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.dao.EncoderProfile;
import io.encodely.exceptions.NotFoundException;
import io.encodely.repositories.EncoderProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EncoderProfileServiceImpl implements EncoderProfileService {

  private final EncoderProfileRepository encoderProfileRepository;

  @Override
  public List<EncoderProfile> findAll() {
    return encoderProfileRepository.findAll();
  }

  @Override
  public EncoderProfile findById(final long id) throws NotFoundException {
    return encoderProfileRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  @Override
  public EncoderProfile save(final EncoderProfile encoderProfile) {
    return encoderProfileRepository.save(encoderProfile);
  }

  @Override
  public EncoderProfile findActive() {
    return encoderProfileRepository.findDistinctByActiveIsTrue();
  }

  @Override
  public void delete(final long id) {
    encoderProfileRepository.deleteById(id);
  }

  @Override
  public EncoderProfile setNewDefault(final EncoderProfile encoderProfile) {

    // Set current active as false
    final EncoderProfile activeIsTrue = encoderProfileRepository.findDistinctByActiveIsTrue();
    activeIsTrue.setActive(false);
    save(activeIsTrue);

    // Set new one as true
    encoderProfile.setActive(true);
    return save(encoderProfile);
  }
}
