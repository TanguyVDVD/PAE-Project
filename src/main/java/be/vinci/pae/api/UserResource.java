package be.vinci.pae.api;


import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.AuthorizeAdmin;
import be.vinci.pae.domain.DomainFactory;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
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

  @Inject
  private DomainFactory myDomainFactory;

  /**
   * Get a list of all users.
   *
   * @param query query to filter users
   * @return a list of users
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getUsers(@QueryParam("query") String query) {
    return jsonMapper.valueToTree(userUCC.getUsers(query));
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
   * @param userDTO user to create a token with
   * @return the token or null if there is a problem
   */
  public ObjectNode createToken(UserDTO userDTO) {
    String token;
    try {

      token = JWT.create()
          .withIssuer("auth0")
          .withClaim("user", userDTO.getId())
          .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)))
          .sign(this.jwtAlgorithm);

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
