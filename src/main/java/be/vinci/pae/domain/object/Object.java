package be.vinci.pae.domain.object;

import java.sql.Date;

/**
 * Object interface representing an object in the domain.
 */
public interface Object extends ObjectDTO {

  /**
   * Give the current date
   *
   * @return today's date (sql)
   */
  Date getCurrentDate();

  /**
   * Check if the object is already accepted or rejected.
   *
   * @param id the id of the object
   * @return true if the status of the object is already defined, else false
   */
  boolean isStatusAlreadyDefined(int id);

}
