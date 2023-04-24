package be.vinci.pae.ucc.notification;

import be.vinci.pae.domain.notification.NotificationDTO;
import be.vinci.pae.domain.object.ObjectDTO;
import java.util.List;

/**
 * NotificationUCC interface that provide the methods of a notification.
 */
public interface NotificationUCC {

  /**
   * Method returns a list of NotificationDTO objects that represent all the notifications a. user
   * has.
   *
   * @param id The ID of the user to retrieve notifications for
   * @return A list of NotificationDTO objects associated with the given user ID
   */
  List<NotificationDTO> getNotificationsByUserID(int id);

  /**
   * Creates a NotificationDTO to notify the user that the object as been accepted or refused. * *
   *
   * @param objectDTO the object to create a notification from
   * @return a NotificationDTO object containing the notification message
   */
  NotificationDTO createAcceptedRefusedObjectNotification(ObjectDTO objectDTO);

  /**
   * Creates a NotificationDTO to notify that a new object has been proposed.
   *
   * @param idObject the ID of the object that has been proposed
   * @return a NotificationDTO object containing the notification message
   */
  NotificationDTO createNewObjectPropositionNotification(int idObject);

  /**
   * Marks a notification with the specified ID as read and returns the updated notification
   * object.
   *
   * @param notificationDTO the ID of the notification to mark as read
   * @return the updated NotificationDTO object with the specified ID marked as read
   */
  NotificationDTO markANotificationAsRead(NotificationDTO notificationDTO);

}
