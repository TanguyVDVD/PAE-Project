package be.vinci.pae.api;

import be.vinci.pae.api.filters.AuthorizeAdmin;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.ucc.object.ObjectUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


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

}