package be.vinci.pae.ucc.notification;

import be.vinci.pae.domain.notification.NotificationDTO;
import java.util.List;

/**
 * NotificationUCCImpl class that implements the Notification UCC interface.
 */
public class NotificationUCCImpl implements NotificationUCC {

  /**
   * This method returns a list of NotificationDTO objects that represent all the notifications a.
   * user has.
   *
   * @param id The ID of the user to retrieve notifications for
   * @return A list of NotificationDTO objects associated with the given user ID
   */
  @Override
  public List<NotificationDTO> getNotificationsByUserID(int id) {
    return null;
  }
}
