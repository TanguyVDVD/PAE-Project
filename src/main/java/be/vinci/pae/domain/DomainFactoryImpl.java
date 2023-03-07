package be.vinci.pae.domain;

import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.object.ObjectImpl;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserImpl;

/**
 * Implementation of the DomainFactory interface Provides a new instance of a UserDTO.
 */
public class DomainFactoryImpl implements DomainFactory {

  /**
   * Returns a new instance of a UserDTO.
   *
   * @return a UserImpl
   */
  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

  /**
   * Returns a new instance of an ObjectDTO.
   *
   * @return a ObjectImpl
   */
  @Override
  public ObjectDTO getObject() {
    return new ObjectImpl();
  }

}
