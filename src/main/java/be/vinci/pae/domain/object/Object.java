package be.vinci.pae.domain.object;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Object interface representing an object in the domain.
 */
public interface Object extends ObjectDTO {

  /**
   * Check if the object is already accepted or rejected.
   *
   * @param status the status of the object
   * @return true if the status of the object is already defined, else false
   */
  boolean isStatusAlreadyDefined(String status);


  /**
   * Get the photo of the object.
   *
   * @return the photo of the object
   */
  File photoFile();

  /**
   * Save the photo of the object.
   *
   * @param photo the photo to set
   * @return whether the saving was successful
   */
  boolean savePhoto(InputStream photo);

  /**
   * Check if the state is "en magasain" of "à l'atelier".
   *
   * @param objectDTO the object to check the state
   * @return true if the state correspond else false
   */
  boolean isStateWorkshopOrShop(ObjectDTO objectDTO);

  /**
   * Set the correct change state date.
   *
   * @param objectDTO       the object after the state change
   * @param objectDTOFromDb the object before the state change
   * @param dateChange      the date of de change
   * @return the objectDTO change, null if there is a problem
   */
  ObjectDTO setStateDate(ObjectDTO objectDTO, ObjectDTO objectDTOFromDb, LocalDate dateChange);
}
