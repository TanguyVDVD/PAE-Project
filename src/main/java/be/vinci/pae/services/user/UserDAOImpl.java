package be.vinci.pae.services.user;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.DalBackendServices;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;


/**
 * UserDAOImpl class that implements UserDAO interface Provide the different methods.
 */
public class UserDAOImpl implements UserDAO {

  @Inject
  private DomainFactory myDomainFactory;

  @Inject
  private DalBackendServices dalBackendServices;

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
      user.setVersionNumber(resultSet.getInt("version_number"));
      user.setLastName(resultSet.getString("last_name"));
      user.setFirstName(resultSet.getString("first_name"));
      user.setPhoneNumber(resultSet.getString("phone_number"));
      user.setEmail(resultSet.getString("email"));
      user.setPassword(resultSet.getString("password"));
      user.setPhoto(resultSet.getBoolean("photo"));
      user.setRegisterDate(resultSet.getDate("register_date").toLocalDate());
      user.setRole(resultSet.getString("role"));
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "error dto from RS USer");
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
    String request = "INSERT INTO" + " pae.users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request, true)) {
      ps.setInt(1, userDTO.getVersionNumber());
      ps.setString(2, userDTO.getLastName());
      ps.setString(3, userDTO.getFirstName());
      ps.setString(4, userDTO.getPhoneNumber());
      ps.setString(5, userDTO.getEmail());
      ps.setString(6, userDTO.getPassword());
      ps.setBoolean(7, userDTO.getPhoto());

      ps.setDate(8, java.sql.Date.valueOf(userDTO.getRegisterDate()));
      ps.setString(9, userDTO.getRole());
      ps.executeUpdate();

      // Get the id of the new user
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "error insert user");
      se.printStackTrace();
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

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setString(1, email);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dtoFromRS(rs);
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "error get a user by email");
      se.printStackTrace();
    }

    return null;
  }

  @Override
  public UserDTO getOneByPhoneNumber(String phoneNumber) {
    String request = "SELECT * FROM pae.users WHERE phone_number = ?";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setString(1, phoneNumber);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dtoFromRS(rs);
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "get a user by phone number");
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

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dtoFromRS(rs);
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "get a user by id");
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

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setString(1, query == null ? "" : query.toLowerCase());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          users.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "get all user");
      se.printStackTrace();
    }

    return users;
  }

  /**
   * Update a user.
   *
   * @param userDTO the user to update
   * @return true if succeeded, false if not
   */
  @Override
  public boolean update(UserDTO userDTO) {
    // Only update the fields that are not null
    Map<String, Object> fields = new HashMap<>();
    fields.put("version_number", userDTO.getVersionNumber() + 1);
    if (userDTO.getLastName() != null) {
      fields.put("last_name", userDTO.getLastName());
    }
    if (userDTO.getFirstName() != null) {
      fields.put("first_name", userDTO.getFirstName());
    }
    if (userDTO.getPhoneNumber() != null) {
      fields.put("phone_number", userDTO.getPhoneNumber());
    }
    if (userDTO.getEmail() != null) {
      fields.put("email", userDTO.getEmail());
    }
    if (userDTO.getPassword() != null) {
      fields.put("password", userDTO.getPassword());
    }
    if (userDTO.getPhoto() != null) {
      fields.put("photo", userDTO.getPhoto());
    }
    if (userDTO.getRegisterDate() != null) {
      fields.put("register_date", userDTO.getRegisterDate());
    }
    if (userDTO.getRole() == null
        || userDTO.getRole().equals("aidant")
        || userDTO.getRole().equals("responsable")) {
      fields.put("role", userDTO.getRole());
    }

    if (fields.isEmpty()) {
      return false;
    }

    String request = "UPDATE pae.users SET " + fields.keySet().stream()
        .map(key -> key + " = ?").collect(Collectors.joining(", "))
        + " WHERE id_user = ? AND version_number = ?";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      int i = 1;
      for (Object value : fields.values()) {
        ps.setObject(i++, value);
      }
      ps.setInt(i, userDTO.getId());
      ps.setInt(i + 1, userDTO.getVersionNumber());

      ps.executeUpdate();

      if (ps.getUpdateCount() == 0) {
        if (getOneById(userDTO.getId()) == null) {
          throw new WebApplicationException("Utilisateur non trouvé", 404);
        } else {
          throw new WebApplicationException(
              "Conflit de version d'utilisateur - v" + userDTO.getVersionNumber(), 409);
        }
      }

      return true;
    } catch (SQLException se) {
      MyLogger.log(Level.INFO, "error update user");
      se.printStackTrace();
    }

    return false;
  }
}

