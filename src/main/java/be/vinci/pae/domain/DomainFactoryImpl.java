package be.vinci.pae.domain;

public class DomainFactoryImpl implements DomainFactory {

  /**
   * @return a UserImpl
   */
  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

}
