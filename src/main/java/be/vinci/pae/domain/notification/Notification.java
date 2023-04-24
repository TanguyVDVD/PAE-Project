package be.vinci.pae.domain.notification;


import be.vinci.pae.domain.object.ObjectDTO;

/**
 * Notification interface representing a notification in the domain.
 */
public interface Notification extends NotificationDTO {


  /**
   * Set up the notification text based on the object.
   *
   * @param objectDTO       the object the notification is from
   * @param notificationDTO the notification to set up
   * @return the notification when all attributes are set
   */
  NotificationDTO setUpNotificationText(ObjectDTO objectDTO, NotificationDTO notificationDTO);

  /**
   * Set up the notification based on the user.
   *
   * @param notificationDTO the notification to set attributes up
   * @param idUser          the id of the user who receive the notification
   * @return the notification
   */
  NotificationDTO setUpNotificationUser(NotificationDTO notificationDTO, int idUser);

}
