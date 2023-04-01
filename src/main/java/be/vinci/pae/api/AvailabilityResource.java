package be.vinci.pae.api;

import be.vinci.pae.api.filters.AuthorizeHelper;
import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.ucc.availability.AvailabilityUCC;
import be.vinci.pae.utils.MyObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeHelper
  public List<AvailabilityDTO> add(List<Date> dates) {

    for (Date date : dates) {
      if (date == null || date.compareTo(Date.valueOf(LocalDate.now())) >= 0) {
        throw new WebApplicationException(
            "Les disponibilités à ajouter sont déjà passées ou null.",
            Response.Status.BAD_REQUEST);
      }
    }

    return availabilityUCC.add(dates);
  }
}