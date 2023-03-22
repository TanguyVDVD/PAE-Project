package be.vinci.pae.domain.object;

/**
 * Object interface representing an object in the domain.
 */
public interface Object extends ObjectDTO {

  /**
   * Method that gives the current date.
   */
  String getCurrentDate();
}
