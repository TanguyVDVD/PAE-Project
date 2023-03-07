package be.vinci.pae.ucc.user;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.user.UserDAO;
import jakarta.inject.Inject;

/**
 * UserUCCImpl class that implements the UserUCC interface provide the declared class login and
 * logout.
 */

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  /**
   * Method that login a user if the parameters are correct.
   *
   * @param email    the email of user
   * @param password the password of user
   * @return the user that has been logged
   */
  @Override
  public UserDTO login(String email, String password) {

    User userDB = (User) myUserDAO.getOneByEmail(email);

    if (userDB == null) {
      return null;
    }

    if (!userDB.isPasswordCorrect(password)) {
      return null;
    }

    return userDB;
  }

}
