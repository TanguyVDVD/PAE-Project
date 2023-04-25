package be.vinci.pae.utils.exceptions;

/**
 * BusinessException class, deals with exceptions caused by user error.
 */
public class BusinessException extends RuntimeException {

  /**
   * Constructor of BusinessException.
   *
   * @param message the message of the exception
   */
  public BusinessException(String message) {
    super(message);
  }
}