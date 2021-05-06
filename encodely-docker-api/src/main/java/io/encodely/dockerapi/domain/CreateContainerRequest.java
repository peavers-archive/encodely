/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.domain;

import com.google.common.base.Joiner;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class CreateContainerRequest {

  @NonNull private final String image;

  @NonNull private final String inputDir;

  @NonNull private final String outputDir;

  @NonNull private final String name;

  private final String runtime;

  @Builder.Default private final List<String> command = new ArrayList<>();

  public String getCommandForPrint() {

    return MessageFormat.format("ffmpeg {0}", Joiner.on(" ").join(this.command));
  }
}
