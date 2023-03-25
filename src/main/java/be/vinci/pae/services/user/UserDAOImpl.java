package be.vinci.pae.services.user;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.DALServices;
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
 * UserDAOImpl class that implements UserDAO interface Provide the different methods.
 */
public class UserDAOImpl implements UserDAO {

  @Inject
  private DomainFactory myDomainFactory;

  @Inject
  private DALServices myDALServices;

  /**
   * Map a ResultSet to a UserDTO.
   *
   * @param resultSet the ResultSet
   * @return the UserDTO
   */
  public UserDTO dtoFromRS(ResultSet resultSet) {
    UserDTO user = myDomainFactory.getUser();

    try {
      user.setId(resultSet.getInt("id_user"));
      user.setLastName(resultSet.getString("last_name"));
      user.setFirstName(resultSet.getString("first_name"));
      user.setPhoneNumber(resultSet.getString("phone_number"));
      user.setEmail(resultSet.getString("email"));
      user.setPassword(resultSet.getString("password"));
      user.setPhoto(resultSet.getBoolean("photo"));
      user.setRegisterDate(resultSet.getString("register_date"));
      user.setIsHelper(resultSet.getBoolean("is_helper"));
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return user;
  }

  /**
   * Insert a new user in the db.
   *
   * @param userDTO the user to insert in the db
   * @return id of the new user if succeeded, -1 if not
   */
  @Override
  public int insert(UserDTO userDTO) {
    String request = "INSERT INTO" + " pae.users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?);";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request, true)) {
      ps.setString(1, userDTO.getLastName());
      ps.setString(2, userDTO.getFirstName());
      ps.setString(3, userDTO.getPhoneNumber());
      ps.setString(4, userDTO.getEmail());
      ps.setString(5, userDTO.getPassword());
      ps.setBoolean(6, userDTO.getPhoto());

      SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
      Date parsed = format.parse(userDTO.getRegisterDate());
      java.sql.Date sql = new java.sql.Date(parsed.getTime());

      ps.setDate(7, sql);
      ps.setBoolean(8, userDTO.isHelper());
      ps.executeUpdate();

      // Get the id of the new user
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    return -1;
  }

  /**
   * Get the user by the email.
   *
   * @param email the user
   * @return the user corresponding to the email
   */
  public UserDTO getOneByEmail(String email) {
    String request = "SELECT * FROM pae.users WHERE email = ?";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setString(1, email);

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

  @Override
  public UserDTO getOneByPhoneNumber(String phoneNumber) {
    String request = "SELECT * FROM pae.users WHERE phone_number = ?";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setString(1, phoneNumber);

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
   * Get the user by the id.
   *
   * @param id of the user
   * @return the user corresponding to the id
   */
  public UserDTO getOneById(int id) {
    String request = "SELECT * FROM pae.users WHERE id_user = ?";

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
   * Get all the users.
   *
   * @param query query to filter users
   * @return the list of all users
   */
  public List<UserDTO> getAll(String query) {
    String request = "SELECT * FROM pae.users WHERE LOWER(last_name || ' ' || first_name) "
        + "LIKE CONCAT('%', ?, '%') ORDER BY id_user";
    ArrayList<UserDTO> users = new ArrayList<>();

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setString(1, query == null ? "" : query.toLowerCase());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          users.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return users;
  }
}

