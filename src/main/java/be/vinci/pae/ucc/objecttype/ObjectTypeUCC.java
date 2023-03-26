package be.vinci.pae.ucc.objecttype;

import be.vinci.pae.domain.objecttype.ObjectTypeDTO;
import java.util.List;

/**
 * ObjectTypeUCC interface that provide the methods of an object type.
 */
public interface ObjectTypeUCC {

  /**
   * Returns a list of all object types.
   *
   * @return a list of all object types
   */
  List<ObjectTypeDTO> getObjectTypes();
}
