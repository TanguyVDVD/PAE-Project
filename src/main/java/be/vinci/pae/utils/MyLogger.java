package be.vinci.pae.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Class that create a logger.
 */
public class MyLogger {

  private static final Logger logger = Logger.getLogger(MyLogger.class.getName());

  static {
    try {
      String configFilePath = System.getProperty("user.dir") + "/logging.properties";
      InputStream inputStream = new FileInputStream(configFilePath);
      LogManager.getLogManager().readConfiguration(inputStream);
    } catch (IOException e) {
      logger.warning(
          "Impossible to load properties from file logging.properties");
    }
  }

  /**
   * Log a new error in the Logger.
   *
   * @param level the level of the error
   * @param msg   the message of the error
   */
  public static void log(Level level, String msg) {
    logger.log(level, msg);
  }

  /**
   * LogFormatter to adapt the format of the Logger.
   */
  static class MyLogFormatter extends Formatter {

    private static final String DATE_FORMAT = "MMM d, yyyy h:mm:ss a";

    /**
     * Format the log message.
     *
     * @param record the log record to be formatted.
     * @return a String with the information according to the error
     */
    @Override
    public String format(LogRecord record) {
      return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT))
          + " " + record.getSourceClassName()
          + " " + record.getSourceMethodName()
          + "\n" + record.getLevel()
          + ": " + record.getMessage() + "\n";
    }

  }

}
