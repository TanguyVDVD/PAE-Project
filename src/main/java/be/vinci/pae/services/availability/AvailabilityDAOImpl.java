package be.vinci.pae.services.availability;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.services.DALBackendServices;
import be.vinci.pae.utils.exceptions.DALException;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * AvailabilityDAO class that implements AvailabilityDAO interface Provide the different methods.
 */
public class AvailabilityDAOImpl implements AvailabilityDAO {

  @Inject
  private DALBackendServices dalBackendServices;

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
    } catch (SQLException e) {
      throw new DALException("Error during the mapping of the availability", e);
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
    } catch (SQLException e) {
      throw new DALException("Error during the get all availabilities", e);
    }

    return availabilities;
  }

  /**
   * Get the availability by the id.
   *
   * @param id of the availability
   * @return the availability corresponding to the id
   */
  @Override
  public AvailabilityDTO getOneById(int id) {
    String request = "SELECT * FROM pae.availabilities WHERE id_availability = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dtoFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DALException("Error during the get one availability by id", e);
    }

    return null;
  }

  /**
   * Get the availability by the date.
   *
   * @param date of the availability
   * @return the availability corresponding to the id
   */
  @Override
  public AvailabilityDTO getOneByDate(LocalDate date) {
    String request = "SELECT * FROM pae.availabilities WHERE date = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setDate(1, Date.valueOf(date));

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dtoFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DALException("Error during the get one availability by date", e);
    }

    return null;
  }

  /**
   * Insert a new availability in the db.
   *
   * @param availability the date to insert in the db
   * @return the new availability added if succeeded
   */
  @Override
  public AvailabilityDTO addOne(AvailabilityDTO availability) {
    String request = "INSERT INTO pae.availabilities VALUES (DEFAULT, ?);";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request, true)) {
      ps.setDate(1, Date.valueOf(availability.getDate()));
      ps.executeUpdate();

      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        return dtoFromRS(rs);
      } else {
        throw new DALException("Error during the add one availability");
      }
    } catch (SQLException e) {
      throw new DALException("Error during the add one availability", e);
    }
  }

  /**
   * Delete an availability in the db.
   *
   * @param id the id of the availability to delete in the db
   * @return the availability deleted if succeeded
   */
  @Override
  public AvailabilityDTO deleteOne(int id) {
    String request = "DELETE FROM pae.availabilities WHERE id_availability = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request, true)) {
      ps.setInt(1, id);
      ps.executeUpdate();

      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        return dtoFromRS(rs);
      } else {
        throw new DALException("Error during the delete one availability");
      }
    } catch (SQLException e) {
      throw new DALException("Error during the delete one availability", e);
    }
  }

  /**
   * Check if the availability is linked to objects.
   *
   * @param availability the availability to check in the db
   * @return true if linked else false
   */
  @Override
  public Boolean isLinked(AvailabilityDTO availability) {
    String request = "SELECT EXISTS( SELECT * FROM pae.objects o WHERE o.receipt_date = ? );";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setInt(1, availability.getId());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getBoolean(1);
        }
      }
    } catch (SQLException e) {
      throw new DALException("Error during the check of availability", e);
    }

    return null;
  }

}
