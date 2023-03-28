package be.vinci.pae.utils;

import be.vinci.pae.utils.exceptions.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;

/**
 * WebException class that implements ExceptionMapper to manage exceptions.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  private final ObjectMapper jsonMapper = MyObjectMapper.getJsonMapper();
  @Context
  private HttpHeaders headers;

  /**
   * Method that allow to make a response to represent an exception.
   *
   * @param exception the exception raise
   * @return a response representing the exception
   */
  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();

    if (exception instanceof WebApplicationException || exception instanceof UserException) {
      int status = exception instanceof WebApplicationException
          ? ((WebApplicationException) exception).getResponse().getStatus()
          : Response.Status.BAD_REQUEST.getStatusCode();
      ObjectNode json = jsonMapper.createObjectNode();

      json.put("error", exception.getMessage());

      return Response.status(status)
          .entity(json)
          .build();
    }

    MyLogger.log(Level.SEVERE, "Exception: " + exception.getMessage());

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .build();
  }
}
