package be.vinci.pae.ucc.object;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.Object;
import be.vinci.pae.domain.object.ObjectDTO;
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
  private DomainFactory myDomainFactory;

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
   * Returns a list of all offers.
   *
   * @param query query to filter offers
   * @return a list of all offers
   */
  @Override
  public java.lang.Object getOffers(String query) {
    return myObjectDAO.getOffers(query);
  }

  /**
   * Returns an object corresponding to the id.
   *
   * @param id the id of the object to filter objects
   * @return an object
   */
  @Override
  public ObjectDTO getOne(int id) {
    return myObjectDAO.getOneById(id);
  }

  /**
   * Accept an offer.
   *
   * @param id the id of the object to accept
   * @return the object updated
   */
  @Override
  public ObjectDTO accept(int id) {
    Object object = (Object) myDomainFactory.getObject();
    String status = myObjectDAO.getOneById(id).getStatus();

    if (object.isStatusAlreadyDefined(status)) {
      return null;
    }

    Date acceptanceDate = object.getCurrentDate();
    return myObjectDAO.setStatusToAccepted(id, acceptanceDate);
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
    Object object = (Object) myDomainFactory.getObject();
    String status = myObjectDAO.getOneById(id).getStatus();

    if (object.isStatusAlreadyDefined(status)) {
      return null;
    }

    Date refusalDate = object.getCurrentDate();
    return myObjectDAO.setStatusToRefused(id, reasonForRefusal, refusalDate);
  }
}
