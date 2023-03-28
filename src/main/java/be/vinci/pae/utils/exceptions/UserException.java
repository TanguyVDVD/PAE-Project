package be.vinci.pae.utils.exceptions;

/**
 * UserException class.
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