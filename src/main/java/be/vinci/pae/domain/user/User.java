package be.vinci.pae.domain.user;

/**
 * User interface representing a user in the domain.
 */
public interface User extends UserDTO {

  /**
   * Return true if the password is correct, false if he is not.
   *
   * @param password the password to check
   * @return a boolean
   */
  boolean isPasswordCorrect(String password);


  /**
   * Method that hash a password.
   *
   * @param password the password to hash
   * @return the password when it's hash
   */
  String hashPassword(String password);

}
