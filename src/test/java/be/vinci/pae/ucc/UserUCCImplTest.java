package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserImpl;
import be.vinci.pae.services.user.UserDAO;
import be.vinci.pae.services.user.UserDAOImpl;
import be.vinci.pae.ucc.user.UserUCC;
import be.vinci.pae.ucc.user.UserUCCImpl;
import jakarta.inject.Singleton;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for UserUCCImpl.
 */
class UserUCCImplTest {

  /**
   * Mocked userDAO.
   */
  private static UserDAO userDAO;

  /**
   * UserUCC to test.
   */
  private static UserUCC userUCC;

  /**
   * Set up the test.
   */
  @BeforeAll
  static void setUp() {
    userDAO = Mockito.mock(UserDAOImpl.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);

        bind(userDAO).to(UserDAO.class);
      }
    });

    userUCC = locator.getService(UserUCC.class);
  }
  
  @Test
  @DisplayName("Login with correct email and password")
  void loginWithCorrectEmailAndPassword() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneByEmail("test@example.com")).thenReturn(user);
    Mockito.when(user.isPasswordCorrect("password")).thenReturn(true);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertEquals(user, userDTO);
  }

  @Test
  @DisplayName("Login with incorrect email")
  void loginWithIncorrectEmail() {
    Mockito.when(userDAO.getOneByEmail("test@example.com")).thenReturn(null);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertNull(userDTO);
  }

  @Test
  @DisplayName("Login with incorrect password")
  void loginWithIncorrectPassword() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneByEmail("test@example.com")).thenReturn(user);
    Mockito.when(user.isPasswordCorrect("password")).thenReturn(false);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertNull(userDTO);
  }

  @Test
  @DisplayName("Login with null email")
  void loginWithNullEmail() {
    UserDTO userDTO = userUCC.login(null, "password");
    assertNull(userDTO);
  }

  @Test
  @DisplayName("Login with null password")
  void loginWithNullPassword() {
    UserDTO userDTO = userUCC.login("test@example.com", null);
    assertNull(userDTO);
  }
}