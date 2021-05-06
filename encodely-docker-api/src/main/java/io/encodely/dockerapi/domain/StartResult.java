/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class StartResult {
  String id;

  Long exitCode;

  String stdout;

  String stderr;
}
