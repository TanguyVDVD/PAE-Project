package be.vinci.pae.ucc.objecttype;

import be.vinci.pae.domain.objecttype.ObjectTypeDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.objecttype.ObjectTypeDAO;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.logging.Level;

/**
 * ObjectTypeUCCImpl class that implements the ObjectTypeUCC interface.
 */
public class ObjectTypeUCCImpl implements ObjectTypeUCC {

  @Inject
  private ObjectTypeDAO myObjectTypeDAO;

  @Inject
  private DALServices myDalServices;

  /**
   * Returns a list of all object types.
   *
   * @return a list of all object types
   */
  @Override
  public List<ObjectTypeDTO> getObjectTypes() {
    myDalServices.startTransaction();
    try {
      return myObjectTypeDAO.getAll();
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      MyLogger.log(Level.INFO, "Error getting list all object types");
      throw new WebApplicationException("Error getting list all object types", Status.BAD_REQUEST);
    } finally {
      myDalServices.commitTransaction();
    }
  }
}
