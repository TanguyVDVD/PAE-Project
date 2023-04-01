package be.vinci.pae.domain.availability;

import java.time.LocalDate;

/**
 * Availability interface representing an availability in the domain.
 */
public interface Availability extends AvailabilityDTO {

  /**
   * Check if the date is a saturday.
   *
   * @param date the date to check
   * @return true if the date is a saturday else false
   */
  boolean isSaturday(LocalDate date);
}
