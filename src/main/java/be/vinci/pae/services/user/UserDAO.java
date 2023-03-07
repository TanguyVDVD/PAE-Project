package be.vinci.pae.services.user;

import be.vinci.pae.domain.user.UserDTO;

/**
 * UserDAO interface that provide the method to interact with the db.
 */
public interface UserDAO {

  /**
   * Insert a new user in the db.
   *
   * @param userDTO the user to insert in the db
   * @return true if succeed false if not
   */
  boolean insert(UserDTO userDTO);

  /**
   * Get the user by the email.
   *
   * @param email the user
   * @return the user correponding to the email
   */
  UserDTO getOneByEmail(String email);

}
