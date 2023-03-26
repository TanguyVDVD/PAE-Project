package be.vinci.pae.api.filters;

import be.vinci.pae.domain.user.User;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter to check if the user is authorized as Mr Riez to access the resource.
 */
@Singleton
@Provider
@AuthorizeRiez
public class AuthorizationRiezRequestFilter extends AuthorizationAdminRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    super.filter(requestContext);

    User authenticatedUser = (User) requestContext.getProperty("user");
    if (authenticatedUser.getId() != 1) {
      throw new WebApplicationException(
          "Vous n'avez pas les droits pour accéder à cette ressource. Seul Mr Riez y a droit",
          Status.FORBIDDEN);
    }
  }

}