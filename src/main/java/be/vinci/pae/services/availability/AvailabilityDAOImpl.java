package be.vinci.pae.services.availability;

import be.vinci.pae.services.DalBackendServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * AvailabilityDAO class that implements AvailabilityDAO interface Provide the different methods.
 */
public class AvailabilityDAOImpl implements AvailabilityDAO {

  @Inject
  private DalBackendServices dalBackendServices;

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
      se.printStackTrace();
    }

    return null;
  }

}
