/* Licensed under Apache-2.0 */
package io.encodely.exceptions;

public class InvalidOutputException extends RuntimeException {

  public InvalidOutputException(final String message, final Throwable cause) {

    super(message, cause);
  }
}
