package be.vinci.pae.domain.objectType;

/**
 * ObjectTypeImpl class that implements the ObjectTypeDTO interface Contains all the attribute of an
 * object type.
 */
public class ObjectTypeImpl implements ObjectType {

  private int id;
  private String label;

  /**
   * Return the id of an object type.
   *
   * @return an int corresponding to the id of an object type
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Set the id of an object type.
   *
   * @param id the id of an object type
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Return the label of an object type.
   *
   * @return a String corresponding to the label of an object type
   */
  @Override
  public String getLabel() {
    return label;
  }

  /**
   * Set the label of an object type.
   *
   * @param label the description of an object type
   */
  @Override
  public void setLabel(String label) {
    this.label = label;
  }
}
