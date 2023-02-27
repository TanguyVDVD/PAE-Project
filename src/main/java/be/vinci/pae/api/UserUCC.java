package be.vinci.pae.api;

import be.vinci.pae.domain.UserDTO;
import jakarta.ws.rs.core.Context;
import org.glassfish.jersey.server.ContainerRequest;

public interface UserUCC {

  UserDTO login(UserDTO userDTO, @Context ContainerRequest request);

  UserDTO logout(UserDTO userDTO);

}
