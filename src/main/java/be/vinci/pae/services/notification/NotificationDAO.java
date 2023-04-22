package be.vinci.pae.services.notification;

import be.vinci.pae.domain.notification.NotificationDTO;
import java.util.List;

/**
 * NotificationDAO interface that provide the method to interact with the db.
 */
public interface NotificationDAO {

  /**
   * Returns a list of NotificationDTO objects for a given user ID.
   *
   * @param id the ID of the user whose notifications are to be retrieved
   * @return a list of NotificationDTO objects associated with the specified user ID
   */
  List<NotificationDTO> getNotificationsByUserID(int id);

  NotificationDTO createAcceptedObjectNotification(NotificationDTO notificationDTO);

  NotificationDTO createAcceptedObjectUserNotification(NotificationDTO notificationDTO);

  NotificationDTO markANotificationAsRead(NotificationDTO notificationDTO);

  List<Integer> getAllHelperId();

}
