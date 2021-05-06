/* Licensed under Apache-2.0 */
package io.encodely.controllers;

import io.encodely.dao.EncoderProfile;
import io.encodely.exceptions.NotFoundException;
import io.encodely.services.EncoderProfileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/api/encoder-profile")
@RequiredArgsConstructor
public class EncoderProfileController {

  private final EncoderProfileService encoderProfileService;

  @PostMapping
  public ResponseEntity<EncoderProfile> save(@RequestBody final EncoderProfile encoderProfile) {

    return ResponseEntity.ok(encoderProfileService.save(encoderProfile));
  }

  @GetMapping
  public ResponseEntity<List<EncoderProfile>> findAll() {

    return ResponseEntity.ok(encoderProfileService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<EncoderProfile> findById(@PathVariable final long id) {

    try {
      return ResponseEntity.ok(encoderProfileService.findById(id));
    } catch (final NotFoundException notFoundException) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable final long id) {

    encoderProfileService.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PostMapping("default")
  public ResponseEntity<Object> setDefaultProfile(
      @RequestBody final EncoderProfile encoderProfile) {

    return ResponseEntity.ok(encoderProfileService.setNewDefault(encoderProfile));
  }
}
