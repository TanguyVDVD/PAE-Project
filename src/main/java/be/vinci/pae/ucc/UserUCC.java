package be.vinci.pae.ucc;

import be.vinci.pae.domain.UserDTO;

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


  /**
   * Method that register a user if the parameters are correct.
   *
   * @param userDTO the user to register
   * @return the user if it works, else null
   */
  UserDTO register(UserDTO userDTO);

}
