package be.vinci.pae.ucc.notification;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.notification.Notification;
import be.vinci.pae.domain.notification.NotificationDTO;
import be.vinci.pae.domain.object.ObjectDTO;
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
   * Creates a NotificationDTO to notify the user that the object as been accepted or refused.
   *
   * @param objectDTO the object to create a notification from
   * @return a NotificationDTO object containing the notification message
   */
  @Override
  public NotificationDTO createAcceptedRefusedObjectNotification(ObjectDTO objectDTO) {

    if (objectDTO.getUser() == null) {
      return null;
    }

    myDalServices.startTransaction();

    try {

      NotificationDTO notificationDTO = domainFactory.getNotification();

      notificationDTO.setRead(false);
      notificationDTO.setIdUser(objectDTO.getUser().getId());

      //NotificationDTO notificationDTO1 = ((Notification) notificationDTO).setUpNotificationText(
      //    objectDTO,
      //    notificationDTO);

      NotificationDTO notificationDTOFromDb = myNotificationDAO.createObjectNotification(
          notificationDTO);

      NotificationDTO notificationDTO2 = ((Notification) notificationDTO).setUpNotificationUser(
          notificationDTOFromDb, objectDTO.getUser().getId());

      NotificationDTO notificationDTOReturn = myNotificationDAO.createObjectUserNotification(
          notificationDTO2);

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
   * @param objectDTO the object that has been proposed
   * @return a NotificationDTO object containing the notification message
   */
  @Override
  public NotificationDTO createNewObjectPropositionNotification(ObjectDTO objectDTO) {

    myDalServices.startTransaction();
    try {

      NotificationDTO notificationDTO = domainFactory.getNotification();

      notificationDTO.setIdObject(objectDTO.getId());
      notificationDTO.setNotificationText(
          "Un nouvel objet vient d'être proposé : " + objectDTO.getDescription());

      NotificationDTO notificationDTOWithoutUser = myNotificationDAO.createObjectNotification(
          notificationDTO);

      List<Integer> listAllHelpers = myNotificationDAO.getAllHelperId();

      for (int i = 0; i < listAllHelpers.size(); i++) {
        notificationDTOWithoutUser.setIdUser(listAllHelpers.get(i));
        notificationDTOWithoutUser.setRead(false);
        myNotificationDAO.createObjectUserNotification(notificationDTOWithoutUser);
      }

      return notificationDTOWithoutUser;


    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      throw e;

    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Marks a notification with the specified ID as read and returns the updated notification
   * object.
   *
   * @param notificationDTO the ID of the notification to mark as read
   * @return the updated NotificationDTO object with the specified ID marked as read
   */
  @Override
  public NotificationDTO markANotificationAsRead(NotificationDTO notificationDTO) {

    myDalServices.startTransaction();

    try {

      return myNotificationDAO.markANotificationAsRead(notificationDTO);

    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      throw e;

    } finally {
      myDalServices.commitTransaction();
    }

  }
}
