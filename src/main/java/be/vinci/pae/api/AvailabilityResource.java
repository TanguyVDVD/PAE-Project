package be.vinci.pae.api;

import be.vinci.pae.ucc.availability.AvailabilityUCC;
import be.vinci.pae.utils.MyObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * ObjectResource class.
 */
@Singleton
@Path("/availabilities")
public class AvailabilityResource {

  private final ObjectMapper jsonMapper = MyObjectMapper.getJsonMapper();

  @Inject
  private AvailabilityUCC availabilityUCC;

  /**
   * Get a list of all availabilities.
   *
   * @return a list of availabilities
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getAvailabilities() {
    return jsonMapper.valueToTree(availabilityUCC.getAvailabilities());
  }
}