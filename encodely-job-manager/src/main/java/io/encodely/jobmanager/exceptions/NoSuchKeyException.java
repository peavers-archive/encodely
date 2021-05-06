/* Licensed under Apache-2.0 */
package io.encodely.jobmanager.exceptions;

public class NoSuchKeyException extends Exception {

  public NoSuchKeyException() {
    super("No key found in the queue with that ID.");
  }
}
