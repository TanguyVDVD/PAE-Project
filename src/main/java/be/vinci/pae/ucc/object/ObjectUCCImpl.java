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
   * Refuse a proposal.
   *
   * @param id               the id of the object to refuse
   * @param reasonForRefusal the reason for refusal
   * @return the object updated
   */
  @Override
  public ObjectDTO refuse(int id, String reasonForRefusal) {
    Object object = (Object) myDomainFactory.getObject();
    String status = myObjectDAO.getOneById(id).getStatus();

    if (object.isStatusAlreadyDefined(id, status)) {
      return null;
    }

    Date refusalDate = object.getCurrentDate();
    return myObjectDAO.setStatusToRefused(id, reasonForRefusal, refusalDate);
  }
}
