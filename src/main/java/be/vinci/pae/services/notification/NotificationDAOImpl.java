package be.vinci.pae.services.notification;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.notification.NotificationDTO;
import be.vinci.pae.services.DalBackendServices;
import be.vinci.pae.utils.exceptions.DALException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationDAO class that implements NotificationDAO interface Provide the different methods.
 */
public class NotificationDAOImpl implements NotificationDAO {

  @Inject
  private DalBackendServices dalBackendServices;

  @Inject
  private DomainFactory myDomainFactory;

  public NotificationDTO dtoFromRS(ResultSet resultSet) {

    NotificationDTO notificationDTO = myDomainFactory.getNotification();

    try {
      notificationDTO.setId(resultSet.getInt("id_notification"));
      notificationDTO.setNotificationText(resultSet.getString("notification_text"));
      notificationDTO.setIdObject(resultSet.getInt("id_object"));
    } catch (SQLException e) {
      throw new DALException("Error during the mapping of the notification", e);
    }

    return notificationDTO;
  }

  public NotificationDTO dtoFromRSAll(ResultSet resultSet) {

    NotificationDTO notificationDTO = myDomainFactory.getNotification();

    try {
      notificationDTO.setId(resultSet.getInt("id_notification"));
      notificationDTO.setNotificationText(resultSet.getString("notification_text"));
      notificationDTO.setIdObject(resultSet.getInt("id_object"));
      notificationDTO.setRead(resultSet.getBoolean("read"));
      notificationDTO.setIdUser(resultSet.getInt("id_user"));
    } catch (SQLException e) {
      throw new DALException("Error during the mapping of the notification", e);
    }

    return notificationDTO;
  }


  public NotificationDTO dtoFromRSUser(ResultSet resultSet) {

    NotificationDTO notificationDTO = myDomainFactory.getNotification();

    try {
      notificationDTO.setRead(resultSet.getBoolean("read"));
      notificationDTO.setId(resultSet.getInt("id_notification"));
      notificationDTO.setIdUser(resultSet.getInt("id_user"));
    } catch (SQLException e) {
      throw new DALException("Error during the mapping of the notification", e);
    }

    return notificationDTO;
  }

  /**
   * Returns a list of NotificationDTO objects for a given user ID.
   *
   * @param id the ID of the user whose notifications are to be retrieved
   * @return a list of NotificationDTO objects associated with the specified user ID
   */
  @Override
  public List<NotificationDTO> getNotificationsByUserID(int id) {
    String request = "SELECT * FROM pae.notifications n, pae.users_notifications un "
        + "WHERE n.id_notification = un.id_notification AND un.id_user = ? ORDER BY un.id_notification DESC;";

    ArrayList<NotificationDTO> objects = new ArrayList<>();

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objects.add(dtoFromRSAll(rs));
        }
      }
    } catch (SQLException e) {
      throw new DALException("Error getting all notifications by user", e);
    }

    return objects;
  }

  @Override
  public NotificationDTO createAcceptedObjectNotification(NotificationDTO notificationDTO) {

    String request = "INSERT INTO pae.notifications VALUES (DEFAULT, ?, ?);";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request, true)) {

      ps.setString(1, notificationDTO.getNotificationText());
      ps.setInt(2, notificationDTO.getIdObject());
      ps.executeUpdate();

      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        return dtoFromRS(rs);
      } else {
        throw new DALException("Error during the add of a notification");
      }
    } catch (SQLException e) {
      throw new DALException("Error during the add of a notification", e);
    }

  }

  @Override
  public NotificationDTO createAcceptedObjectUserNotification(NotificationDTO notificationDTO) {

    String request = "INSERT INTO pae.users_notifications VALUES (?, ?, ?);";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request, true)) {

      ps.setBoolean(1, notificationDTO.getRead());
      ps.setInt(2, notificationDTO.getId());
      ps.setInt(3, notificationDTO.getIdUser());
      ps.executeUpdate();

      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        return dtoFromRSUser(rs);
      } else {
        throw new DALException("Error during the add of a notification");
      }
    } catch (SQLException e) {
      throw new DALException("Error during the add of a notification", e);
    }

  }
}
