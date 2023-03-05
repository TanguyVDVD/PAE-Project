package be.vinci.pae.ucc;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.services.UserDS;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;

/**
 * UserUCCImpl class that implements the UserUCC interface provide the declared class login and
 * logout.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDS myUserDS;

  @Inject
  private DomainFactory domainFactory;


  /**
   * Method that login a user if the parameters are correct.
   *
   * @param email
   * @return the user that has been logged
   */
  @Override
  public UserDTO login(String email, String password) {
    UserDTO user = domainFactory.getUser();
    user.setEmail(email);
    user.setPassword(password);
    //myUserDS.getOneByEmail(email);
    return user;
  }

  /**
   * Method that allow a user to logout.
   *
   * @param userDTO the user to logout
   * @return the user that has been logged out
   */
  @Override
  @GET
  public UserDTO logout(UserDTO userDTO) {
    return userDTO;
  }

}
