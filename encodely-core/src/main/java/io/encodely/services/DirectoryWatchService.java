/* Licensed under Apache-2.0 */
package io.encodely.services;

import java.io.IOException;
import java.nio.file.Path;

public interface DirectoryWatchService {

  void watch(Path directory) throws IOException, InterruptedException;
}
