package be.vinci.pae.api.filters;

import be.vinci.pae.domain.user.User;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter to check if the user is authorized as an admin to access the resource.
 */
@Singleton
@Provider
@AuthorizeAdmin
public class AuthorizationAdminRequestFilter extends AuthorizationRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    super.filter(requestContext);

    User authenticatedUser = (User) requestContext.getProperty("user");
    if (!authenticatedUser.isHelper()) {
      throw new WebApplicationException("You don't have access to this resource", Status.FORBIDDEN);
    }
  }

}