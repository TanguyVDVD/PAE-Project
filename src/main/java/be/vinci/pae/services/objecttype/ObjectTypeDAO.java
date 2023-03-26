package be.vinci.pae.services.objecttype;

import be.vinci.pae.domain.objecttype.ObjectTypeDTO;
import java.util.List;

/**
 * ObjectTypeDAO interface that provide the method to interact with the db.
 */
public interface ObjectTypeDAO {

  /**
   * Get all object types.
   *
   * @return the list of object types
   */
  List<ObjectTypeDTO> getAll();

  /**
   * Get the object type by the id.
   *
   * @param id of the object type
   * @return the object type String corresponding to the id
   */
  String getOneById(int id);

  /**
   * Get the object type id by the label.
   *
   * @param label the label of the object type
   * @return the id corresponding to the label
   */
  int getIdByLabel(String label);
}
