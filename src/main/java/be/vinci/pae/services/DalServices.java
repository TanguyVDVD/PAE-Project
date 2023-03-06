package be.vinci.pae.services;

import java.sql.PreparedStatement;

/**
 * DalServices interface that provide the method to connect the db and create PreparedStatements.
 */
public interface DalServices {

  /**
   * Load the PostgresSQL driver and connect to the database.
   */
  void connectDatabase();

  /**
   * Return the PreparedStatement associated at the request.
   *
   * @param request the sql request
   * @return the PreparedStatement of this sql request
   */
  PreparedStatement getPreparedStatement(String request);
}
