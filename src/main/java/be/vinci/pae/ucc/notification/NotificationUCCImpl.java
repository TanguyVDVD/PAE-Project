package be.vinci.pae.ucc.notification;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.notification.NotificationDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.notification.NotificationDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * NotificationUCCImpl class that implements the Notification UCC interface.
 */
public class NotificationUCCImpl implements NotificationUCC {

  @Inject
  private NotificationDAO myNotificationDAO;
  @Inject
  private DALServices myDalServices;

  @Inject
  private DomainFactory domainFactory;

  /**
   * Method returns a list of NotificationDTO objects that represent all the notifications a user
   * has.
   *
   * @param id The ID of the user to retrieve notifications for
   * @return A list of NotificationDTO objects associated with the given user ID
   */
  @Override
  public List<NotificationDTO> getNotificationsByUserID(int id) {

    myDalServices.startTransaction();
    try {
      return myNotificationDAO.getNotificationsByUserID(id);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Creates a NotificationDTO to notify the user that the object with the given ID has been
   * accepted.
   *
   * @param idObject the ID of the object that has been accepted
   * @return a NotificationDTO object containing the notification message
   */
  @Override
  public NotificationDTO createAcceptedObjectNotification(int idObject, int idUser) {

    myDalServices.startTransaction();

    try {

      NotificationDTO notificationDTO = domainFactory.getNotification();

      notificationDTO.setIdObject(idObject);
      notificationDTO.setNotificationText("Votre objet a été accepté !");

      NotificationDTO notificationDTOFromDb = myNotificationDAO.createAcceptedObjectNotification(
          notificationDTO);

      notificationDTOFromDb.setRead(false);
      notificationDTOFromDb.setIdUser(idUser);
      NotificationDTO notificationDTOReturn = myNotificationDAO.createAcceptedObjectUserNotification(
          notificationDTOFromDb);

      notificationDTOReturn.setNotificationText(notificationDTOFromDb.getNotificationText());
      notificationDTOReturn.setIdObject(notificationDTOFromDb.getIdObject());
      return notificationDTOReturn;

    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      throw e;

    } finally {
      myDalServices.commitTransaction();
    }

  }

  /**
   * Creates a NotificationDTO to notify that a new object has been proposed.
   *
   * @param idObject the ID of the object that has been proposed
   * @return a NotificationDTO object containing the notification message
   */
  @Override
  public NotificationDTO createNewObjectPropositionNotification(int idObject) {

    myDalServices.startTransaction();
    try {

      NotificationDTO notificationDTO = domainFactory.getNotification();

      notificationDTO.setIdObject(idObject);
      notificationDTO.setRead(false);
      notificationDTO.setNotificationText(
          "Un nouvel objet vient d'être proposé");

    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      throw e;

    } finally {
      myDalServices.commitTransaction();
    }
    return null;
  }
}
