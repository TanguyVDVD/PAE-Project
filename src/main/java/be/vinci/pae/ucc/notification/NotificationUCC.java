package be.vinci.pae.ucc.notification;

import be.vinci.pae.domain.notification.NotificationDTO;
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
   * Creates a NotificationDTO to notify the user that the object with the given ID has been
   * accepted.
   *
   * @param idObject the ID of the object that has been accepted
   * @return a NotificationDTO object containing the notification message
   */
  NotificationDTO createAcceptedObjectNotification(int idObject, int idUser);

  /**
   * Creates a NotificationDTO to notify that a new object has been proposed.
   *
   * @param idObject the ID of the object that has been proposed
   * @return a NotificationDTO object containing the notification message
   */
  NotificationDTO createNewObjectPropositionNotification(int idObject);


}
