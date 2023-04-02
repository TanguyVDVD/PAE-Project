package be.vinci.pae.api;

import be.vinci.pae.api.filters.AuthorizeHelper;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.ucc.availability.AvailabilityUCC;
import be.vinci.pae.utils.MyObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.DayOfWeek;
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

  @Inject
  private DomainFactory myDomainFactory;

  /**
   * Get a list of all availabilities.
   *
   * @return a list of availabilities
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<AvailabilityDTO> getAvailabilities() {
    return availabilityUCC.getAvailabilities();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeHelper
  public AvailabilityDTO add(JsonNode json) {
    if (json.isNull() || !json.hasNonNull("date")) {
      throw new WebApplicationException(
          "La date à ajouter aux disponibilités est null",
          Response.Status.BAD_REQUEST);
    }

    LocalDate date = jsonMapper.convertValue(json.get("date"), LocalDate.class);

    if (date.isBefore(LocalDate.now()) || !date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
      throw new WebApplicationException(
          "La date à ajouter aux disponibilités n'est pas bonne",
          Response.Status.BAD_REQUEST);
    }

    AvailabilityDTO availability = myDomainFactory.getAvailability();
    availability.setDate(date);

    return availabilityUCC.add(availability);
  }
}