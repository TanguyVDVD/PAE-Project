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
   * Get a user by the id.
   *
   * @param id the user
   * @return the user corresponding to the id
   */
  UserDTO getUserById(int id);

  /**
   * Update a user, after verifying password.
   *
   * @param userDTO  the user to update
   * @param password the password of the user
   * @return the updated user
   */
  UserDTO updateUser(UserDTO userDTO, String password);

  /**
   * Update a user.
   *
   * @param userDTO the user to update
   * @return the updated user
   */
  UserDTO updateUser(UserDTO userDTO);

  /**
   * Get a user's profile picture.
   *
   * @param userDTO the user
   * @return the profile picture of the user
   */
  File getProfilePicture(UserDTO userDTO);

  /**
   * Update a user's profile picture.
   *
   * @param userDTO  the user
   * @param password the password to verify
   * @param file     the new profile picture
   * @return the updated user
   */
  UserDTO updateProfilePicture(UserDTO userDTO, String password, InputStream file);
}
