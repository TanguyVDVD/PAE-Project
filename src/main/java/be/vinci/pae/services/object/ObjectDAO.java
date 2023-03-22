package be.vinci.pae.services.object;

import be.vinci.pae.domain.object.ObjectDTO;
import java.sql.Date;
import java.util.List;

/**
 * ObjectDAO interface that provide the method to interact with the db.
 */
public interface ObjectDAO {

  /**
   * Get all objects.
   *
   * @param query query to filter objects
   * @return the list of objects
   */
  List<ObjectDTO> getAll(String query);

  /**
   * Get the object by the id.
   *
   * @param id the id of the object
   * @return the object corresponding to the id
   */
  ObjectDTO getOneById(int id);

  /**
   * Set the status of an object to accepted.
   *
   * @param id the id of the object
   * @return the modified object
   */
  ObjectDTO setStatusToAccepted(int id);

  /**
   * Set the status of an object to refused.
   *
   * @param id               the id of the object
   * @param reasonForRefusal the reason for refusal
   * @param refusalDate      the refusal date
   * @return the modified object
   */
  ObjectDTO setStatusToRefused(int id, String reasonForRefusal, Date refusalDate);
}
