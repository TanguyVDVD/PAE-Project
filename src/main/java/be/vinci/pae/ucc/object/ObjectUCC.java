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

  ObjectDTO reject(int id);
}
