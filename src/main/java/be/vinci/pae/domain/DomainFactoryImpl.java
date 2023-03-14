package be.vinci.pae.domain;

import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.domain.availability.AvailabilityImpl;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.object.ObjectImpl;
import be.vinci.pae.domain.objectType.ObjectTypeDTO;
import be.vinci.pae.domain.objectType.ObjectTypeImpl;
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

  /**
   * Returns a new instance of a ObjectTypeDTO.
   *
   * @return an ObjectTypeImpl
   */
  @Override
  public ObjectTypeDTO getObjectType() {
    return new ObjectTypeImpl();
  }

  /**
   * Returns a new instance of an AvailabilityDTO.
   *
   * @return a AvailabilityImpl
   */
  @Override
  public AvailabilityDTO getAvailability() {
    return new AvailabilityImpl();
  }

}
