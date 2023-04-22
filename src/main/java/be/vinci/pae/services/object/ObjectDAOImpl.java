package be.vinci.pae.services.object;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.services.DalBackendServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import be.vinci.pae.services.objecttype.ObjectTypeDAO;
import be.vinci.pae.services.user.UserDAO;
import be.vinci.pae.utils.exceptions.DALException;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    } catch (SQLException e) {
      throw new DALException("Error mapping ResultSet to ObjectDTO", e);
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
    } catch (SQLException e) {
      throw new DALException("Error getting all objects", e);
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
    } catch (SQLException e) {
      throw new DALException("Error getting all objects by user", e);
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
    } catch (SQLException e) {
      throw new DALException("Error getting all objects by availability", e);
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
    } catch (SQLException e) {
      throw new DALException("Error getting all offers", e);
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
    } catch (SQLException e) {
      throw new DALException("Error getting object by id", e);
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

      if (ps.getUpdateCount() == 0) {
        if (getOneById(objectDTO.getId()) == null) {
          throw new NotFoundException("Objet non trouvé");
        } else {
          throw new SQLException(
              "Conflit de version de l'objet - V" + objectDTO.getVersionNumber());
        }
      }
    } catch (Exception e) {
      throw new DALException(e.getMessage(), e);
    }

    return getOneById(id);
  }

  /**
   * Set the status of an object to accepted.
   *
   * @param id             the id of the object
   * @param acceptanceDate the acceptance date of the object
   * @param versionNumber  the version number of the object
   * @return the modified object
   */
  @Override
  public ObjectDTO setStatusToAccepted(int id, LocalDate acceptanceDate, int versionNumber) {
    String request =
        "UPDATE pae.objects SET state = 'accepté', status = 'accepté', "
            + "acceptance_date = ?, version_number = ? "
            + "WHERE id_object = ? AND version_number = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setDate(1, java.sql.Date.valueOf(acceptanceDate));
      ps.setInt(2, versionNumber + 1);
      ps.setInt(3, id);
      ps.setInt(4, versionNumber);

      ps.executeUpdate();

      if (ps.getUpdateCount() == 0) {
        if (getOneById(id) == null) {
          throw new NotFoundException("Objet non trouvé");
        } else {
          throw new SQLException(
              "Conflit de version de l'objet - V" + versionNumber);
        }
      }

    } catch (Exception e) {
      throw new DALException(e.getMessage(), e);
    }

    ObjectDTO object = getOneById(id);
    if (object.getStatus().equals("accepté")) {
      return object;
    }

    return null;
  }

  /**
   * Set the status of an object to "refused".
   *
   * @param id               the id of the object
   * @param reasonForRefusal the reason for refusal
   * @param refusalDate      the refusal date
   * @param versionNumber    the version number of the object
   * @return the modified object
   */
  @Override
  public ObjectDTO setStatusToRefused(int id, String reasonForRefusal, LocalDate refusalDate,
      int versionNumber) {
    String request = "UPDATE pae.objects SET state = 'refusé', status = 'refusé', "
        + "refusal_date = ?, reason_for_refusal = ?, version_number = ? "
        + "WHERE id_object = ? AND version_number = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setDate(1, java.sql.Date.valueOf(refusalDate));

      ps.setString(2, reasonForRefusal);
      ps.setInt(3, versionNumber + 1);

      ps.setInt(4, id);
      ps.setInt(5, versionNumber);

      ps.executeUpdate();

      if (ps.getUpdateCount() == 0) {
        if (getOneById(id) == null) {
          throw new NotFoundException("Objet non trouvé");
        } else {
          throw new SQLException(
              "Conflit de version de l'objet - V" + versionNumber);
        }
      }

    } catch (Exception e) {
      throw new DALException(e.getMessage(), e);
    }

    ObjectDTO object = getOneById(id);
    if (object.getStatus().equals("refusé")) {
      return object;
    }

    return null;

  }

  /**
   * Insert a new object in the db.
   *
   * @param objectDTO the object to insert in the db
   * @return the object inserted in the db
   */
  @Override
  public ObjectDTO insert(ObjectDTO objectDTO) {
    String request = "INSERT INTO pae.objects VALUES "
        + "(DEFAULT, 0, ?, ?, null, 'proposé', null, null, false, ?, "
        + "null, null, null, null, null, null, null, ?, ?, ?, ?);";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request, true)) {
      ps.setString(1, objectDTO.getDescription());
      ps.setString(2, objectDTO.getTimeSlot());
      ps.setDate(3, java.sql.Date.valueOf(objectDTO.getOfferDate()));
      ps.setString(4, objectDTO.getPhoneNumber());
      if (objectDTO.getUser() != null) {
        ps.setInt(5, objectDTO.getUser().getId());
      } else {
        ps.setNull(5, Types.INTEGER);
      }
      ps.setInt(6, myAvailabilityDao.getOneByDate(objectDTO.getReceiptDate()).getId());
      ps.setInt(7, myObjectTypeDAO.getIdByLabel(objectDTO.getObjectType()));
      ps.executeUpdate();

      // Get the id of the new object
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        return getOneById(rs.getInt(1));
      }
    } catch (Exception e) {
      throw new DALException("Error during the insertion of the object", e);
    }

    return null;
  }
}
