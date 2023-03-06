package be.vinci.pae.services;

import static be.vinci.pae.services.UserDSImpl.DB_CONNECTION;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DalServicesImpl implements DalServices {

  /**
   * Load the PostgresSQL driver and connect to the database.
   */
  @Override
  public void connectDatabase() {

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
