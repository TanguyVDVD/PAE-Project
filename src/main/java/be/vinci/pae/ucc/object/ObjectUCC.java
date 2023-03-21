package be.vinci.pae.ucc.object;

import be.vinci.pae.domain.object.ObjectDTO;
import java.util.List;

/**
 * ObjectUCC interface that provide the methods of an object.
 */
public interface ObjectUCC {

  /**
   * Returns a list of all objects.
   *
   * @param query query to filter objects
   * @return a list of all objects
   */
  List<ObjectDTO> getObjects(String query);

  /**
   * Accept an offer.
   *
   * @param id the id of the object to accept
   * @return the object updated
   */
  ObjectDTO accept(int id);

  /**
   * Reject an offer.
   *
   * @param id the id of the object to reject
   * @return the object updated
   */
  ObjectDTO reject(int id);

  /**
   * Updates the status of the object with the specified ID to dropped In The Workshop with the
   * date.
   *
   * @param id   the ID of the object whose status should be updated
   * @param date the date on which the object was dropped in the workshop
   * @return an ObjectDTO object containing the updated information of the object
   */
  ObjectDTO setStatuDroppedInTheWorkhop(int id, String date);

  /**
   * Updates the status of the object with the specified ID to dropped In The Shop with the date.
   *
   * @param id   the ID of the object whose status should be updated
   * @param date the date on which the object was dropped in the shop
   * @return an ObjectDTO object containing the updated information of the object
   */
  ObjectDTO setStatuDroppedInTheShop(int id, String date);

  /**
   * Updates the status of the object with the specified ID to For Sale with the date.
   *
   * @param id   the ID of the object whose status should be updated
   * @param date the date on which the object was put up for sale
   * @return an ObjectDTO object containing the updated information of the object
   */
  ObjectDTO setStatuForSale(int id, String date);

  /**
   * Updates the status of the object with the specified ID to Sold with the date.
   *
   * @param id   the ID of the object whose status should be updated
   * @param date the date on which the object was sold
   * @return an ObjectDTO object containing the updated information of the object
   */
  ObjectDTO setStatuSold(int id, String date);
}
