/* Licensed under Apache-2.0 */
package io.encodely.services;

import java.util.List;

public interface DockerImageService {

  List<String> getTags(String endpoint, String imageName);
}
