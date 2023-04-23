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

  /**
   * Creates a new object notification with the provided notification data and returns the created
   * notification.
   *
   * @param notificationDTO the data of the notification to be created
   * @return the created notification
   */
  NotificationDTO createObjectNotification(NotificationDTO notificationDTO);

  /**
   * Creates a new user notification with the provided notification data and returns the created
   * notification.
   *
   * @param notificationDTO the data of the notification to be created
   * @return the created notification
   */
  NotificationDTO createObjectUserNotification(NotificationDTO notificationDTO);

  /**
   * Marks the provided notification as read and returns the updated notification data.
   *
   * @param notificationDTO the notification data to be marked as read
   * @return the updated notification data
   */
  NotificationDTO markANotificationAsRead(NotificationDTO notificationDTO);

  /**
   * Returns a list of all helper IDs.
   *
   * @return a list of all helper IDs
   */
  List<Integer> getAllHelperId();

}
