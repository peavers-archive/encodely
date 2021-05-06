/* Licensed under Apache-2.0 */
package io.encodely.startup;

import io.encodely.dao.EncoderProfile;
import io.encodely.services.EncoderProfileService;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SetDefaultProfilesTask {

  public static final String DOCKER_IMAGE = "jrottenberg/ffmpeg:3.4";

  public static final String DOCKER_IMAGE_NVIDIA = "jrottenberg/ffmpeg:4.1-nvidia";

  private final EncoderProfileService encoderProfileService;

  @PostConstruct
  public void setDefaults() {

    final List<EncoderProfile> encoderProfiles =
        List.of(

            // Basic h264 with hardware encoding
            EncoderProfile.builder()
                .id(1L)
                .name("H264 NVENC")
                .command("-hwaccel cuvid -i $input -c:v h264_nvenc -vb 7M $output")
                .extension("mp4")
                .dockerImage(DOCKER_IMAGE_NVIDIA)
                .dockerRuntime("nvidia")
                .build(),

            // Basic h264
            EncoderProfile.builder()
                .id(2L)
                .name("H264")
                .command(
                    "-i $input -vcodec libx264 -pix_fmt yuv420p -profile:v baseline -level 3 $output")
                .extension("mp4")
                .dockerImage(DOCKER_IMAGE)
                .active(true)
                .build(),

            // https://gist.github.com/mikoim/27e4e0dc64e384adbcb91ff10a2d3678
            EncoderProfile.builder()
                .id(3L)
                .name("YouTube recommends setting")
                .command(
                    "-i $input -c:a libfdk_aac -profile:a aac_low -b:a 384k -pix_fmt yuv420p -c:v libx264 -profile:v high -preset slow -crf 18 -g 30 -bf 2 -movflags faststart $output")
                .extension("mp4")
                .dockerImage(DOCKER_IMAGE)
                .build(),

            // https://gist.github.com/Vestride/278e13915894821e1d6f
            EncoderProfile.builder()
                .id(4L)
                .name("Convert to MP4")
                .command("-i $input -vcodec h264 -acodec aac -strict -2 $output")
                .extension("mp4")
                .dockerImage(DOCKER_IMAGE)
                .build(),

            // https://gist.github.com/Vestride/278e13915894821e1d6f
            EncoderProfile.builder()
                .id(5L)
                .name("Convert to WebM VP8")
                .command(
                    "-i $input -vcodec libvpx -qmin 0 -qmax 50 -crf 10 -b:v 1M -acodec libvorbis $output")
                .extension("mp4")
                .dockerImage(DOCKER_IMAGE)
                .build(),

            // https://gist.github.com/Vestride/278e13915894821e1d6f
            EncoderProfile.builder()
                .id(6L)
                .name("Convert to WebM VP9")
                .command("-i $input -vcodec libvpx-vp9 -b:v 1M -acodec libvorbis $output")
                .extension("mp4")
                .dockerImage(DOCKER_IMAGE)
                .build(),

            // https://gist.github.com/steven2358/ba153c642fe2bb1e47485962df07c730
            EncoderProfile.builder()
                .id(7L)
                .name("Remux MKV file into MP4")
                .command("-i $input in.mkv -c:v copy -c:a copy $output")
                .extension("mp4")
                .dockerImage(DOCKER_IMAGE)
                .build());

    encoderProfiles.forEach(encoderProfileService::save);
  }
}
