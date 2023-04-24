package be.vinci.pae.domain;


import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.domain.notification.NotificationDTO;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.objecttype.ObjectTypeDTO;
import be.vinci.pae.domain.user.UserDTO;

/**
 * DomainFactory interface. representing a factory to create objects from the domain The interface
 * contains all the BIZ methods
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
   * @return an ObjectImpl
   */
  ObjectDTO getObject();

  /**
   * Returns a new instance of a ObjectTypeDTO.
   *
   * @return an ObjectTypeImpl
   */
  ObjectTypeDTO getObjectType();

  /**
   * Returns a new instance of an AvailabilityDTO.
   *
   * @return a AvailabilityImpl
   */
  AvailabilityDTO getAvailability();

  /**
   * Returns a new instance of a NotificationDTO.
   *
   * @return a NotificationImpl
   */
  NotificationDTO getNotification();

}
