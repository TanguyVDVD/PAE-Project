package be.vinci.pae.ucc.availability;

import be.vinci.pae.domain.availability.Availability;
import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * AvailabilityUCCImpl class that implements the AvailabilityUCC interface.
 */
public class AvailabilityUCCImpl implements AvailabilityUCC {

  @Inject
  private AvailabilityDAO myAvailabilityDAO;

  @Inject
  private DALServices myDalServices;

  /**
   * Returns a list of all availabilities.
   *
   * @return a list of all availabilities
   */
  @Override
  public List<AvailabilityDTO> getAvailabilities() {
    myDalServices.startTransaction();
    try {
      return myAvailabilityDAO.getAll();
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      throw new WebApplicationException(
          "Erreur lors de la récupération de la liste des disponibilités",
          Status.INTERNAL_SERVER_ERROR);
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Add new availabilities.
   *
   * @param dates the availabilities to add
   * @return a list of all the new availabilities
   */
  @Override
  public List<AvailabilityDTO> add(List<LocalDate> dates) {
    List<AvailabilityDTO> availabilities = new ArrayList<>();
    myDalServices.startTransaction();
    try {

      for (LocalDate date : dates) {
        AvailabilityDTO availabilityDTO = myAvailabilityDAO.add(date);
        Availability availabilityTemp = (Availability) availabilityDTO;

        if (!availabilityTemp.isSaturday(date)) {
          throw new WebApplicationException(
              "Date invalide, ce n'est pas un samedi",
              Response.Status.BAD_REQUEST);
        }

        availabilities.add(availabilityDTO);
      }
      return availabilities;
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      throw new WebApplicationException(
          "Erreur lors de l'ajout des disponibilités",
          Status.INTERNAL_SERVER_ERROR);
    } finally {
      myDalServices.commitTransaction();
    }
  }
}
