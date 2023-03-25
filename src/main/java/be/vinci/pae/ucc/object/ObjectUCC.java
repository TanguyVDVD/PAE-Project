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
   * Returns a list of all offers.
   *
   * @param query query to filter offers
   * @return a list of all offers
   */
  Object getOffers(String query);

  /**
   * Returns an object corresponding to the id.
   *
   * @param id the id of the object to filter objects
   * @return an object
   */
  ObjectDTO getOne(int id);

  /**
   * Accept a offer.
   *
   * @param id the id of the object to accept
   * @return the object updated
   */
  ObjectDTO accept(int id);

  /**
   * Refuse a offer.
   *
   * @param id               the id of the object to refuse
   * @param reasonForRefusal the reason for refusal
   * @return the object updated
   */
  ObjectDTO refuse(int id, String reasonForRefusal);

  /**
   * Update the iformation and the state of an object.
   *
   * @param id        the id of the object
   * @param objectDTO the object
   * @param date      the date the state has been updated
   * @return null if there is an error or the object updated
   */
  ObjectDTO update(int id, ObjectDTO objectDTO, String date);

}
