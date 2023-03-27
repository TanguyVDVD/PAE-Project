package be.vinci.pae.ucc.object;

import be.vinci.pae.domain.object.Object;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.object.ObjectDAO;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;

/**
 * UserUCCImpl class that implements the ObjectUCC interface.
 */
public class ObjectUCCImpl implements ObjectUCC {

  @Inject
  private ObjectDAO myObjectDAO;

  @Inject
  private DALServices myDalServices;

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
      MyLogger.log(Level.INFO, "Erreur lors de la récupération de la liste des objets");
      throw new WebApplicationException("Erreur lors de la récupération de la liste des objets",
          Status.INTERNAL_SERVER_ERROR);
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
      MyLogger.log(Level.INFO, "Erreur lors de la récupération de la liste des objets");
      throw new WebApplicationException("Erreur lors de la récupération de la liste des objets",
          Status.INTERNAL_SERVER_ERROR);
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
      MyLogger.log(Level.INFO, "Erreur lors de la récupération de la liste des offres");
      throw new WebApplicationException("Erreur lors de la récupération de la liste des offres",
          Status.INTERNAL_SERVER_ERROR);
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
      MyLogger.log(Level.INFO, "Erreur lors de la récupération de l'objet");
      throw new WebApplicationException("Erreur lors de la récupération de l'objet",
          Status.INTERNAL_SERVER_ERROR);
    } finally {
      myDalServices.commitTransaction();
    }

  }

  /**
   * Accept an offer.
   *
   * @param id the id of the object to accept
   * @return the object updated
   */
  @Override
  public ObjectDTO accept(int id) {

    myDalServices.startTransaction();
    try {
      Object object = (Object) myObjectDAO.getOneById(id);
      String status = object.getStatus();

      if (object.isStatusAlreadyDefined(status)) {
        return null;
      }

      return myObjectDAO.setStatusToAccepted(id, LocalDate.now());
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      MyLogger.log(Level.INFO, "Erreur lors de l'acceptation de l'offre");
      throw new WebApplicationException("Erreur lors de l'acceptation de l'offre",
          Status.INTERNAL_SERVER_ERROR);
    } finally {
      myDalServices.commitTransaction();

    }

  }

  /**
   * Refuse an offer.
   *
   * @param id               the id of the object to refuse
   * @param reasonForRefusal the reason for refusal
   * @return the object updated
   */
  @Override
  public ObjectDTO refuse(int id, String reasonForRefusal) {

    myDalServices.startTransaction();

    try {

      Object object = (Object) myObjectDAO.getOneById(id);
      String status = object.getStatus();

      if (object.isStatusAlreadyDefined(status)) {
        return null;
      }

      return myObjectDAO.setStatusToRefused(id, reasonForRefusal, LocalDate.now());
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      MyLogger.log(Level.INFO, "Erreur lors du refus de l'offre");
      throw new WebApplicationException("Erreur lors du refus de l'offre",
          Status.INTERNAL_SERVER_ERROR);
    } finally {
      myDalServices.commitTransaction();
    }

  }

  /**
   * Update the iformation and the state of an object.
   *
   * @param id        the id of the object
   * @param objectDTO the object
   * @param date      the date the state has been updated
   * @return null if there is an error or the object updated
   */
  @Override
  public ObjectDTO update(int id, ObjectDTO objectDTO, LocalDate date) {

    myDalServices.startTransaction();

    try {
      Object objectFromDB = (Object) myObjectDAO.getOneById(id);

      if (!objectFromDB.getStatus().equals("accepté")) {
        return null;
      }

      if (!objectDTO.getState().equals(objectFromDB.getState())) {
        if (objectDTO.getState().equals("à l'atelier")) {
          objectFromDB.setWorkshopDate(date);
        }
        if (objectDTO.getState().equals("en magasin")) {
          objectFromDB.setDepositDate(date);
        }
        if (objectDTO.getState().equals("en vente")) {
          objectFromDB.setOnSaleDate(date);
        }
        if (objectDTO.getState().equals("vendu")) {
          objectFromDB.setSellingDate(date);
        }
        if (objectDTO.getState().equals("retiré")) {
          objectFromDB.setWithdrawalDate(date);
        }
      }

      objectFromDB.setObjectType(objectDTO.getObjectType());
      objectFromDB.setDescription(objectDTO.getDescription());
      objectFromDB.setPrice(objectDTO.getPrice());
      objectFromDB.setState(objectDTO.getState());
      objectFromDB.setIsVisible(objectDTO.getisVisible());

      return myObjectDAO.updateObject(objectFromDB.getId(), objectFromDB);

    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      MyLogger.log(Level.INFO, "Erreur lors de la mise à jour de l'objet");
      throw new WebApplicationException("Erreur lors de la mise à jour de l'objet",
          Status.INTERNAL_SERVER_ERROR);
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

      object.setPhoto(true);

      if (myObjectDAO.updateObject(object.getId(), object) == null) {
        return null;
      }

      return myObjectDAO.getOneById(objectDTO.getId());
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      MyLogger.log(Level.INFO, "Erreur lors de la mise à jour de la photo de l'objet");
      throw new WebApplicationException("Erreur lors de la mise à jour de la photo de l'objet",
          Status.INTERNAL_SERVER_ERROR);
    } finally {
      myDalServices.commitTransaction();
    }
  }
}
