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
   * @return true if succeed false if not
   */
  @Override
  public UserDTO insert(UserDTO userDTO) {
    return null;
  }

  /**
   * Get the user by the email.
   *
   * @param email the user
   * @return the user correponding to the email
   */
  public UserDTO getOneByEmail(String email) {

    connectDatabase();

    UserDTO user = myDomainFactory.getUser();
    try {
      PreparedStatement ps = DB_CONNECTION.prepareStatement(
          "SELECT * FROM pae.users WHERE email = ?");
      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();
      System.out.println("passage");

      if (rs.next()) {
        user.setId(rs.getInt("id_user"));
        user.setLastName(rs.getString("last_name"));
        user.setFirstName(rs.getString("first_name"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoto(rs.getString("photo"));
        user.setRegisterDate(rs.getDate("register_date"));
        user.setIsHelper(rs.getBoolean("is_helper"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

    if (user.getEmail() == null)
      return null;
    return user;
  }
}

