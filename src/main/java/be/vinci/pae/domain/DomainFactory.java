package be.vinci.pae.domain;


import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.user.UserDTO;

/**
 * DomainFactory interface. representing a factory tor create object objects from the domain The
 * interface contains all the BIZ methods
 */
public interface DomainFactory {

  /**
   * Returns a new instance of a UserDTO.
   *
   * @return a UserImpl
   */
  UserDTO getUser();

  /**
   * Returns a new instance of a ObjectDTO.
   *
   * @return a UserImpl
   */
  ObjectDTO getObject();

}
