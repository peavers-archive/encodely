/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.dao.EncoderProfile;
import io.encodely.dao.MediaFile;

public interface TranscodeService {

  MediaFile transcode(MediaFile mediaFile, EncoderProfile encoderProfile);
}
