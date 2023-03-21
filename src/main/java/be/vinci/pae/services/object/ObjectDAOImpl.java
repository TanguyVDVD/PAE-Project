package be.vinci.pae.services.object;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import be.vinci.pae.services.objecttype.ObjectTypeDAO;
import be.vinci.pae.services.user.UserDAO;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ObjectDAO class that implements ObjectDAO interface Provide the different methods.
 */
public class ObjectDAOImpl implements ObjectDAO {

  @Inject
  private DomainFactory myDomainFactory;

  @Inject
  private DALServices myDALServices;

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
      object.setDescription(resultSet.getString("description"));
      object.setPhoto(resultSet.getBoolean("photo"));
      object.setPhoneNumber(resultSet.getString("phone_number"));
      object.setVisibility(resultSet.getBoolean("is_visible"));
      object.setPrice(resultSet.getDouble("price"));
      object.setState(resultSet.getString("state"));
      object.setAcceptanceDate(resultSet.getDate("acceptance_date") == null ? ""
          : resultSet.getDate("acceptance_date").toString());
      object.setDepositDate(resultSet.getDate("deposit_date") == null ? ""
          : resultSet.getDate("deposit_date").toString());
      object.setSellingDate(resultSet.getDate("selling_date") == null ? ""
          : resultSet.getDate("selling_date").toString());
      object.setWithdrawalDate(resultSet.getDate("withdrawal_date") == null ? ""
          : resultSet.getDate("withdrawal_date").toString());
      object.setTimeSlot(resultSet.getString("time_slot"));
      object.setStatus(resultSet.getString("status"));
      object.setReasonForRefusal(resultSet.getString("reason_for_refusal"));
      object.setPhoneNumber(resultSet.getString("phone_number"));
      object.setPickupDate(
          myAvailabilityDao.getOneById(resultSet.getInt("pickup_date")) == null ? ""
              : myAvailabilityDao.getOneById(resultSet.getInt("pickup_date")).toString());
      object.setUser(myUserDao.getOneById(resultSet.getInt("id_user")));
      object.setObjectType(myObjectTypeDAO.getOneById(resultSet.getInt("id_object_type")));

    } catch (SQLException se) {
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

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setString(1, query == null ? "" : query.toLowerCase());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objects.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
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

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dtoFromRS(rs);
        }
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return null;
  }

  /**
   * Set the status of an object to accepted.
   *
   * @param id the id of the object
   * @return the modified object
   */
  @Override
  public ObjectDTO setStatusToAccepted(int id) {
    String request = "UPDATE pae.objects SET status = 'accepté' WHERE id_object = ?;";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    } catch (SQLException se) {
      se.printStackTrace();
    }

    ObjectDTO object = getOneById(id);
    if (object.getStatus().equals("accepté")) {
      return object;
    }

    return null;
  }

  /**
   * Change the state of the object with the given ID to sold with the date.
   *
   * @param id   the ID of the object to update
   * @param date the date of the sale
   * @return an ObjectDTO representing the updated object
   */
  public ObjectDTO setStateToSold(int id, String date) {
    String request = "UPDATE pae.objects SET state = 'vendu', "
        + "selling_date = ? WHERE id_object = ?;";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {

      SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
      Date parsed = format.parse(date);
      java.sql.Date sql = new java.sql.Date(parsed.getTime());

      ps.setDate(1, sql);
      ps.setInt(2, id);
      ps.executeUpdate();
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    return getOneById(id);

  }
}
