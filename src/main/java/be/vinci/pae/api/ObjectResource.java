package be.vinci.pae.api;

import be.vinci.pae.api.filters.AuthorizeAdmin;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.ucc.object.ObjectUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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


/**
 * ObjectResource class.
 */
@Singleton
@Path("/objects")
public class ObjectResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
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
   * Method that update the state of an object.
   *
   * @param objectUCCToUpdate the object to update
   * @param id                the id of the object
   * @return the object that was just updated
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ObjectUCC updateObjectState(ObjectUCC objectUCCToUpdate, @PathParam("id") int id) {
    return null;
  }

  /**
   * Method that update the status of an object (accepted or rejected).
   *
   * @param status the new status to update
   * @param id     the id of the object
   * @return the object that was just updated
   */
  @PATCH
  @Path("status/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ObjectUCC updateObjectStatus(String status, @PathParam("id") int id) {
    if (id <= 0 || status.isBlank() || (!status.equals("rejeté") && !status.equals("accepté"))) {
      throw new WebApplicationException("Mauvais statut (accepté ou refusé) ou id",
          (Response.Status.BAD_REQUEST));
    }

    ObjectDTO objectDTO = objectUCC.setStatus(id, status);

    if (objectDTO == null) {
      throw new WebApplicationException("Objet déjà accepté ou refusé",
          Status.NOT_MODIFIED);
    }

    return null;
  }
}