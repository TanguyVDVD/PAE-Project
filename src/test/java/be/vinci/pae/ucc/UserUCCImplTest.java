package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.domain.UserImpl;
import be.vinci.pae.services.UserDS;
import be.vinci.pae.services.UserDSImpl;
import jakarta.inject.Singleton;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserUCCImplTest {

  static ServiceLocator locator;
  private static UserDS userDS;
  private static UserUCC userUCC;

  @BeforeAll
  static void setUp() {
    userDS = Mockito.mock(UserDSImpl.class);

    locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);

        bind(userDS).to(UserDS.class);
      }
    });

    userUCC = locator.getService(UserUCC.class);
  }

  @Test
  void loginWithCorrectEmailAndPassword() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(userDS.getOneByEmail("test@example.com")).thenReturn(user);
    Mockito.when(user.isPasswordCorrect("password")).thenReturn(true);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertEquals(user, userDTO);
  }

  @Test
  void loginWithIncorrectEmail() {
    Mockito.when(userDS.getOneByEmail("test@example.com")).thenReturn(null);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertNull(userDTO);
  }

  @Test
  void loginWithIncorrectPassword() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(userDS.getOneByEmail("test@example.com")).thenReturn(user);
    Mockito.when(user.isPasswordCorrect("password")).thenReturn(false);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertNull(userDTO);
  }

  @Test
  void loginWithNullEmail() {
    UserDTO userDTO = userUCC.login(null, "password");
    assertNull(userDTO);
  }

  @Test
  void loginWithNullPassword() {
    UserDTO userDTO = userUCC.login("test@example.com", null);
    assertNull(userDTO);
  }
}