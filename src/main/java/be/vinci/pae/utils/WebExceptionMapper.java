package be.vinci.pae.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * WebException class that implements ExceptionMapper to manage exceptions.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Context
  private HttpHeaders headers;

  /**
   * Method that allow to make a reponse to represent an exception.
   *
   * @param exception the exception raise
   * @return a response representing the exception
   */
  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();

    if (exception instanceof WebApplicationException) {
      int status = ((WebApplicationException) exception).getResponse().getStatus();
      ObjectNode json = jsonMapper.createObjectNode();

      json.put("error", exception.getMessage());

      return Response.status(status)
          .entity(json)
          .build();
    }

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .build();
  }
}
