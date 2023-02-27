package be.vinci.pae.api;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * Class that implements the UserUCC interface provide the declared class login and logout
 */
public class UserUCCImpl implements UserUCC {

  /**
   * Method that login a user if the parameters are correct
   *
   * @param userDTO the user to login
   * @param request the request
   * @return the user that has been logged
   * @throws throw an exception if there is a problem
   */
  @Override
  @POST
  public UserDTO login(UserDTO userDTO, @Context ContainerRequest request) {
    User user = (User) userDTO;
    if (!user.isPasswordCorrect()) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).entity("Le mot de passe n'est pas correct")
              .type("text/plain").build());
    }
    return (UserDTO) user;
  }

  /**
   * Method that allow a user to logout
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
