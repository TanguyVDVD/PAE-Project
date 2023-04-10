package be.vinci.pae.ucc.object;

import be.vinci.pae.domain.object.ObjectDTO;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
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
   * Get all objects by user.
   *
   * @param id the id of the user
   * @return the list of objects
   */
  List<ObjectDTO> getObjectsByUser(int id);

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
  ObjectDTO accept(int id, int versionNumber);

  /**
   * Refuse a offer.
   *
   * @param id               the id of the object to refuse
   * @param reasonForRefusal the reason for refusal
   * @return the object updated
   */
  ObjectDTO refuse(int id, String reasonForRefusal, int versionNumber);

  /**
   * Update the iformation and the state of an object.
   *
   * @param id        the id of the object
   * @param objectDTO the object
   * @param date      the date the state has been updated
   * @return null if there is an error or the object updated
   */
  ObjectDTO update(int id, ObjectDTO objectDTO, LocalDate date);

  /**
   * Get an object's photo.
   *
   * @param objectDTO the object
   * @return the photo of the object
   */
  File getPhoto(ObjectDTO objectDTO);

  /**
   * Update an object's photo.
   *
   * @param objectDTO the object
   * @param file      the new photo
   * @return the updated object
   */
  ObjectDTO updatePhoto(ObjectDTO objectDTO, InputStream file);
}
