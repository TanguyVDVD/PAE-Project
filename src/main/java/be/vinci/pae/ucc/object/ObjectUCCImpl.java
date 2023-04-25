package be.vinci.pae.ucc.object;

import be.vinci.pae.domain.object.Object;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.object.ObjectDAO;
import be.vinci.pae.ucc.notification.NotificationUCC;
import jakarta.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;


/**
 * UserUCCImpl class that implements the ObjectUCC interface.
 */
public class ObjectUCCImpl implements ObjectUCC {

  @Inject
  private ObjectDAO myObjectDAO;

  @Inject
  private DALServices myDalServices;

  @Inject
  private NotificationUCC myNotificationUCC;

  /**
   * Returns a list of all objects.
   *
   * @param query query to filter objects
   * @return a list of all objects
   */
  @Override
  public List<ObjectDTO> getObjects(String query) {
    myDalServices.startTransaction();
    try {
      return myObjectDAO.getAll(query);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Get all objects by user.
   *
   * @param id the id of the user
   * @return the list of objects
   */
  @Override
  public List<ObjectDTO> getObjectsByUser(int id) {
    myDalServices.startTransaction();
    try {
      return myObjectDAO.getAllByUser(id);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Returns a list of all offers.
   *
   * @param query query to filter offers
   * @return a list of all offers
   */
  @Override
  public java.lang.Object getOffers(String query) {

    myDalServices.startTransaction();
    try {
      return myObjectDAO.getOffers(query);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }

  }

  /**
   * Returns an object corresponding to the id.
   *
   * @param id the id of the object to filter objects
   * @return an object
   */
  @Override
  public ObjectDTO getOne(int id) {

    myDalServices.startTransaction();

    try {
      return myObjectDAO.getOneById(id);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }

  }

  /**
   * Accept an offer.
   *
   * @param id            the id of the object to accept
   * @param versionNumber the version number of the object
   * @return the object updated
   */
  @Override
  public ObjectDTO accept(int id, int versionNumber) {

    myDalServices.startTransaction();
    try {
      Object object = (Object) myObjectDAO.getOneById(id);
      String status = object.getStatus();

      if (object.isStatusAlreadyDefined(status)) {
        return null;
      }

      ObjectDTO objectDTOToReturn = myObjectDAO.setStatusToAccepted(id, LocalDate.now(),
          versionNumber);

      myNotificationUCC.createAcceptedRefusedObjectNotification(objectDTOToReturn);

      return objectDTOToReturn;
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();

    }

  }

  /**
   * Refuse an offer.
   *
   * @param id               the id of the object to refuse
   * @param reasonForRefusal the reason for refusal
   * @param versionNumber    the version number of the object
   * @return the object updated
   */
  @Override
  public ObjectDTO refuse(int id, String reasonForRefusal, int versionNumber) {

    myDalServices.startTransaction();

    try {

      Object object = (Object) myObjectDAO.getOneById(id);
      String status = object.getStatus();

      if (object.isStatusAlreadyDefined(status)) {
        return null;
      }

      ObjectDTO objectDTOToReturn = myObjectDAO.setStatusToRefused(id, reasonForRefusal,
          LocalDate.now(), versionNumber);

      myNotificationUCC.createAcceptedRefusedObjectNotification(objectDTOToReturn);

      return objectDTOToReturn;
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }

  }

  /**
   * Update the information and the state of an object.
   *
   * @param id        the id of the object
   * @param objectDTO the object
   * @param date      the date the state has been updated
   * @param user      the user trying to update the object
   * @return null if there is an error or the object updated
   */
  @Override
  public ObjectDTO update(int id, ObjectDTO objectDTO, LocalDate date, UserDTO user) {

    myDalServices.startTransaction();

    try {
      Object objectFromDB = (Object) myObjectDAO.getOneById(id);

      objectFromDB.isStateChangeAllowed(objectDTO, user);

      String newState = objectDTO.getState();

      objectFromDB.setStateDate(newState, date);
      objectFromDB.setObjectType(objectDTO.getObjectType());
      objectFromDB.setDescription(objectDTO.getDescription());
      objectFromDB.setPrice(objectDTO.getPrice());
      objectFromDB.setState(newState);
      objectFromDB.setIsVisible(objectDTO.getisVisible());
      objectFromDB.setVersionNumber(objectDTO.getVersionNumber());

      return myObjectDAO.updateObject(objectFromDB.getId(), objectFromDB);

    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Get an object's photo.
   *
   * @param objectDTO the object
   * @return the photo of the object
   */
  @Override
  public File getPhoto(ObjectDTO objectDTO) {
    Object object = (Object) objectDTO;

    return object.photoFile();
  }

  /**
   * Update an object's photo.
   *
   * @param objectDTO the object
   * @param file      the new photo
   * @return the updated object
   */
  @Override
  public ObjectDTO updatePhoto(ObjectDTO objectDTO, InputStream file) {
    myDalServices.startTransaction();

    try {
      Object object = (Object) myObjectDAO.getOneById(objectDTO.getId());

      if (object == null) {
        return null;
      }

      object.savePhoto(file);

      return myObjectDAO.updateObject(object.getId(), object);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Add an object.
   *
   * @param objectDTO the object to add
   * @return the object that has been added
   */
  @Override
  public ObjectDTO add(ObjectDTO objectDTO) {
    myDalServices.startTransaction();

    try {

      ObjectDTO objectDTOReturn = myObjectDAO.insert(objectDTO);
      myNotificationUCC.createNewObjectPropositionNotification(objectDTOReturn.getId());
      return objectDTOReturn;
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }
}
