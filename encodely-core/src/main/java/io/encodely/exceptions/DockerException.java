/* Licensed under Apache-2.0 */
package io.encodely.exceptions;

public class DockerException extends RuntimeException {

  public DockerException() {}

  public DockerException(final String message) {

    super(message);
  }

  public DockerException(final String message, final Throwable cause) {

    super(message, cause);
  }
}
