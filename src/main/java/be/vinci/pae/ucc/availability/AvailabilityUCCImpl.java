package be.vinci.pae.ucc.availability;

import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
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
   * Add new availability.
   *
   * @param availability the availability to add
   * @return the new availability
   */
  public AvailabilityDTO add(AvailabilityDTO availability) {
    myDalServices.startTransaction();
    try {
      if (myAvailabilityDAO.getOneByDate(availability.getDate()) != null) {
        throw new WebApplicationException(
            "Impossible d'insérer la date en db, la date existe déjà",
            Status.METHOD_NOT_ALLOWED);
      }

      AvailabilityDTO addedAvailability = myAvailabilityDAO.insert(availability);

      if (addedAvailability == null) {
        throw new WebApplicationException(
            "Impossible d'insérer la date en db",
            Status.INTERNAL_SERVER_ERROR);
      }

      return addedAvailability;
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      throw new WebApplicationException(
          "Erreur lors de l'ajout de la disponibilité à la db",
          Status.INTERNAL_SERVER_ERROR);
    } finally {
      myDalServices.commitTransaction();
    }
  }
}
