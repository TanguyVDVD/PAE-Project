package be.vinci.pae.services;

import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * DALServicesImpl class that implements DALServices interface Provide the different methods.
 */
public class DALServicesImpl implements DALServices {

  /**
   * Connection to the database.
   */
  public Connection dbConnection = null;


  /**
   * Constructor of DALServicesImpl.
   */
  public DALServicesImpl() {
    connectDatabase();
  }

  /**
   * Load the PostgresSQL driver and connect to the database.
   */
  @Override
  public void connectDatabase() {

    try (BasicDataSource ds = new BasicDataSource()) {

      ds.setUrl(Config.getProperty("DatabaseUrl"));
      ds.setUsername(Config.getProperty("DatabaseUser"));
      ds.setPassword(Config.getProperty("DatabasePassword"));
      ds.setDriverClassName("org.postgresql.Driver");

      // Connection to the database
      try {
        dbConnection = ds.getConnection();
      } catch (SQLException e) {
        System.out.println("Unable to reach the server!");
        System.exit(1);
      }
    } catch (SQLException e) {
      System.out.println("Impossible to create DataSource");
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
      return dbConnection.prepareStatement(request);
    } catch (SQLException se) {
      se.printStackTrace();
    }
    return null;
  }
}
