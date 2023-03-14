package be.vinci.pae.services.object;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.services.DALServices;
import jakarta.inject.Inject;
import java.util.List;

/**
 * ObjectDAO class that implements ObjectDAO interface Provide the different methods.
 */
public class ObjectDAOImpl implements ObjectDAO {

  @Inject
  private DomainFactory myDomainFactory;

  @Inject
  private DALServices myDALServices;

  /**
   * Get all objects.
   *
   * @param query query to filter objects
   * @return the list of objects
   */
  @Override
  public List<ObjectDTO> getAll(String query) {
    return null;
  }
}
