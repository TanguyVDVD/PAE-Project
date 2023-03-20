package be.vinci.pae.ucc.user;

import be.vinci.pae.domain.user.UserDTO;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * UserUCC interface that provide the various methods for the user.
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
   * @return the user that has been registered
   */
  UserDTO register(UserDTO userDTO);

  /**
   * Returns a list of all users.
   *
   * @param query query to filter users
   * @return a list of all users
   */
  List<UserDTO> getUsers(String query);

  /**
   * Get the profile picture of a user.
   *
   * @param id id of the user
   * @return the profile picture of the user
   */
  File getProfilePicture(int id);

  /**
   * Update the profile picture of a user.
   *
   * @param id    id of the user
   * @param photo the photo to set
   * @return whether the update was successful
   */
  boolean updateProfilePicture(int id, InputStream photo);


}
