/* Licensed under Apache-2.0 */
package io.encodely.controllers;

import io.encodely.dao.MediaFile;
import io.encodely.services.MediaFileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/media")
@RequiredArgsConstructor
public class MediaFileController {

  private final MediaFileService mediaFileService;

  @GetMapping
  public ResponseEntity<List<MediaFile>> findAll() {
    return ResponseEntity.ok(mediaFileService.findAll());
  }
}
