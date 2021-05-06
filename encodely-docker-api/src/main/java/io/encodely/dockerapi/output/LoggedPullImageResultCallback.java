/* Licensed under Apache-2.0 */
package io.encodely.dockerapi.output;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.PullResponseItem;
import java.io.Closeable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggedPullImageResultCallback extends PullImageResultCallback {
  private final Set<String> allLayers = new HashSet<>();

  private final Set<String> downloadedLayers = new HashSet<>();

  private final Set<String> pulledLayers = new HashSet<>();

  private final Map<String, Long> totalSizes = new HashMap<>();

  private final Map<String, Long> currentSizes = new HashMap<>();

  private boolean completed;

  private Instant start;

  @Override
  public void onStart(final Closeable stream) {
    super.onStart(stream);
    start = Instant.now();

    log.info("Starting to pull image");
  }

  @Override
  public void onNext(final PullResponseItem item) {
    super.onNext(item);

    final String statusLowercase = item.getStatus() != null ? item.getStatus().toLowerCase() : "";
    final String id = item.getId();

    if (item.getProgressDetail() != null) {
      allLayers.add(id);
    }

    if (statusLowercase.equalsIgnoreCase("download complete")) {
      downloadedLayers.add(id);
    }

    if (statusLowercase.equalsIgnoreCase("pull complete")) {
      pulledLayers.add(id);
    }

    if (item.getProgressDetail() != null) {
      final Long total = item.getProgressDetail().getTotal();
      final Long current = item.getProgressDetail().getCurrent();

      if (total != null && total > totalSizes.getOrDefault(id, 0L)) {
        totalSizes.put(id, total);
      }
      if (current != null && current > currentSizes.getOrDefault(id, 0L)) {
        currentSizes.put(id, current);
      }
    }

    if (statusLowercase.startsWith("pulling from") || statusLowercase.contains("complete")) {

      final long totalSize = totalLayerSize();
      final long currentSize = downloadedLayerSize();

      final int pendingCount = allLayers.size() - downloadedLayers.size();
      final String friendlyTotalSize;
      if (pendingCount > 0) {
        friendlyTotalSize = "? MB";
      } else {
        friendlyTotalSize = byteCountToDisplaySize(totalSize);
      }

      log.info(
          "Pulling image layers: {} pending, {} downloaded, {} extracted, ({}/{})",
          format("%2d", pendingCount),
          format("%2d", downloadedLayers.size()),
          format("%2d", pulledLayers.size()),
          byteCountToDisplaySize(currentSize),
          friendlyTotalSize);
    }

    if (statusLowercase.contains("complete")) {
      completed = true;
    }
  }

  @Override
  public void onComplete() {
    super.onComplete();

    final long downloadedLayerSize = downloadedLayerSize();
    final long duration = Duration.between(start, Instant.now()).getSeconds();

    if (completed) {
      log.info(
          "Pull complete. {} layers, pulled in {}s (downloaded {} at {}/s)",
          allLayers.size(),
          duration,
          byteCountToDisplaySize(downloadedLayerSize),
          byteCountToDisplaySize(downloadedLayerSize / duration));
    }
  }

  private long downloadedLayerSize() {
    return currentSizes.values().stream().filter(Objects::nonNull).mapToLong(it -> it).sum();
  }

  private long totalLayerSize() {
    return totalSizes.values().stream().filter(Objects::nonNull).mapToLong(it -> it).sum();
  }
}
