package be.vinci.pae.services;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.UserDTO;
import jakarta.inject.Inject;

/**
 * UserDS class that implements UserDs interface Provide the different methods.
 */
public class UserDSImpl implements UserDS {

  @Inject
  private static DomainFactory myDomainFactory;

  /**
   * Insert a new user in the db.
   *
   * @param userDTO the user to insert in the db
   * @return true if succeed false if not
   */
  @Override
  public UserDTO insert(UserDTO userDTO) {
    return null;
  }

  /**
   * Get the user by the email.
   *
   * @param email the user
   * @return the user correponding to the email
   */
  public UserDTO getOneByEmail(String email) {
    return null;
  }
}
