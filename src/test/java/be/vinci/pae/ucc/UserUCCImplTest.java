package be.vinci.pae.ucc;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserUCCImplTest {

  private UserUCC userUCC;
  private UserDTO userDTO;
  private User user;
  private String correctEmail;
  private String correctPassword;

  @BeforeEach
  void setUp() {
    userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getEmail()).thenReturn(correctEmail);
    Mockito.when(userDTO.getPassword()).thenReturn(user.hashPassword(correctPassword));
  }

  @Test
  void login() {
  }
}