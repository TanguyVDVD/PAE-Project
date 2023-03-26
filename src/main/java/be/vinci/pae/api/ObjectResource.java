package be.vinci.pae.api;

import be.vinci.pae.api.filters.AuthorizeAdmin;
import be.vinci.pae.api.filters.AuthorizeRiez;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.ucc.object.ObjectUCC;
import be.vinci.pae.utils.MyLogger;
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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDate;
import java.util.logging.Level;

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
  @AuthorizeAdmin
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getObjects(@QueryParam("query") String query) {
    return jsonMapper.valueToTree(objectUCC.getObjects(query));
  }

  /**
   * Get a list of all offers.
   *
   * @param query query to filter offers
   * @return a list of offers
   */
  @GET
  @Path("/offers")
  @AuthorizeAdmin
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getoffers(@QueryParam("query") String query) {
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
      MyLogger.log(Level.INFO, "Mauvais id indiqué");
      throw new WebApplicationException("Mauvais id indiqué",
          Response.Status.BAD_REQUEST);
    }
    ObjectDTO objectDTO = objectUCC.getOne(id);
    if (objectDTO == null) {
      MyLogger.log(Level.INFO, "Impossible de trouver les informations de l'objet");
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Impossible de trouver les informations de l'objet").type("text/plain").build());
    }
    return objectDTO;
  }

  /**
   * Method that update the state of an object.
   *
   * @param json the json
   * @param id   the id of the object
   * @return the object that was just updated
   */

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ObjectNode updateObject(JsonNode json, @PathParam("id") int id) {
    if (!json.hasNonNull("type") || !json.hasNonNull("description") || !json.hasNonNull(
        "state") || !json.hasNonNull("is_visible")) {
      MyLogger.log(Level.INFO, "Tous les champs ne sont pas remplis");

      throw new WebApplicationException("Tous les champs ne sont pas remplis",
          Status.BAD_REQUEST);
    }

    LocalDate changeDate;

    if (!json.hasNonNull("date")) {
      changeDate = LocalDate.now();
    } else {
      try {
        changeDate = LocalDate.parse(json.get("date").asText());
      } catch (Exception e) {
        MyLogger.log(Level.INFO, "La date n'est pas au bon format");
        throw new WebApplicationException("La date n'est pas au bon format",
            Status.BAD_REQUEST);
      }
    }

    String stateObject = json.get("state").asText();

    if (stateObject.equals("mis en vente") && !json.hasNonNull("price")) {
      MyLogger.log(Level.INFO, "Un prix doit être entré");
      throw new WebApplicationException("Un prix doit être entré",
          Status.NOT_FOUND);
    }

    int priceObject = json.get("price").asInt();

    if (priceObject > 10 || priceObject < 0) {
      throw new WebApplicationException("Le prix doit être compris entre 0€ et 10€",
          Status.NOT_FOUND);
    }

    String typeObject = json.get("type").asText();
    String descriptionObject = json.get("description").asText();
    boolean isVisibleObject = json.get("isVisible").asBoolean();

    ObjectDTO objectUpdated = myDomainFactory.getObject();

    objectUpdated.setObjectType(typeObject);
    objectUpdated.setDescription(descriptionObject);
    objectUpdated.setState(stateObject);
    objectUpdated.setIsVisible(isVisibleObject);
    objectUpdated.setPrice(priceObject);

    ObjectDTO objectDTOAfterUpdate = objectUCC.update(id, objectUpdated, changeDate);

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
  @AuthorizeRiez
  public ObjectNode updateObjectStatus(JsonNode json, @PathParam("id") int id) {
    String status = json.get("status").asText();
    if (id <= 0 || status.isBlank() || !status.equals("refusé") && !status.equals("accepté")) {
      MyLogger.log(Level.INFO, "Mauvais statut (accepté ou refusé) ou id");
      throw new WebApplicationException("Mauvais statut (accepté ou refusé) ou id",
          Response.Status.BAD_REQUEST);
    }

    ObjectDTO objectDTO;

    if (status.equals("accepté")) {
      objectDTO = objectUCC.accept(id);
    } else {
      String reasonForRefusal = "";
      if (json.get("reasonForRefusal") != null) {
        reasonForRefusal = json.get("reasonForRefusal").asText("");
      }
      objectDTO = objectUCC.refuse(id, reasonForRefusal);
    }

    if (objectDTO == null) {
      MyLogger.log(Level.INFO, "Impossible de changer le statut de l'objet en db");
      throw new WebApplicationException("Impossible de changer le statut de l'objet en db",
          Status.NOT_MODIFIED);
    }

    return jsonMapper.convertValue(objectDTO, ObjectNode.class);
  }
}