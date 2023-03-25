package be.vinci.pae.domain.availability;

import java.time.LocalDate;

/**
 * AvailabilityDTO interface. Representing a data transfer object (DTO) for an availability in the
 * domain The interface only contains getter and setter.
 */
public interface AvailabilityDTO {

  /**
   * Return the id of an availability.
   *
   * @return an int corresponding to the id of an availability
   */
  int getId();

  /**
   * set the id of an availability.
   *
   * @param id the id of an availability
   */
  void setId(int id);

  /**
   * Return the date of an availability.
   *
   * @return a LocalDate corresponding to the date of an availability
   */
  LocalDate getDate();

  /**
   * set the date of an availability.
   *
   * @param date the description of an availability
   */
  void setDate(LocalDate date);

}
