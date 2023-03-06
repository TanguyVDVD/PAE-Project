package be.vinci.pae.ucc;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.services.UserDS;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;

/**
 * UserUCCImpl class that implements the UserUCC interface provide the declared class login and
 * logout.
 */

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDS myUserDS;

  /**
   * Method that login a user if the parameters are correct.
   *
   * @param email    the email of user
   * @param password the password of user
   * @return the user that has been logged
   */
  @Override
  public UserDTO login(String email, String password) {

    User userDB = (User) myUserDS.getOneByEmail(email);

    if (userDB == null) {
      return null;
    }

    if (!userDB.isPasswordCorrect(password)) {
      return null;
    }

    return (UserDTO) userDB;
  }

  /**
   * Method that allow a user to logout.
   *
   * @param userDTO the user to logout
   * @return the user that has been logged out
   */
  @Override
  @GET
  public UserDTO logout(UserDTO userDTO) {
    return userDTO;
  }

}
