package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config class.
 */
public class Config {

  private static Properties props;

  /**
   * Load the fil with the properties.
   *
   * @param file the file to load.
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      throw new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain")
              .build());
    }
  }

  /**
   * Get property.
   *
   * @param key the key of the property
   * @return the property
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * Get the property parse in int.
   *
   * @param key the key of the property
   * @return the key of the property
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * Get the property parse in bool.
   *
   * @param key the key of the property
   * @return the key of the property
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }

}
