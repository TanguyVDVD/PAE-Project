package be.vinci.pae.services;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDS class that implements UserDs interface Provide the different methods.
 */
public class UserDSImpl implements UserDS {

  // Database url
  public static final String DATABASE_URL;
  // User connecting to the database
  public static final String DATABASE_USER;
  // User's password
  public static final String DATABASE_PASSWORD;

  // Connection to the database
  public static Connection DB_CONNECTION = null;

  // Loading properties
  static {
    Config.load("dev.properties");
    DATABASE_URL = Config.getProperty("DatabaseUrl");
    DATABASE_USER = Config.getProperty("DatabaseUser");
    DATABASE_PASSWORD = Config.getProperty("DatabasePassword");
  }

  @Inject
  private DomainFactory myDomainFactory;

  /**
   * Load the PostgresSQL driver and connect to the database.
   */
  public static void connectDatabase() {
    // Load the PostgresSQL driver
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Missing PostgreSQL driver!");
      System.exit(1);
    }

    // Connection to the database
    try {
      DB_CONNECTION = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    } catch (SQLException e) {
      System.out.println("Unable to reach the server!");
      System.exit(1);
    }
  }

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

    try (PreparedStatement ps = DB_CONNECTION.prepareStatement(
        "SELECT * FROM pae.users WHERE email = ?;")) {
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
   * Get the user by the email.
   *
   * @param id the user
   * @return the user corresponding to the email
   */
  public UserDTO getOneById(int id) {
    UserDTO user = myDomainFactory.getUser();

    try (PreparedStatement ps = DB_CONNECTION.prepareStatement(
        "SELECT * FROM pae.users WHERE id_user = ?;")) {
      ps.setInt(1, id);

      user = setUser(ps, user);

    } catch (SQLException se) {
      se.printStackTrace();
    }

    if (user.getId() < 1) {
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
      if (resultSet.getFetchSize() != 1) {
        return user;
      }

      while (resultSet.next()) {
        user.setId(resultSet.getInt("id_user"));
        user.setLastName(resultSet.getString("last_name"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setPhoto(resultSet.getString("photo"));
        user.setRegisterDate(resultSet.getDate("register_date"));
        user.setIsHelper(resultSet.getBoolean("is_helper"));
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return user;
  }
}

