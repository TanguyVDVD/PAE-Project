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
   * @return the availability corresponding to the id
   */
  AvailabilityDTO getOneById(int id);

  /**
   * Get the availability by the date.
   *
   * @param date of the availability
   * @return the availability corresponding to the id
   */
  AvailabilityDTO getOneByDate(LocalDate date);

  /**
   * Insert a new availability in the db.
   *
   * @param availability the date to insert in the db
   * @return the new availability added if succeeded
   */
  AvailabilityDTO addOne(AvailabilityDTO availability);

  /**
   * Delete an availability in the db.
   *
   * @param id the id of the availability to delete in the db
   * @return the availability deleted if succeeded
   */
  AvailabilityDTO deleteOne(int id);
}
