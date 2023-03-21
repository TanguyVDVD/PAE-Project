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
   * @param id the id of the object to reject
   * @return the object updated
   */
  @Override
  public ObjectDTO reject(int id) {
    return null;
  }

  /**
   * Updates the status of the object with the specified ID to dropped In The Workshop with the
   * date.
   *
   * @param id   the ID of the object whose status should be updated
   * @param date the date on which the object was dropped in the workshop
   * @return an ObjectDTO object containing the updated information of the object
   */
  @Override
  public ObjectDTO setStatuDroppedInTheWorkhop(int id, String date) {
    return null;
  }

  /**
   * Updates the status of the object with the specified ID to dropped In The Shop with the date.
   *
   * @param id   the ID of the object whose status should be updated
   * @param date the date on which the object was dropped in the shop
   * @return an ObjectDTO object containing the updated information of the object
   */
  @Override
  public ObjectDTO setStatuDroppedInTheShop(int id, String date) {
    return null;
  }

  /**
   * Updates the status of the object with the specified ID to For Sale with the date.
   *
   * @param id   the ID of the object whose status should be updated
   * @param date the date on which the object was put up for sale
   * @return an ObjectDTO object containing the updated information of the object
   */
  @Override
  public ObjectDTO setStatuForSale(int id, String date) {
    return null;
  }

  /**
   * Updates the status of the object with the specified ID to Sold with the date.
   *
   * @param id   the ID of the object whose status should be updated
   * @param date the date on which the object was sold
   * @return an ObjectDTO object containing the updated information of the object
   */
  @Override
  public ObjectDTO setStatuSold(int id, String date) {
    return myObjectDAO.setStateToSold(id, date);
  }
}
