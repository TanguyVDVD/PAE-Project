package be.vinci.pae.domain.notification;


/**
 * Notification interface representing a notification in the domain.
 */
public interface Notification extends NotificationDTO {

  /**
   * Set up the notification based on the user.
   *
   * @param notificationDTO the notification to set attributes up
   * @param idUser          the id of the user who receive the notification
   * @return the notification
   */
  NotificationDTO setUpNotificationUser(NotificationDTO notificationDTO, int idUser);

}
