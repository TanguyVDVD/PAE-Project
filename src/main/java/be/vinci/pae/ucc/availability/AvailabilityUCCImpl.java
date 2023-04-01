package be.vinci.pae.ucc.availability;

import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
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
   * @param availabilities the availabilities to add
   * @return a list of all the new availabilities
   */
  @Override
  public List<AvailabilityDTO> add(List<AvailabilityDTO> availabilities) {
    List<AvailabilityDTO> addedAvailabilities = new ArrayList<>();
    myDalServices.startTransaction();
    try {

      for (AvailabilityDTO availability : availabilities) {
        AvailabilityDTO addedAvailability = myAvailabilityDAO.insert(availability);

        if (addedAvailability == null) {
          throw new WebApplicationException(
              "Impossible d'insérer la date en db",
              Status.INTERNAL_SERVER_ERROR);
        }

        addedAvailabilities.add(addedAvailability);
      }

      return addedAvailabilities;
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
