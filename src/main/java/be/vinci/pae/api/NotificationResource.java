package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.ucc.notification.NotificationUCC;
import be.vinci.pae.utils.MyObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * NotificationResource class.
 */
@Singleton
@Path("/notifications")
public class NotificationResource {

  private final ObjectMapper jsonMapper = MyObjectMapper.getJsonMapper();

  @Inject
  private NotificationUCC myNotificationUCC;

  /**
   * Get a list of all notification of a user.
   *
   * @return a list of notifications
   */
  @GET
  @Path("/user/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getNotificationByUser(@Context ContainerRequest request,
      @PathParam("id") int id) {
    User user = (User) request.getProperty("user");

    System.out.println(id);
    return jsonMapper.valueToTree(myNotificationUCC.getNotificationsByUserID(id));
  }

}
