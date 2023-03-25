package be.vinci.pae.services;

/**
 * DALServices interface that provide the method to connect the db.
 */
public interface DALServices {

  /**
   * Starts a transaction by getting a connection from the connection pool.
   */
  void startTransaction();

  /**
   * Commits the transaction by retrieving the connection from the ThreadLocal variable.
   */
  void commitTransaction();

  /**
   * Rolls back the transaction by retrieving the connection from the ThreadLocal variable.
   */
  void rollbackTransaction();


}
