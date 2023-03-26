package be.vinci.pae.domain.object;

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
  static boolean isStatusAlreadyDefined(String status);
}
