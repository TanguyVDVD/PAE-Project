package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.AuthorizeHelper;
import be.vinci.pae.api.filters.AuthorizeManager;
import be.vinci.pae.api.filters.GetUser;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.ucc.object.ObjectUCC;
import be.vinci.pae.utils.MyObjectMapper;
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
import java.util.List;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;


/**
 * ObjectResource class.
 */
@Singleton
@Path("/objects")
public class ObjectResource {

  private final ObjectMapper jsonMapper = MyObjectMapper.getJsonMapper();
  @Inject
  private ObjectUCC objectUCC;
  @Inject
  private DomainFactory myDomainFactory;

  /**
   * Get a list of all objects.
   *
   * @param query query to filter objects
   * @return a list of objects
   */
  @GET
  @AuthorizeHelper
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getObjects(@QueryParam("query") String query) {
    return jsonMapper.valueToTree(objectUCC.getObjects(query));
  }

  /**
   * Get a list of all public objects.
   *
   * @param query query to filter objects
   * @return a list of objects
   */
  @GET
  @Path("/public")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDTO> getPublicObjects(@QueryParam("query") String query) {
    List<ObjectDTO> objects = objectUCC.getObjects(query);

    return objects.stream().filter(
        object -> object.getisVisible() && (object.getState().equals("en magasin")
            || object.getState().equals("en vente")
            || object.getState().equals("vendu"))).toList();
  }

  /**
   * Get a list of all objects proposed by a user.
   *
   * @param request the request
   * @param id      the id of the user
   * @return a list of objects
   */
  @GET
  @Authorize
  @Path("/user/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getObjectsByUser(@Context ContainerRequest request, @PathParam("id") int id) {
    User authorizedUser = (User) request.getProperty("user");

    if (authorizedUser.getId() != id && !authorizedUser.getRole().equals("aidant")
        && !authorizedUser.getRole().equals("responsable")) {
      throw new WebApplicationException(
          "Vous n'avez pas le droit de voir les objets de cet utilisateur",
          Status.FORBIDDEN);
    }

    return jsonMapper.valueToTree(objectUCC.getObjectsByUser(id));
  }

  /**
   * Get a list of all offers.
   *
   * @param query query to filter offers
   * @return a list of offers
   */
  @GET
  @Path("/offers")
  @AuthorizeHelper
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getOffers(@QueryParam("query") String query) {
    return jsonMapper.valueToTree(objectUCC.getOffers(query));
  }

  /**
   * Get an object corresponding to the id.
   *
   * @param id the id of the object
   * @return an object
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO getOne(@PathParam("id") int id) {
    if (id <= 0) {
      throw new WebApplicationException("Mauvais id indiqué",
          Response.Status.BAD_REQUEST);
    }
    ObjectDTO objectDTO = objectUCC.getOne(id);
    if (objectDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Impossible de trouver les informations de l'objet").type("text/plain").build());
    }
    return objectDTO;
  }

  /**
   * Method that update the state of an object.
   *
   * @param request the request
   * @param json    the json
   * @param id      the id of the object
   * @return the object that was just updated
   */

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeHelper
  public ObjectNode updateObject(@Context ContainerRequest request,
      JsonNode json,
      @PathParam("id") int id) {

    if (!json.hasNonNull("type") || !json.hasNonNull("description")
        || !json.hasNonNull("state") || !json.hasNonNull("isVisible")) {

      throw new WebApplicationException("Tous les champs ne sont pas remplis",
          Status.UNAUTHORIZED);
    }

    LocalDate changeDate;

    if (!json.hasNonNull("date")) {
      changeDate = LocalDate.now();
    } else {
      try {
        changeDate = LocalDate.parse(json.get("date").asText());
      } catch (Exception e) {
        throw new WebApplicationException("La date n'est pas au bon format",
            Status.BAD_REQUEST);
      }
    }

    String stateObject = json.get("state").asText();

    if (stateObject.equals("en vente") && !json.hasNonNull("price")) {

      throw new WebApplicationException("Un prix doit être entré",
          Status.NOT_FOUND);
    }

    double priceObject = json.get("price").asDouble();

    if (priceObject > 10 || priceObject < 0) {
      throw new WebApplicationException("Le prix doit être compris entre 0€ et 10€",
          Status.NOT_FOUND);
    }

    String typeObject = json.get("type").asText();
    String descriptionObject = json.get("description").asText();
    boolean isVisibleObject = json.get("isVisible").asBoolean();
    int versionNumber = json.get("versionNbr").asInt();

    ObjectDTO objectUpdated = myDomainFactory.getObject();

    objectUpdated.setObjectType(typeObject);
    objectUpdated.setDescription(descriptionObject);
    objectUpdated.setState(stateObject);
    objectUpdated.setIsVisible(isVisibleObject);
    objectUpdated.setPrice(priceObject);
    objectUpdated.setVersionNumber(versionNumber);

    UserDTO user = (UserDTO) request.getProperty("user");

    ObjectDTO objectDTOAfterUpdate = objectUCC.update(id, objectUpdated, changeDate, user);

    return jsonMapper.convertValue(objectDTOAfterUpdate, ObjectNode.class);
  }

  /**
   * Method that update the status of an object (accepted or rejected).
   *
   * @param json the new status to update as a json
   * @param id   the id of the object
   * @return the object that was just updated
   */
  @PATCH
  @Path("/status/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeManager
  public ObjectNode updateObjectStatus(JsonNode json, @PathParam("id") int id) {
    String status = json.get("status").asText();
    if (id <= 0 || status.isBlank() || !status.equals("refusé") && !status.equals("accepté")) {
      throw new WebApplicationException("Mauvais statut (accepté ou refusé) ou id",
          Response.Status.BAD_REQUEST);
    }

    ObjectDTO objectDTO;
    int versionNumber = 0;
    if (json.get("versionNbr") != null) {
      versionNumber = json.get("versionNbr").asInt();
    }

    if (status.equals("accepté")) {
      objectDTO = objectUCC.accept(id, versionNumber);
    } else {
      String reasonForRefusal = "";
      if (json.get("reasonForRefusal") != null) {
        reasonForRefusal = json.get("reasonForRefusal").asText("");
      }

      objectDTO = objectUCC.refuse(id, reasonForRefusal, versionNumber);
    }

    if (objectDTO == null) {
      throw new WebApplicationException("Impossible de changer le statut de l'objet en db",
          Status.NOT_MODIFIED);
    }

    return jsonMapper.convertValue(objectDTO, ObjectNode.class);
  }

  /**
   * Get an object's photo.
   *
   * @param id the object's id
   * @return the object's photo
   */
  @GET
  @Path("/{id}/photo")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response getPhoto(@PathParam("id") int id) {
    ObjectDTO object = objectUCC.getOne(id);

    File f = objectUCC.getPhoto(object);

    if (f == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(f, MediaType.APPLICATION_OCTET_STREAM)
        .header("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"").build();
  }

  /**
   * Update an object's photo.
   *
   * @param id          the object's id
   * @param photo       the photo of the user
   * @param photoDetail the detail of the photo
   * @return the object's information
   */
  @PUT
  @Path("/{id}/photo")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeHelper
  public ObjectDTO updatePhoto(@PathParam("id") int id, @FormDataParam("photo") InputStream photo,
      @FormDataParam("photo") FormDataContentDisposition photoDetail) {
    if (photoDetail == null || photoDetail.getFileName() == null) {
      throw new WebApplicationException("Paramètres manquants", Response.Status.BAD_REQUEST);
    }

    ObjectDTO objectDTO = myDomainFactory.getObject();

    objectDTO.setId(id);

    ObjectDTO objectAfterUpdate = objectUCC.updatePhoto(objectDTO, photo);

    if (objectAfterUpdate == null) {
      throw new WebApplicationException("Objet non trouvé", Status.NOT_FOUND);
    }

    return objectAfterUpdate;
  }

  /**
   * Offer an object.
   *
   * @param request     the request
   * @param description the description of the object
   * @param objectType  the object type
   * @param receiptDate the day to receipt the object
   * @param timeSlot    the time slot chosen
   * @param phoneNumber the phone number used to offer the object
   * @param photo       the photo of the user
   * @return an object when added
   */
  @GetUser
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @POST
  public ObjectDTO add(
      @Context ContainerRequest request,
      @FormDataParam("phoneNumber") String phoneNumber,
      @FormDataParam("description") String description,
      @FormDataParam("objectType") String objectType,
      @FormDataParam("receiptDate") String receiptDate,
      @FormDataParam("timeSlot") String timeSlot,
      @FormDataParam("photo") InputStream photo
  ) {
    UserDTO authorizedUser = (UserDTO) request.getProperty("user");

    // Check the user
    if (authorizedUser == null) {
      if (phoneNumber == null || phoneNumber.isBlank()) {
        throw new WebApplicationException(
            "Numéro de téléphonne manquant ou utilisateur non connecté",
            Response.Status.BAD_REQUEST);
      } else {
        // Check phone number format
        phoneNumber = User.formatPhoneNumber(phoneNumber);
        if (phoneNumber == null) {
          throw new WebApplicationException("Numéro de téléphone invalide",
              Response.Status.BAD_REQUEST);
        }
      }
    } else if (phoneNumber != null && !phoneNumber.isBlank()) {
      throw new WebApplicationException(
          "Impossible de proser un objet anonymement pour un utilisateur connecté",
          Response.Status.BAD_REQUEST);
    }

    if (description == null || description.isBlank()) {
      throw new WebApplicationException("Description manquante", Response.Status.BAD_REQUEST);
    }

    if (objectType == null || objectType.isBlank()) {
      throw new WebApplicationException("Type de l'objet manquant", Response.Status.BAD_REQUEST);
    }

    if (receiptDate == null || LocalDate.parse(receiptDate).isBefore(LocalDate.now())) {
      throw new WebApplicationException("Date invalide", Response.Status.BAD_REQUEST);
    }

    if (timeSlot == null || !timeSlot.equals("matin") && !timeSlot.equals("après-midi")) {
      throw new WebApplicationException("Créneau invalide", Response.Status.BAD_REQUEST);
    }

    if (photo == null) {
      throw new WebApplicationException("Photo manquante", Status.BAD_REQUEST);
    }

    if (description.length() > 120) {
      throw new WebApplicationException("Description trop longue", Status.BAD_REQUEST);
    }

    ObjectDTO objectDTO = myDomainFactory.getObject();

    objectDTO.setDescription(description);
    objectDTO.setObjectType(objectType);
    objectDTO.setReceiptDate(LocalDate.parse(receiptDate));
    objectDTO.setTimeSlot(timeSlot);
    objectDTO.setPhoneNumber(phoneNumber);
    objectDTO.setUser(authorizedUser);
    objectDTO.setOfferDate(LocalDate.now());

    ObjectDTO objectAdded = objectUCC.add(objectDTO);

    objectUCC.updatePhoto(objectAdded, photo);

    return objectAdded;
  }
}