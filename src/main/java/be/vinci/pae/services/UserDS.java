package be.vinci.pae.services;

import be.vinci.pae.domain.UserDTO;

/**
 * UserDS interface that provide the method to interact with the db.
 */
public interface UserDS {

  /**
   * Insert a new user in the db.
   *
   * @param userDTO the user to insert in the db
   * @return true if succeed false if not
   */
  UserDTO insert(UserDTO userDTO);

  /**
   * Get the user by the email.
   *
   * @param email the user
   * @return the user correponding to the email
   */
  UserDTO getOneByEmail(String email);

}
