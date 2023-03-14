package be.vinci.pae.domain.availability;

import java.util.Date;

/**
 * AvailabilityImpl class that implements the AvailabilityDTO interface Contains all the attribute
 * of an availability.
 */
public class AvailabilityImpl implements Availability {

  private int id;
  private Date date;

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
  public Date getDate() {
    return date;
  }

  /**
   * set the date of an availability.
   *
   * @param date the description of an availability
   */
  @Override
  public void setDate(Date date) {
    this.date = date;
  }
}
