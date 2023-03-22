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

}
