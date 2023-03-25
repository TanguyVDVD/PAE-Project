package be.vinci.pae.services.availability;

import java.time.LocalDate;

/**
 * AvailabilityDAO interface that provide the method to interact with the db.
 */
public interface AvailabilityDAO {

  /**
   * Get the availability by the id.
   *
   * @param id of the availability
   * @return the date corresponding to the id
   */
  LocalDate getOneById(int id);
}
