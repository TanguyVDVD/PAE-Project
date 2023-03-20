package be.vinci.pae.ucc.object;

import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.services.object.ObjectDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * UserUCCImpl class that implements the ObjectUCC interface.
 */
public class ObjectUCCImpl implements ObjectUCC {

  @Inject
  private ObjectDAO myObjectDAO;

  /**
   * Returns a list of all objects.
   *
   * @param query query to filter objects
   * @return a list of all objects
   */
  @Override
  public List<ObjectDTO> getObjects(String query) {
    return myObjectDAO.getAll(query);
  }

  /**
   * Accept an offer.
   *
   * @param id     the id of the object to accept
   * @param status the new status of the object (accepted or rejected)
   * @return the object updated
   */
  @Override
  public ObjectDTO setStatus(int id, String status) {
    return myObjectDAO.setStatus(id, status);
  }


}
