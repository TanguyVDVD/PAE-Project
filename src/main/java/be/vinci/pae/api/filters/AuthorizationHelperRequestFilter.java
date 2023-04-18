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
@AuthorizeHelper
public class AuthorizationHelperRequestFilter extends AuthorizationRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    super.filter(requestContext);

    User authenticatedUser = (User) requestContext.getProperty("user");
    if (!authenticatedUser.getRole().equals("aidant")
        && !authenticatedUser.getRole().equals("responsable")) {
      throw new WebApplicationException(
          "Vous n'avez pas les droits pour accéder à cette ressource : "
              + "vous devez être aidant ou responsable.",
          Status.FORBIDDEN);
    }
  }

}