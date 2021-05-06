/* Licensed under Apache-2.0 */
package io.encodely.services;

import io.encodely.domain.dockerhub.Page;
import io.encodely.domain.dockerhub.Result;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockerImageServiceImpl implements DockerImageService {

  private static final List<String> TAGS = new ArrayList<>();
  private final RestTemplate restTemplate;

  @Override
  @Cacheable(cacheNames = "docker-tags")
  public List<String> getTags(final String endpoint, final String imageName) {

    final Page page = restTemplate.getForEntity(endpoint, Page.class).getBody();

    if (page != null) {
      page.getResults().forEach(getResultConsumer(imageName));
    }

    if (page != null && StringUtils.isNoneEmpty(page.getNext())) {
      getTags(page.getNext(), imageName);
    }

    return TAGS;
  }

  private Consumer<Result> getResultConsumer(final String imageName) {

    return result -> TAGS.add(MessageFormat.format("{0}:{1}", imageName, result.getName()));
  }
}
