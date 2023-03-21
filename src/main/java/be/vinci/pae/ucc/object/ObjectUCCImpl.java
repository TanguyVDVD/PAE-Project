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
   * @param id the id of the object to accept
   * @return the object updated
   */
  @Override
  public ObjectDTO accept(int id) {
    return myObjectDAO.setStatusToAccepted(id);
  }

  /**
   * Reject an offer.
   *
   * @param id               the id of the object to reject
   * @param reasonForRefusal the reason for refusal
   * @return the object updated
   */
  @Override
  public ObjectDTO reject(int id, String reasonForRefusal) {
    return myObjectDAO.setStatusToRejected(id, reasonForRefusal);
  }
}
