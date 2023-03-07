package be.vinci.pae.ucc.user;

import be.vinci.pae.domain.user.UserDTO;

/**
 * UserUCC interface that provide the login and logout mehtod of a user.
 */
public interface UserUCC {

  /**
   * Method that login a user if the parameters are correct.
   *
   * @param email    the email of user
   * @param password the password of user
   * @return the user that has been logged
   */
  UserDTO login(String email, String password);

}
