package be.vinci.pae.ucc;

import be.vinci.pae.domain.UserDTO;

/**
 * UserUCC interface that provide the login and logout mehtod of a user.
 */
public interface UserUCC {

  /**
   * Method that login a user if the parameters are correct.
   *
   * @param email
   * @return the user that has been logged
   */
  UserDTO login(String email, String password);

  /**
   * Method that allow a user to logout.
   *
   * @param userDTO the user to logout
   * @return the user that has been logged out
   */
  UserDTO logout(UserDTO userDTO);

}
