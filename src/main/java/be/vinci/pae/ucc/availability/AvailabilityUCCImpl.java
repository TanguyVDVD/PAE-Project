package be.vinci.pae.ucc.availability;

import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import be.vinci.pae.services.object.ObjectDAO;
import be.vinci.pae.utils.exceptions.UserException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * AvailabilityUCCImpl class that implements the AvailabilityUCC interface.
 */
public class AvailabilityUCCImpl implements AvailabilityUCC {

  @Inject
  private AvailabilityDAO myAvailabilityDAO;

  @Inject
  private ObjectDAO myObjectDAO;

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

      throw e;
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
  public AvailabilityDTO addOne(AvailabilityDTO availability) {
    myDalServices.startTransaction();
    try {
      if (myAvailabilityDAO.getOneByDate(availability.getDate()) != null) {
        throw new UserException(
            "Impossible d'insérer la date, la date existe déjà");
      }

      return myAvailabilityDAO.addOne(availability);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * delete an availability.
   *
   * @param id the availability id
   * @return the deleted availability
   */
  @Override
  public AvailabilityDTO deleteOne(int id) {
    myDalServices.startTransaction();
    try {
      if (myAvailabilityDAO.getOneById(id) == null) {
        throw new UserException(
            "Impossible de supprimer la disponibilité, la disponibilité n'existe pas");
      }

      // Vérifie que la disponibilité à supprimer n'est pas encore liée à un objet
      if (!myObjectDAO.getAllByAvailability(id).isEmpty()) {
        throw new UserException(
            "Impossible de supprimer la disponibilité, "
                + "la disponibilité est déjà associée à un ou plusieurs objets");
      }

      return myAvailabilityDAO.deleteOne(id);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }
}
