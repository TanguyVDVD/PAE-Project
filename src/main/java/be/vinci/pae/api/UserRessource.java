package be.vinci.pae.api;

import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.ucc.UserUCC;
import be.vinci.pae.ucc.UserUCCImpl;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * UserRessource class.
 */
public class UserRessource {

  //@Inject
  //private UserUCC userUCC;

  private UserUCC userUCC = new UserUCCImpl();

  /**
   * Login a user with json object return the user created and the token.
   *
   * @param json a json object
   * @return a user when is created
   */
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @POST
  public UserDTO login(JsonNode json) {

    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      throw new WebApplicationException("email or password required", Response.Status.BAD_REQUEST);
    }
    String login = json.get("email").asText();
    String password = json.get("password").asText();
    UserDTO userDTO = userUCC.login(login, password);

    //token

    return userDTO;
  }

}
