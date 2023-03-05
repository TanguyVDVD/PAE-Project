package be.vinci.pae.main;

import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.WebExceptionMapper;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  // Base URI the Grizzly HTTP server will listen on
  public static final String BASE_URI;
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
    BASE_URI = Config.getProperty("BaseUri");
    DATABASE_URL = Config.getProperty("DatabaseUrl");
    DATABASE_USER = Config.getProperty("DatabaseUser");
    DATABASE_PASSWORD = Config.getProperty("DatabasePassword");
  }

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in vinci.be package
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api")
        .register(ApplicationBinder.class)
        .register(WebExceptionMapper.class);

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Load the PostgresSQL driver and connect to the database
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
   * Main method.
   *
   * @param args main args String[]
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with WADL available at "
        + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
    connectDatabase();
    System.in.read();
    server.stop();
  }

}
