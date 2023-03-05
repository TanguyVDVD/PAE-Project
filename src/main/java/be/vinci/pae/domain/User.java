package be.vinci.pae.domain;

/**
 * User interface representing a user in the domain.
 */
public interface User extends UserDTO {

  /**
   * Return true if the password is correct, false if he is not.
   *
   * @return a boolean
   */
  boolean isPasswordCorrect();

}
