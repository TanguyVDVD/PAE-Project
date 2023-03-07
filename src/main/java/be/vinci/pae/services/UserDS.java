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
  boolean insert(UserDTO userDTO);

  /**
   * Get the user by the email.
   *
   * @param email the user
   * @return the user correponding to the email
   */
  UserDTO getOneByEmail(String email);

  /**
   * Returns a UserDTO object corresponding to the specified phone number.
   *
   * @param phoneNumber the phone number of the user to retrieve
   * @return the UserDTO object corresponding to the specified phone number, or null if no user is
   * found
   */
  UserDTO getOneByPhoneNumber(String phoneNumber);

}
