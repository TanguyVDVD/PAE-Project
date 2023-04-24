package be.vinci.pae.services.user;

import be.vinci.pae.domain.user.UserDTO;
import java.util.List;

/**
 * UserDAO interface that provide the method to interact with the db.
 */
public interface UserDAO {

  /**
   * Insert a new user in the db.
   *
   * @param userDTO the user to insert in the db
   * @return the user inserted in the db
   */
  UserDTO insert(UserDTO userDTO);

  /**
   * Get the user by the email.
   *
   * @param email the user
   * @return the user corresponding to the email
   */
  UserDTO getOneByEmail(String email);

  /**
   * Get the user by the phone number.
   *
   * @param phoneNumber the user
   * @return the user corresponding to the phone number
   */
  UserDTO getOneByPhoneNumber(String phoneNumber);

  /**
   * Get the user by the id.
   *
   * @param id the user
   * @return the user corresponding to the id
   */
  UserDTO getOneById(int id);

  /**
   * Get all the users.
   *
   * @param query query to filter users
   * @return the list of users
   */
  List<UserDTO> getAll(String query);

  /**
   * Update a user.
   *
   * @param userDTO the user to update
   * @return user updated
   */
  UserDTO update(UserDTO userDTO);
}
