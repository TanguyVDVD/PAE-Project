package be.vinci.pae.utils.exceptions;

/**
 * DALException class, deals with the exceptions of the DAL.
 */
public class DALException extends RuntimeException {

  /**
   * Constructor of DALException.
   *
   * @param message the message of the exception
   */
  public DALException(String message) {
    super(message);
  }

  /**
   * Constructor of DALException.
   *
   * @param message the message of the exception
   * @param cause   the cause of the exception
   */
  public DALException(String message, Throwable cause) {
    super(message, cause);
  }
}
