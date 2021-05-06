/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.output;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseConsumer<T extends BaseConsumer<T>> implements Consumer<OutputFrame> {

  @Getter @Setter private boolean removeColorCodes = true;

  public T withRemoveAnsiCodes(final boolean removeAnsiCodes) {

    this.removeColorCodes = removeAnsiCodes;
    return (T) this;
  }
}
