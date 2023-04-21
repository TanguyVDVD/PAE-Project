package be.vinci.pae.services.notification;

import be.vinci.pae.domain.notification.NotificationDTO;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {

  /**
   * Returns a list of NotificationDTO objects for a given user ID.
   *
   * @param id the ID of the user whose notifications are to be retrieved
   * @return a list of NotificationDTO objects associated with the specified user ID
   */
  @Override
  public List<NotificationDTO> getNotificationsByUserID(int id) {
    return null;
  }
}
