package be.vinci.pae.ucc.notification;

import be.vinci.pae.domain.notification.NotificationDTO;
import java.util.List;

/**
 * NotificationUCC interface that provide the methods of a notification.
 */
public interface NotificationUCC {

  /**
   * This method returns a list of NotificationDTO objects that represent all the notifications a
   * user has.
   *
   * @param id The ID of the user to retrieve notifications for
   * @return A list of NotificationDTO objects associated with the given user ID
   */
  List<NotificationDTO> getNotificationsByUserID(int id);

}
