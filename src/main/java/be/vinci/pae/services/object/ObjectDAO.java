package be.vinci.pae.services.object;

import be.vinci.pae.domain.object.ObjectDTO;
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
}
