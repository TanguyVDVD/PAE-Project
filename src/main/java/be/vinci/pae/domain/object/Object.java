package be.vinci.pae.domain.object;

import java.io.File;
import java.io.InputStream;

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
}
