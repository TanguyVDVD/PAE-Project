package be.vinci.pae.ucc;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.services.UserDS;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

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

    return userDB;
  }

  @Override
  public UserDTO register(UserDTO userDTO) {

    User userInDB = (User) myUserDS.getOneByPhoneNumber(userDTO.getPhoneNumber());

    if (userInDB != null) {
      throw new WebApplicationException("user already exist", Response.Status.BAD_REQUEST);
    }

    User userTemp = (User) userDTO;
    userTemp.setPassword(userTemp.hashPassword(userTemp.getPassword()));
    UserDTO userDtoToInsert = (UserDTO) userTemp;
    System.out.println(userDtoToInsert.getPassword());

    if (!myUserDS.insert(userDtoToInsert)) {
      return null;
    }

    return userDtoToInsert;
  }

}
