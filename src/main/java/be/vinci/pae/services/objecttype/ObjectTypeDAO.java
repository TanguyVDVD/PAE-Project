package be.vinci.pae.services.objecttype;

/**
 * ObjectTypeDAO interface that provide the method to interact with the db.
 */
public interface ObjectTypeDAO {

  /**
   * Get the object type by the id.
   *
   * @param id of the object type
   * @return the object type String corresponding to the id
   */
  String getOneById(int id);

  /**
   * Get the object type id by the String.
   *
   * @param state of the object type
   * @return the object type id corresponding to the String
   */
  int getIdByString(String state);
}
