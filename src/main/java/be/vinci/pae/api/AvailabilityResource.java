package be.vinci.pae.api;

import be.vinci.pae.api.filters.AuthorizeHelper;
import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.ucc.availability.AvailabilityUCC;
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
import java.util.ArrayList;
import java.util.List;

/**
 * ObjectResource class.
 */
@Singleton
@Path("/availabilities")
public class AvailabilityResource {

  @Inject
  private AvailabilityUCC availabilityUCC;

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
  public List<AvailabilityDTO> add(List<Date> dates) {
    List<LocalDate> localDates = new ArrayList<>();
    for (Date date : dates) {
      if (date == null || date.toLocalDate().isBefore(LocalDate.now())) {
        throw new WebApplicationException(
            "Les disponibilités à ajouter sont déjà passées ou null.",
            Response.Status.BAD_REQUEST);
      }
      localDates.add(date.toLocalDate());
    }

    return availabilityUCC.add(localDates);
  }
}