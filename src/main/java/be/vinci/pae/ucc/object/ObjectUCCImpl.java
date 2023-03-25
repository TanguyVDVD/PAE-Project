package be.vinci.pae.ucc.object;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.Object;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.object.ObjectDAO;
import jakarta.inject.Inject;
import java.sql.Date;
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
  private DomainFactory myDomainFactory;


  /**
   * Returns a list of all objects.
   *
   * @param query query to filter objects
   * @return a list of all objects
   */
  @Override
  public List<ObjectDTO> getObjects(String query) {
    List<ObjectDTO> listAllObjects = null;
    myDalServices.startTransaction();
    try {
      listAllObjects = myObjectDAO.getAll(query);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
    } finally {
      myDalServices.commitTransaction();
    }
    return listAllObjects;
  }

  /**
   * Returns a list of all offers.
   *
   * @param query query to filter offers
   * @return a list of all offers
   */
  @Override
  public java.lang.Object getOffers(String query) {

    java.lang.Object object = null;

    myDalServices.startTransaction();
    try {
      object = myObjectDAO.getOffers(query);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
    } finally {
      myDalServices.commitTransaction();
    }
    return object;
  }

  /**
   * Returns an object corresponding to the id.
   *
   * @param id the id of the object to filter objects
   * @return an object
   */
  @Override
  public ObjectDTO getOne(int id) {

    ObjectDTO objectDTO = null;

    myDalServices.startTransaction();

    try {
      objectDTO = myObjectDAO.getOneById(id);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
    } finally {
      myDalServices.commitTransaction();
    }
    return objectDTO;
  }

  /**
   * Accept an offer.
   *
   * @param id the id of the object to accept
   * @return the object updated
   */
  @Override
  public ObjectDTO accept(int id) {
    ObjectDTO objectDTO = null;

    myDalServices.startTransaction();
    try {
      Object object = (Object) myDomainFactory.getObject();
      String status = myObjectDAO.getOneById(id).getStatus();

      if (object.isStatusAlreadyDefined(status)) {
        return null;
      }

      Date acceptanceDate = object.getCurrentDate();
      objectDTO = myObjectDAO.setStatusToAccepted(id, acceptanceDate);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
    } finally {
      myDalServices.commitTransaction();
    }
    return objectDTO;
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

    ObjectDTO objectDTO = null;

    myDalServices.startTransaction();

    try {

      Object object = (Object) myDomainFactory.getObject();
      String status = myObjectDAO.getOneById(id).getStatus();

      if (object.isStatusAlreadyDefined(status)) {
        return null;
      }

      Date refusalDate = object.getCurrentDate();
      objectDTO = myObjectDAO.setStatusToRefused(id, reasonForRefusal, refusalDate);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
    } finally {
      myDalServices.commitTransaction();
    }
    return objectDTO;
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
  public ObjectDTO update(int id, ObjectDTO objectDTO, String date) {

    ObjectDTO objectDTOToReturn = null;

    myDalServices.startTransaction();

    try {
      ObjectDTO objectFromDB = myObjectDAO.getOneById(id);

      if (!objectFromDB.getStatus().equals("accepté")) {
        return null;
      }

      if (!objectDTO.getState().equals(objectFromDB.getState())) {
        if (objectDTO.getState().equals("en atelier")) {
          objectFromDB.setWorkshopDate(date);
        }
        if (objectDTO.getState().equals("en magasin")) {
          objectFromDB.setDepositDate(date);
        }
        if (objectDTO.getState().equals("mis en vente")) {
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

      objectDTOToReturn = myObjectDAO.updateObject(objectFromDB.getId(), objectFromDB);

    } catch (Exception e) {
      myDalServices.rollbackTransaction();
    } finally {
      myDalServices.commitTransaction();
    }
    return objectDTOToReturn;
  }


}
