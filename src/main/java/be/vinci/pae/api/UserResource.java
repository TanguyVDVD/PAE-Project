package be.vinci.pae.api;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.ucc.UserUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

  @Inject
  private DomainFactory myDomainFactory;

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

    return createToken(userDTO);
  }

  /**
   * Register a user with json object return the user created and the token.
   *
   * @param json a json object
   * @return a user when is created
   */
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @POST
  public ObjectNode register(JsonNode json) {

    if (!json.hasNonNull("last_name") || !json.hasNonNull("first_name") || !json.hasNonNull(
        "phone_number") || !json.hasNonNull("email") || !json.hasNonNull("password")
        || !json.hasNonNull("photo") || !json.hasNonNull("register_date") || !json.hasNonNull(
        "is_helper")) {
      throw new WebApplicationException("All the field are required", Response.Status.BAD_REQUEST);
    }

    String lastName = json.get("last_name").asText();
    String firstName = json.get("first_name").asText();
    String phoneNumber = json.get("phone_number").asText();
    String email = json.get("email").asText();
    String password = json.get("password").asText();
    String photo = json.get("photo").asText();
    String registerDate = json.get("register_date").asText();
    boolean isHelper = json.get("is_helper").asBoolean();

    UserDTO userRegister = myDomainFactory.getUser();

    userRegister.setLastName(lastName);
    userRegister.setFirstName(firstName);
    userRegister.setPhoneNumber(phoneNumber);
    userRegister.setEmail(email);
    userRegister.setPassword(password);
    userRegister.setPhoto(photo);
    userRegister.setRegisterDate(registerDate);
    userRegister.setIsHelper(isHelper);

    UserDTO userAfterRegister = userUCC.register(userRegister);

    return createToken(userAfterRegister);

  }

  /**
   * Method that create a token for a user.
   *
   * @param userToCreateToken user to create a token with
   * @return the token or null if there is a problem
   */
  public ObjectNode createToken(UserDTO userToCreateToken) {
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userToCreateToken.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", userToCreateToken.getId())
          .put("helper", userToCreateToken.isHelper());
      return publicUser;

    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }
  }

}
