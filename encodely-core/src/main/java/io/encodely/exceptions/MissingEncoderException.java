/* Licensed under Apache-2.0 */
package io.encodely.exceptions;

public class MissingEncoderException extends Exception {

  public MissingEncoderException() {}

  public MissingEncoderException(final String message) {

    super(message);
  }

  public MissingEncoderException(final String message, final Throwable cause) {

    super(message, cause);
  }
}
