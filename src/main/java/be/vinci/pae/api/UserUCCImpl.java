package be.vinci.pae.api;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;

public class UserUCCImpl implements UserUCC {

  /**
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
   * @param userDTO
   * @return the user that has been logged out
   */
  @Override
  @GET
  public UserDTO logout(UserDTO userDTO) {
    return userDTO;
  }

}
