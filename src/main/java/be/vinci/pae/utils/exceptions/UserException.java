package be.vinci.pae.utils.exceptions;

/**
 * UserException class, deals with exceptions caused by user error.
 */
public class UserException extends RuntimeException {

  /**
   * Constructor of UserException.
   *
   * @param message the message of the exception
   */
  public UserException(String message) {
    super(message);
  }
}