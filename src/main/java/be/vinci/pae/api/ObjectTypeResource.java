package be.vinci.pae.api;

import be.vinci.pae.ucc.objecttype.ObjectTypeUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


/**
 * ObjectTypeResource class.
 */
@Singleton
@Path("/objectsTypes")
public class ObjectTypeResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private ObjectTypeUCC objectTypeUCC;

  /**
   * Get a list of all object types.
   *
   * @return a list of object types
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getObjectTypes() {
    return jsonMapper.valueToTree(objectTypeUCC.getObjectTypes());
  }
}