/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.domain;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;

public class OutputFrame extends Frame {

  public OutputFrame(final StreamType streamType, final byte[] payload) {

    super(streamType, payload);
  }
}
