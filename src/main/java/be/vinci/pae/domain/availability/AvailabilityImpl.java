package be.vinci.pae.domain.availability;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * AvailabilityImpl class that implements the AvailabilityDTO interface Contains all the attribute
 * of an availability.
 */
public class AvailabilityImpl implements Availability {

  private int id;
  private LocalDate date;

  /**
   * Return the id of an availability.
   *
   * @return an int corresponding to the id of an availability
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * set the id of an availability.
   *
   * @param id the id of an availability
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Return the date of an availability.
   *
   * @return a Date corresponding to the date of an availability
   */
  @Override
  public LocalDate getDate() {
    return date;
  }

  /**
   * set the date of an availability.
   *
   * @param date the description of an availability
   */
  @Override
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Check if the date is a saturday.
   *
   * @param date the date to check
   * @return true if the date is a saturday else false
   */
  @Override
  public boolean isSaturday(LocalDate date) {
    return date.getDayOfWeek().equals(DayOfWeek.SATURDAY);
  }
}
