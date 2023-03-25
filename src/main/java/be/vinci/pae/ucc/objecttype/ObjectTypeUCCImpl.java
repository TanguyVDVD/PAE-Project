package be.vinci.pae.ucc.objecttype;

import be.vinci.pae.services.objecttype.ObjectTypeDAO;
import jakarta.inject.Inject;

/**
 * ObjectTypeUCCImpl class that implements the ObjectTypeUCC interface.
 */
public class ObjectTypeUCCImpl implements ObjectTypeUCC {

  @Inject
  private ObjectTypeDAO myObjectTypeDAO;

  /**
   * Returns a list of all object types.
   *
   * @return a list of all object types
   */
  @Override
  public Object getObjectTypes() {
    return myObjectTypeDAO.getAll();
  }
}
