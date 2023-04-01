package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.AuthorizeHelper;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.ucc.user.UserUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.MyLogger;
import be.vinci.pae.utils.MyObjectMapper;
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
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;
import org.junit.platform.commons.util.StringUtils;

/**
 * UserResource class.
 */
@Singleton
@Path("/users")
public class UserResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = MyObjectMapper.getJsonMapper();
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
  @AuthorizeHelper
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
      MyLogger.log(Level.INFO, "Adresse mail et mot de passe requis");
      throw new WebApplicationException("Adresse mail et mot de passe requis", Status.UNAUTHORIZED);
    }

    String login = json.get("email").asText();
    String password = json.get("password").asText();

    UserDTO userDTO = userUCC.login(login, password);

    if (userDTO == null) {
      MyLogger.log(Level.INFO, "Adresse mail ou mot de passe incorrect");
      throw new WebApplicationException("Adresse mail ou mot de passe incorrect", Status.NOT_FOUND);
    }

    return createToken(userDTO);
  }

  /**
   * Register a user with json object return the user created and the token.
   *
   * @param lastName    the last name of the user
   * @param firstName   the first name of the user
   * @param email       the email of the user
   * @param phone       the phone number of the user
   * @param password    the password of the user
   * @param photo       the photo of the user
   * @param photoDetail the detail of the photo
   * @return a user when is created
   */
  @Path("register")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @POST
  public ObjectNode register(
      @FormDataParam("lastname") String lastName,
      @FormDataParam("firstname") String firstName, @FormDataParam("email") String email,
      @FormDataParam("phone") String phone, @FormDataParam("password") String password,
      @FormDataParam("photo") InputStream photo,
      @FormDataParam("photo") FormDataContentDisposition photoDetail
  ) {
    String[] inputs = {lastName, firstName, email, phone, password};

    for (String input : inputs) {
      if (input == null || input.isBlank()) {
        MyLogger.log(Level.INFO, "Paramètres manquants");
        throw new WebApplicationException("Paramètres manquants", Response.Status.UNAUTHORIZED);
      }
    }

    UserDTO userRegister = myDomainFactory.getUser();

    userRegister.setLastName(lastName);
    userRegister.setFirstName(firstName);
    userRegister.setEmail(email);
    userRegister.setPhoneNumber(phone);
    userRegister.setPassword(password);
    userRegister.setPhoto(photoDetail != null && photoDetail.getFileName() != null);
    userRegister.setRegisterDate(LocalDate.now());
    userRegister.setRole(false);

    UserDTO userAfterRegister = userUCC.register(userRegister);

    if (userAfterRegister.getPhoto()) {
      userUCC.updateProfilePicture(userAfterRegister, photo);
    }

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
      token = JWT.create().withIssuer("auth0").withClaim("user", userDTO.getId())
          .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)))
          .sign(this.jwtAlgorithm);

      return jsonMapper.convertValue(userDTO, ObjectNode.class).put("token", token);

    } catch (Exception e) {
      MyLogger.log(Level.INFO, "Erreur création token");
      System.out.println(e.getMessage());
      System.out.println("Unable to create token");
      return null;
    }
  }

  /**
   * Retrieve a user's information.
   *
   * @param id the user's id
   * @return the user's information
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeHelper
  public UserDTO getUserInfo(@PathParam("id") int id) {
    return userUCC.getUserById(id);
  }

  /**
   * Update a user's information.
   *
   * @param request the request
   * @param id      the user's id
   * @param data    information to update
   * @return the user's information
   */
  @PATCH
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDTO updateUserInfo(@Context ContainerRequest request, @PathParam("id") int id,
      JsonNode data) {
    UserDTO authorizedUser = (UserDTO) request.getProperty("user");

    // Will only be verified if not null
    String passwordToVerify = null;

    // Create a new DTO to only keep changes
    UserDTO userDTO = myDomainFactory.getUser();
    userDTO.setId(id);

    UserDTO dataDTO = jsonMapper.convertValue(data, UserDTO.class);

    if (authorizedUser.getId() == id) {
      // Only the user themselves can change their own information

      // Verify current password
      if (data.hasNonNull("currentPassword")) {
        passwordToVerify = data.get("currentPassword").asText();
      }

      if (StringUtils.isBlank(passwordToVerify)) {
        MyLogger.log(Level.INFO, "Mot de passe actuel requis");
        throw new WebApplicationException("Mot de passe actuel requis", Status.BAD_REQUEST);
      }

      if (StringUtils.isNotBlank(dataDTO.getFirstName())) {
        userDTO.setFirstName(dataDTO.getFirstName());
      }

      if (StringUtils.isNotBlank(dataDTO.getLastName())) {
        userDTO.setLastName(dataDTO.getLastName());
      }

      if (StringUtils.isNotBlank(dataDTO.getEmail())) {
        userDTO.setEmail(dataDTO.getEmail());
      }

      if (StringUtils.isNotBlank(dataDTO.getPhoneNumber())) {
        userDTO.setPhoneNumber(dataDTO.getPhoneNumber());
      }

      if (StringUtils.isNotBlank(dataDTO.getPassword())) {
        userDTO.setPassword(dataDTO.getPassword());
      }
    } else if (authorizedUser.getId() == 1) {
      // Only the admin can change the helper status
      if (dataDTO.getRole() != null) {
        userDTO.setRole(dataDTO.getRole());
      }
    } else {
      MyLogger.log(Level.INFO, "Vous n'avez pas les droits pour modifier cet utilisateur");
      // The user is not the admin and is not the user themselves
      throw new WebApplicationException("Vous n'avez pas les droits pour modifier cet utilisateur",
          Status.UNAUTHORIZED);
    }

    UserDTO userAfterUpdate = userUCC.updateUser(userDTO, passwordToVerify);

    if (userAfterUpdate == null) {
      MyLogger.log(Level.INFO, "Utilisateur non trouvé");
      throw new WebApplicationException("Utilisateur non trouvé", Status.NOT_FOUND);
    }

    return userAfterUpdate;
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
  public ObjectNode getOwnInfo(@Context ContainerRequest request) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");

    return jsonMapper.convertValue(userDTO, ObjectNode.class);
  }

  /**
   * Get a user's profile picture.
   *
   * @param id the user's id
   * @return the user's profile picture
   */
  @GET
  @Path("/{id}/photo")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response getProfilePicture(@PathParam("id") int id) {
    UserDTO user = myDomainFactory.getUser();
    user.setId(id);

    File f = userUCC.getProfilePicture(user);

    if (f == null) {
      MyLogger.log(Level.INFO, "Photo non trouvée");
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(f, MediaType.APPLICATION_OCTET_STREAM)
        .header("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"").build();
  }

  /**
   * Update a user's profile picture.
   *
   * @param request     the request
   * @param id          the user's id
   * @param password    the user's password
   * @param photo       the photo of the user
   * @param photoDetail the detail of the photo
   * @return the user's information
   */
  @PUT
  @Path("/{id}/photo")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDTO updateProfilePicture(@Context ContainerRequest request,
      @PathParam("id") int id,
      @FormDataParam("password") String password,
      @FormDataParam("photo") InputStream photo,
      @FormDataParam("photo") FormDataContentDisposition photoDetail) {
    UserDTO authorizedUser = (UserDTO) request.getProperty("user");

    if (authorizedUser.getId() != id) {
      MyLogger.log(Level.INFO, "Vous n'avez pas les droits pour modifier cet utilisateur");
      throw new WebApplicationException("Vous n'avez pas les droits pour modifier cet utilisateur",
          Status.UNAUTHORIZED);
    }

    if (photoDetail == null || photoDetail.getFileName() == null) {
      MyLogger.log(Level.INFO, "Paramètres manquants");
      throw new WebApplicationException("Paramètres manquants", Response.Status.BAD_REQUEST);
    }

    UserDTO userDTO = myDomainFactory.getUser();

    userDTO.setId(id);
    userDTO.setPhoto(true);

    UserDTO userAfterUpdate = userUCC.updateProfilePicture(userDTO, photo);

    if (userAfterUpdate == null) {
      MyLogger.log(Level.INFO, "Utilisateur non trouvé");
      throw new WebApplicationException("Utilisateur non trouvé", Status.NOT_FOUND);
    }

    return userAfterUpdate;
  }
}
