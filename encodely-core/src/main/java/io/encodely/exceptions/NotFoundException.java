/* Licensed under Apache-2.0 */
package io.encodely.exceptions;

public class NotFoundException extends Exception {

  public NotFoundException() {
    super("Entity not found.");
  }
}
