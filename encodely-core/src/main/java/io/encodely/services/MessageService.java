/* Licensed under Apache-2.0 */
package io.encodely.services;

public interface MessageService {

  void sendMessage(String topic, Object payload);
}
