package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.notification.NotificationDTO;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.ucc.notification.NotificationUCC;
import be.vinci.pae.utils.MyObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * NotificationResource class.
 */
@Singleton
@Path("/notifications")
public class NotificationResource {

  private final ObjectMapper jsonMapper = MyObjectMapper.getJsonMapper();

  @Inject
  private DomainFactory myDomainFactory;
  @Inject
  private NotificationUCC myNotificationUCC;

  /**
   * Get a list of all notification of a user.
   *
   * @param id the id of the user
   * @return a list of notifications
   */
  @GET
  @Path("/user/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getNotificationByUser(@PathParam("id") int id) {

    if (id <= 0) {
      throw new WebApplicationException("Impossible de récupérer les notifications, problème d'ID",
          Response.Status.BAD_REQUEST);
    }

    return jsonMapper.valueToTree(myNotificationUCC.getNotificationsByUserID(id));
  }

  /**
   * Mark a notification as read.
   *
   * @param request the request
   * @param id      the id of the notif
   * @return a NotificationDTO
   */
  @PATCH
  @Path("/{id}/read")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public NotificationDTO markNotificationAsRead(@Context ContainerRequest request,
      @PathParam("id") int id) {

    if (id <= 0) {
      throw new WebApplicationException("Impossible de récupérer les notifications, problème d'ID",
          Response.Status.BAD_REQUEST);
    }

    User user = (User) request.getProperty("user");

    NotificationDTO notificationDTO = myDomainFactory.getNotification();

    notificationDTO.setId(id);
    notificationDTO.setIdUser(user.getId());

    return myNotificationUCC.markANotificationAsRead(notificationDTO);

  }


}
