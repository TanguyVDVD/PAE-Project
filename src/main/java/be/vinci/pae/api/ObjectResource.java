package be.vinci.pae.api;

import be.vinci.pae.api.filters.AuthorizeAdmin;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.ucc.object.ObjectUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


/**
 * ObjectResource class.
 */
@Singleton
@Path("/objects")
public class ObjectResource {

  @Inject
  private ObjectUCC objectUCC;

  @Inject
  private DomainFactory myDomainFactory;

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