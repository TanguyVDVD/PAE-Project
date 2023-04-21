package be.vinci.pae.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * NotificationResource class.
 */
@Singleton
@Path("/notifications")
public class NotificationResource {

  /**
   * Get a list of all notification of a user.
   *
   * @return a list of notifications
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getNotificationByUser() {
    return null;
  }

}
