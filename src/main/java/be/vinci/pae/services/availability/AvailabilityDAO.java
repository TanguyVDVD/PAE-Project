package be.vinci.pae.services.availability;

import be.vinci.pae.domain.availability.AvailabilityDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * AvailabilityDAO interface that provide the method to interact with the db.
 */
public interface AvailabilityDAO {

  /**
   * Get all availabilities.
   *
   * @return the list of availabilities
   */
  List<AvailabilityDTO> getAll();

  /**
   * Get the availability by the id.
   *
   * @param id of the availability
   * @return the date corresponding to the id
   */
  LocalDate getOneById(int id);

  /**
   * Insert a new availability in the db.
   *
   * @param availability the date to insert in the db
   * @return the new availability added if succeeded, null if not
   */
  AvailabilityDTO insert(AvailabilityDTO availability);
}
