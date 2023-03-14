package be.vinci.pae.services.availability;

import be.vinci.pae.services.DALServices;
import jakarta.inject.Inject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * AvailabilityDAO class that implements AvailabilityDAO interface Provide the different methods.
 */
public class AvailabilityDAOImpl implements AvailabilityDAO {

  @Inject
  private DALServices myDALServices;

  /**
   * Get the availability by the id.
   *
   * @param id of the availability
   * @return the date corresponding to the id
   */
  @Override
  public Date getOneById(int id) {
    String request = "SELECT date FROM pae.availability WHERE id_availability = ?;";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getDate("date");
        }
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return null;
  }

}
