package be.vinci.pae.ucc.object;

import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.services.object.ObjectDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * UserUCCImpl class that implements the ObjectUCC interface.
 */
public class ObjectUCCImpl implements ObjectUCC {

  @Inject
  private ObjectDAO myObjectDAO;


  /**
   * Returns a list of all objects.
   *
   * @param query query to filter objects
   * @return a list of all objects
   */
  @Override
  public List<ObjectDTO> getObjects(String query) {
    return myObjectDAO.getAll(query);
  }

  /**
   * Accept an offer.
   *
   * @param id the id of the object to accept
   * @return the object updated
   */
  @Override
  public ObjectDTO accept(int id) {
    return myObjectDAO.setStatusToAccepted(id);
  }

  /**
   * Reject an offer.
   *
   * @param id the id of the object to reject
   * @return the object updated
   */
  @Override
  public ObjectDTO reject(int id) {
    return null;
  }

  @Override
  public ObjectDTO update(int id, ObjectDTO objectDTO, String date) {

    ObjectDTO objectFromDB = myObjectDAO.getOneById(id);
    System.out.println(objectFromDB.getDescription());

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
    objectFromDB.setVisibility(objectDTO.isVisible());

    return myObjectDAO.updateObject(objectFromDB.getId(), objectFromDB);
  }


}
