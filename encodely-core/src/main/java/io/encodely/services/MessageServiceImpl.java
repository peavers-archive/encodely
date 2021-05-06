/* Licensed under Apache-2.0 */
package io.encodely.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final SimpMessagingTemplate messagingTemplate;

  private final ExecutorService messageExecutor = Executors.newCachedThreadPool();

  @Override
  public void sendMessage(final String topic, final Object payload) {
    messageExecutor.submit(() -> messagingTemplate.convertAndSend(topic, payload));
  }

  @PreDestroy
  public void onExit() {
    messageExecutor.shutdownNow();
  }
}
