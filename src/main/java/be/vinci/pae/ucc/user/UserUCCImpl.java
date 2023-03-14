package be.vinci.pae.ucc.user;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.user.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;


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

  @Override
  public UserDTO register(UserDTO userDTO) {
    User userInDB = (User) myUserDAO.getOneByPhoneNumber(userDTO.getPhoneNumber());

    if (userInDB != null) {
      throw new WebApplicationException("Numéro de téléphone déjà utilisé",
          Response.Status.BAD_REQUEST);
    }

    User userTemp = (User) userDTO;
    userTemp.setPassword(userTemp.hashPassword(userTemp.getPassword()));
    UserDTO userDtoToInsert = (UserDTO) userTemp;
    System.out.println(userDtoToInsert.getPassword());

    if (!myUserDAO.insert(userDtoToInsert)) {
      return null;
    }

    return userDtoToInsert;
  }

  public List<UserDTO> getUsers(String query) {
    return myUserDAO.getAll(query);
  }

}
