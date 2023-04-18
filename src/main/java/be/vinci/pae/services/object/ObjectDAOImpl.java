package be.vinci.pae.services.object;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.services.DalBackendServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import be.vinci.pae.services.objecttype.ObjectTypeDAO;
import be.vinci.pae.services.user.UserDAO;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * ObjectDAO class that implements ObjectDAO interface Provide the different methods.
 */
public class ObjectDAOImpl implements ObjectDAO {

  @Inject
  private DomainFactory myDomainFactory;

  @Inject
  private DalBackendServices dalBackendServices;

  @Inject
  private UserDAO myUserDao;

  @Inject
  private AvailabilityDAO myAvailabilityDao;

  @Inject
  private ObjectTypeDAO myObjectTypeDAO;

  /**
   * Map a ResultSet to an ObjectDTO.
   *
   * @param resultSet the ResultSet
   * @return the ObjectDTO
   */
  public ObjectDTO dtoFromRS(ResultSet resultSet) {
    ObjectDTO object = myDomainFactory.getObject();

    try {
      object.setId(resultSet.getInt("id_object"));
      object.setVersionNumber(resultSet.getInt("version_number"));
      object.setDescription(resultSet.getString("description"));
      object.setPhoto(resultSet.getBoolean("photo"));
      object.setPhoneNumber(resultSet.getString("phone_number"));
      object.setIsVisible(resultSet.getBoolean("is_visible"));
      object.setPrice(resultSet.getDouble("price"));
      object.setState(resultSet.getString("state"));
      object.setOfferDate(resultSet.getDate("offer_date") == null ? null
          : resultSet.getDate("offer_date").toLocalDate());
      object.setAcceptanceDate(resultSet.getDate("acceptance_date") == null ? null
          : resultSet.getDate("acceptance_date").toLocalDate());
      object.setRefusalDate(resultSet.getDate("refusal_date") == null ? null
          : resultSet.getDate("refusal_date").toLocalDate());
      object.setWorkshopDate(resultSet.getDate("workshop_date") == null ? null
          : resultSet.getDate("workshop_date").toLocalDate());
      object.setDepositDate(resultSet.getDate("deposit_date") == null ? null
          : resultSet.getDate("deposit_date").toLocalDate());
      object.setSellingDate(resultSet.getDate("selling_date") == null ? null
          : resultSet.getDate("selling_date").toLocalDate());
      object.setWithdrawalDate(resultSet.getDate("withdrawal_date") == null ? null
          : resultSet.getDate("withdrawal_date").toLocalDate());
      object.setOnSaleDate(resultSet.getDate("on_sale_date") == null ? null
          : resultSet.getDate("on_sale_date").toLocalDate());
      object.setWorkshopDate(resultSet.getDate("workshop_date") == null ? null
          : resultSet.getDate("workshop_date").toLocalDate());
      object.setTimeSlot(resultSet.getString("time_slot"));
      object.setStatus(resultSet.getString("status"));
      object.setReasonForRefusal(resultSet.getString("reason_for_refusal"));
      object.setPhoneNumber(resultSet.getString("phone_number"));
      object.setReceiptDate(
          myAvailabilityDao.getOneById(resultSet.getInt("receipt_date")) == null ? null
              : myAvailabilityDao.getOneById(resultSet.getInt("receipt_date")).getDate());
      object.setUser(myUserDao.getOneById(resultSet.getInt("id_user")));
      object.setObjectType(myObjectTypeDAO.getOneById(resultSet.getInt("id_object_type")));

    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error get dto from rs");
      se.printStackTrace();
    }

    return object;
  }

  /**
   * Get all objects.
   *
   * @param query query to filter objects
   * @return the list of objects
   */
  @Override
  public List<ObjectDTO> getAll(String query) {
    String request = "SELECT * FROM pae.objects o, pae.object_types ot "
        + "WHERE o.id_object_type = ot.id_object_type AND LOWER(o.description || ' ' || ot.label) "
        + "LIKE CONCAT('%', ?, '%') ORDER BY id_object;";

    ArrayList<ObjectDTO> objects = new ArrayList<>();

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setString(1, query == null ? "" : query.toLowerCase());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objects.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error get all object");
      se.printStackTrace();
    }

    return objects;
  }

  /**
   * Get all objects by user.
   *
   * @param id the id of the user
   * @return the list of objects
   */
  @Override
  public List<ObjectDTO> getAllByUser(int id) {
    String request = "SELECT * FROM pae.objects o, pae.object_types ot "
        + "WHERE o.id_object_type = ot.id_object_type AND o.id_user = ? ORDER BY id_object;";

    ArrayList<ObjectDTO> objects = new ArrayList<>();

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objects.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error get all object from a user");
      se.printStackTrace();
    }

    return objects;
  }

  /**
   * Get all objects by availability.
   *
   * @param id the id of the availability
   * @return the list of objects
   */
  @Override
  public List<ObjectDTO> getAllByAvailability(int id) {
    String request = "SELECT * FROM pae.objects o, pae.availabilities a "
        + "WHERE o.receipt_date = a.id_availability AND a.id_availability = ? "
        + "ORDER BY a.date desc;";

    ArrayList<ObjectDTO> objects = new ArrayList<>();

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objects.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error get all object from an availability");
      se.printStackTrace();
    }

    return objects;
  }

  /**
   * Get all offers.
   *
   * @param query query to filter offers
   * @return the list of offers
   */
  @Override
  public Object getOffers(String query) {
    String request = "SELECT * FROM pae.objects o, pae.object_types ot "
        + "WHERE o.id_object_type = ot.id_object_type AND o.state = 'proposé' "
        + "AND LOWER(o.description || ' ' || ot.label) "
        + "LIKE CONCAT('%', ?, '%') ORDER BY id_object;";

    ArrayList<ObjectDTO> objects = new ArrayList<>();

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setString(1, query == null ? "" : query.toLowerCase());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objects.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error get all offers");
      se.printStackTrace();
    }

    return objects;
  }

  /**
   * Get the object by the id.
   *
   * @param id the id of the object
   * @return the object corresponding to the id
   */
  @Override
  public ObjectDTO getOneById(int id) {
    String request = "SELECT * FROM pae.objects WHERE id_object = ?";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dtoFromRS(rs);
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error get one object by id");
      se.printStackTrace();
    }

    return null;
  }

  /**
   * Update the object in the db.
   *
   * @param id        the id of the object
   * @param objectDTO the object to update
   * @return null if there is an error then the object
   */
  public ObjectDTO updateObject(int id, ObjectDTO objectDTO) {
    String request =
        "UPDATE pae.objects SET description = ?, id_object_type = ?, is_visible = ?, state = ?, "
            + "price = ?, "
            + "workshop_date = ?, "
            + "deposit_date = ?, "
            + "on_sale_date = ?, "
            + "selling_date = ?, "
            + "withdrawal_date = ?, "
            + "version_number = ? "
            + "WHERE id_object = ? "
            + "AND version_number = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setString(1, objectDTO.getDescription());
      ps.setInt(2, myObjectTypeDAO.getIdByLabel(objectDTO.getObjectType()));
      ps.setBoolean(3, objectDTO.getisVisible());
      ps.setString(4, objectDTO.getState());
      ps.setDouble(5, objectDTO.getPrice());
      ps.setDate(6, objectDTO.getWorkshopDate() == null ? null
          : java.sql.Date.valueOf(objectDTO.getWorkshopDate()));
      ps.setDate(7, objectDTO.getDepositDate() == null ? null
          : java.sql.Date.valueOf(objectDTO.getDepositDate()));
      ps.setDate(8, objectDTO.getOnSaleDate() == null ? null
          : java.sql.Date.valueOf(objectDTO.getOnSaleDate()));
      ps.setDate(9, objectDTO.getSellingDate() == null ? null
          : java.sql.Date.valueOf(objectDTO.getSellingDate()));
      ps.setDate(10, objectDTO.getWithdrawalDate() == null ? null
          : java.sql.Date.valueOf(objectDTO.getWithdrawalDate()));
      ps.setInt(11, objectDTO.getVersionNumber() + 1);
      ps.setInt(12, id);
      ps.setInt(13, objectDTO.getVersionNumber());
      ps.executeUpdate();
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error update an object");
      se.printStackTrace();
      return null;
    }
    objectDTO.setVersionNumber(objectDTO.getVersionNumber() + 1);
    return objectDTO;
  }

  /**
   * Set the status of an object to accepted.
   *
   * @param id             the id of the object
   * @param acceptanceDate the acceptance date of the object
   * @return the modified object
   */
  @Override
  public ObjectDTO setStatusToAccepted(int id, LocalDate acceptanceDate, int versionNumber) {
    String request =
        "UPDATE pae.objects SET state = 'accepté', status = 'accepté', acceptance_date = ?, version_number = ? "
            + "WHERE id_object = ? AND version_number = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setDate(1, java.sql.Date.valueOf(acceptanceDate));
      ps.setInt(2, versionNumber + 1);

      ps.setInt(3, id);
      ps.setInt(4, versionNumber);
      ps.executeUpdate();
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error set object accepted");
      se.printStackTrace();
    }

    ObjectDTO object = getOneById(id);
    if (object.getStatus().equals("accepté")) {
      return object;
    }

    return null;
  }

  /**
   * Set the status of an object to refuse.
   *
   * @param id               the id of the object
   * @param reasonForRefusal the reason for refusal
   * @param refusalDate      the refusal date
   * @return the modified object
   */
  @Override
  public ObjectDTO setStatusToRefused(int id, String reasonForRefusal, LocalDate refusalDate,
      int versionNumber) {
    String request = "UPDATE pae.objects SET state = 'refusé', status = 'refusé', "
        + "refusal_date = ?, reason_for_refusal = ?, version_number = ? WHERE id_object = ? AND version_number = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setDate(1, java.sql.Date.valueOf(refusalDate));
      ps.setString(2, reasonForRefusal);
      ps.setInt(3, versionNumber + 1);

      ps.setInt(4, id);
      ps.setInt(5, versionNumber);
      ps.executeUpdate();
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error set object refused");
      se.printStackTrace();
    }

    ObjectDTO object = getOneById(id);
    if (object.getStatus().equals("refusé")) {
      return object;
    }

    return null;

  }
}
