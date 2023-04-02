package be.vinci.pae.services.availability;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.services.DalBackendServices;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * AvailabilityDAO class that implements AvailabilityDAO interface Provide the different methods.
 */
public class AvailabilityDAOImpl implements AvailabilityDAO {

  @Inject
  private DalBackendServices dalBackendServices;

  @Inject
  private DomainFactory myDomainFactory;


  /**
   * Map a ResultSet to a AvailabilityDTO.
   *
   * @param resultSet the ResultSet
   * @return the AvailabilityDTO
   */
  public AvailabilityDTO dtoFromRS(ResultSet resultSet) {
    AvailabilityDTO availability = myDomainFactory.getAvailability();

    try {
      availability.setId(resultSet.getInt("id_availability"));
      availability.setDate(resultSet.getDate("date") == null ? null
          : resultSet.getDate("date").toLocalDate());
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error dtoFromRS");
      se.printStackTrace();
    }

    return availability;
  }

  /**
   * Get all availabilities.
   *
   * @return the list of availabilities
   */
  @Override
  public List<AvailabilityDTO> getAll() {
    String request = "SELECT * FROM pae.availabilities ORDER BY date desc";
    ArrayList<AvailabilityDTO> availabilities = new ArrayList<>();

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          availabilities.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error get all availabilities");
      se.printStackTrace();
    }

    return availabilities;
  }

  /**
   * Get the availability by the id.
   *
   * @param id of the availability
   * @return the date corresponding to the id
   */
  @Override
  public LocalDate getOneById(int id) {
    String request = "SELECT date FROM pae.availability WHERE id_availability = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getDate("date").toLocalDate();
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error get a availability by id");
      se.printStackTrace();
    }

    return null;
  }

  /**
   * Insert a new availability in the db.
   *
   * @param availability the date to insert in the db
   * @return the new availability added if succeeded, null if not
   */
  @Override
  public AvailabilityDTO insert(AvailabilityDTO availability) {
    String request = "INSERT INTO pae.availabilities VALUES (DEFAULT, ?);";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request, true)) {
      ps.setDate(1, Date.valueOf(availability.getDate()));
      ps.executeUpdate();

      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        return dtoFromRS(rs);
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "Error insert availability");
      se.printStackTrace();
    }

    return null;
  }

}
