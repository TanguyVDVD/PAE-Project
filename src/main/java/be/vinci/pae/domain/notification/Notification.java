package be.vinci.pae.domain.notification;


import be.vinci.pae.domain.object.ObjectDTO;

/**
 * Notification interface representing a notification in the domain.
 */
public interface Notification extends NotificationDTO {

  NotificationDTO setUpNotificationText(ObjectDTO objectDTO, NotificationDTO notificationDTO);

  NotificationDTO setUpNotificationUser(NotificationDTO notificationDTO, int idObject);

}
