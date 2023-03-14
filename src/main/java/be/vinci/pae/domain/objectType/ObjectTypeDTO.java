package be.vinci.pae.domain.objectType;

/**
 * ObjectTypeDTO interface. Representing a data transfer object (DTO) for a object type in the
 * domain The interface only contains getter and setter.
 */
public interface ObjectTypeDTO {

  /**
   * Return the id of an object type.
   *
   * @return an int corresponding to the id of an object type
   */
  int getId();

  /**
   * Set the id of an object type.
   *
   * @param id the id of an object type
   */
  void setId(int id);

  /**
   * Return the label of an object type.
   *
   * @return a String corresponding to the label of an object type
   */
  String getLabel();

  /**
   * Set the label of an object type.
   *
   * @param label the description of an object type
   */
  void setLabel(String label);
}
