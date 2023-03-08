package be.vinci.pae.services.user;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.DALServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO class that implements UserDs interface Provide the different methods.
 */
public class UserDAOImpl implements UserDAO {

  @Inject
  private DomainFactory myDomainFactory;

  @Inject
  private DALServices myDALServices;

  /**
   * Insert a new user in the db.
   *
   * @param userDTO the user to insert in the db
   * @return true if succeeds, else false
   */
  @Override
  public boolean insert(UserDTO userDTO) {
    return false;
  }

  /**
   * Get the user by the email.
   *
   * @param email the user
   * @return the user corresponding to the email
   */
  public UserDTO getOneByEmail(String email) {
    UserDTO user = myDomainFactory.getUser();
    String request = "SELECT * FROM pae.users WHERE email = ?;";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setString(1, email);
      user = setUser(ps, user);
    } catch (SQLException se) {
      se.printStackTrace();
    }

    if (user.getEmail() == null) {
      return null;
    }

    return user;
  }

  /**
   * Get the user by the id.
   *
   * @param id the user
   * @return the user corresponding to the email
   */
  public UserDTO getOneById(int id) {
    UserDTO user = myDomainFactory.getUser();
    String request = "SELECT * FROM pae.users WHERE id_user = ?;";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setInt(1, id);
      user = setUser(ps, user);
    } catch (SQLException se) {
      se.printStackTrace();
    }

    if (user.getEmail() == null) {
      return null;
    }

    return user;
  }

  /**
   * Set up the user.
   *
   * @param preparedStatement the PreparedStatement
   * @param user              the user to set up
   */
  public UserDTO setUser(PreparedStatement preparedStatement, UserDTO user) {
    try (ResultSet resultSet = preparedStatement.executeQuery()) {
      if (!resultSet.next()) {
        return user;
      }

      user.setId(resultSet.getInt("id_user"));
      user.setLastName(resultSet.getString("last_name"));
      user.setFirstName(resultSet.getString("first_name"));
      user.setPhoneNumber(resultSet.getString("phone_number"));
      user.setEmail(resultSet.getString("email"));
      user.setPassword(resultSet.getString("password"));
      user.setPhoto(resultSet.getString("photo"));
      user.setRegisterDate(resultSet.getDate("register_date"));
      user.setIsHelper(resultSet.getBoolean("is_helper"));
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return user;
  }

  /**
   * Get all the users.
   *
   * @param query query to filter users
   * @return the list of all users
   */
  public List<UserDTO> getAll(String query) {
    String request = "SELECT * FROM pae.users WHERE LOWER(last_name || ' ' || first_name) LIKE CONCAT('%', ?, '%') ORDER BY id_user";
    ArrayList<UserDTO> users = new ArrayList<UserDTO>();

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setString(1, query == null ? "" : query.toLowerCase());

      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          UserDTO user = myDomainFactory.getUser();
          user.setId(resultSet.getInt("id_user"));
          user.setLastName(resultSet.getString("last_name"));
          user.setFirstName(resultSet.getString("first_name"));
          user.setPhoneNumber(resultSet.getString("phone_number"));
          user.setEmail(resultSet.getString("email"));
          user.setPassword(resultSet.getString("password"));
          user.setPhoto(resultSet.getString("photo"));
          user.setRegisterDate(resultSet.getDate("register_date"));
          user.setIsHelper(resultSet.getBoolean("is_helper"));
          users.add(user);
        }
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return users;
  }
}

