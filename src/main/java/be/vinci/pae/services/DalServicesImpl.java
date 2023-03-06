package be.vinci.pae.services;

import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DalServicesImpl implements DalServices {

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

  public DalServicesImpl() {
    connectDatabase();
  }

  /**
   * Load the PostgresSQL driver and connect to the database.
   */
  @Override
  public void connectDatabase() {
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
   * Return the PreparedStatement associated at the request.
   *
   * @param request the sql request
   * @return the PreparedStatement of this sql request
   */
  @Override
  public PreparedStatement getPreparedStatement(String request) {
    try {
      return DB_CONNECTION.prepareStatement(request);
    } catch (SQLException se) {
      se.printStackTrace();
    }
    return null;
  }
}
