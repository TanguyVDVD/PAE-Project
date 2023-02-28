package be.vinci.pae.domain;

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

}
