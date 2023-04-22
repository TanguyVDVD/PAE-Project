package be.vinci.pae.api.filters;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.ucc.user.UserUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter to check if the user is connected.
 */
@Singleton
@Provider
@GetUser
public class GetUserRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  @Inject
  private UserUCC userUCC;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");

    if (token != null) {
      DecodedJWT decodedToken;

      try {
        decodedToken = this.jwtVerifier.verify(token);
        User authenticatedUser = (User) userUCC.getUserById(decodedToken.getClaim("user").asInt());
        if (authenticatedUser != null) {
          requestContext.setProperty("user", authenticatedUser);
        }
      } catch (Exception ignored) {
        // We can ignore the exception
      }
    }
  }

}