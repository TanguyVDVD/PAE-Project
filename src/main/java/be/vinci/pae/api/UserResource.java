package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.AuthorizeAdmin;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.ucc.user.UserUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * UserResource class.
 */
@Singleton
@Path("/users")
public class UserResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private UserUCC userUCC;

  /**
   * Get a list of all users.
   *
   * @return a list of users
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getUsers() {
    return jsonMapper.valueToTree(userUCC.getAllUsers());
  }

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
  public ObjectNode login(JsonNode json) {

    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      throw new WebApplicationException("email or password required", Response.Status.BAD_REQUEST);
    }

    String login = json.get("email").asText();
    String password = json.get("password").asText();

    UserDTO userDTO = userUCC.login(login, password);

    if (userDTO == null) {
      throw new WebApplicationException("Login or password incorrect",
          Response.Status.UNAUTHORIZED);
    }

    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userDTO.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", userDTO.getId())
          .put("firstName", userDTO.getFirstName())
          .put("lastName", userDTO.getLastName())
          .put("isHelper", userDTO.isHelper());

      return publicUser;

    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }
  }

  /**
   * Retrieve the logged-in user's information.
   *
   * @param request the request
   * @return the user's information
   */
  @GET
  @Path("/my")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getUserInfo(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    ObjectNode publicUser = jsonMapper.createObjectNode()
        .put("id", user.getId())
        .put("firstName", user.getFirstName())
        .put("lastName", user.getLastName())
        .put("isHelper", user.isHelper());

    return publicUser;
  }
}
