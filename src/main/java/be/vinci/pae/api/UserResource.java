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
import java.util.Date;
import org.apache.commons.validator.routines.EmailValidator;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
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
      throw new WebApplicationException("Adresse mail et mot de passe requis", Status.NOT_FOUND);
    }

    String login = json.get("email").asText();
    String password = json.get("password").asText();

    UserDTO userDTO = userUCC.login(login, password);

    if (userDTO == null) {
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
        throw new WebApplicationException("Paramètres manquants", Response.Status.BAD_REQUEST);
      }
    }

    // Check email format
    EmailValidator emailValidator = EmailValidator.getInstance();
    if (!emailValidator.isValid(email)) {
      throw new WebApplicationException("Adresse mail invalide", Response.Status.BAD_REQUEST);
    }

    // Check phone number format
    phone = phone.replaceAll("[^0-9]", "");
    if (!phone.startsWith("0")) {
      phone = "0" + phone;
    }

    if (!phone.matches("^0[0-9]{7,9}$")) {
      throw new WebApplicationException("Numéro de téléphone invalide",
          Response.Status.BAD_REQUEST);
    }

    UserDTO userRegister = myDomainFactory.getUser();

    userRegister.setLastName(lastName);
    userRegister.setFirstName(firstName);
    userRegister.setEmail(email);
    userRegister.setPhoneNumber(phone);
    userRegister.setPassword(password);
    userRegister.setPhoto(photoDetail != null && photoDetail.getFileName() != null);
    userRegister.setRegisterDate(new java.sql.Date(System.currentTimeMillis()).toString());

    UserDTO userAfterRegister = userUCC.register(userRegister);

    if (userAfterRegister.getPhoto()) {
      userUCC.updateProfilePicture(userAfterRegister.getId(), photo);
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
    File f = userUCC.getProfilePicture(id);

    if (f == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(f, MediaType.APPLICATION_OCTET_STREAM)
        .header("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"").build();
  }
}
