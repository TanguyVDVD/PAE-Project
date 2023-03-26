package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserImpl;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.user.UserDAO;
import be.vinci.pae.services.user.UserDAOImpl;
import be.vinci.pae.ucc.user.UserUCC;
import be.vinci.pae.ucc.user.UserUCCImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
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

    DALServices myDalServices = Mockito.mock(DALServices.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);

        bind(userDAO).to(UserDAO.class);
        bind(myDalServices).to(DALServices.class);
      }
    });

    userUCC = locator.getService(UserUCC.class);
  }

  /**
   * Set up mocked objects.
   */
  @BeforeEach
  void setUpMockedObjects() {
    Mockito.reset(userDAO);
  }

  @DisplayName("Login with correct email and password")
  @Test
  void loginWithCorrectEmailAndPassword() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneByEmail("test@example.com")).thenReturn(user);
    Mockito.when(user.isPasswordCorrect("password")).thenReturn(true);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertAll(
        () -> assertNotNull(userDTO, "Login have not return a user"),
        () -> assertEquals(user, userDTO, "Login have not return the same user")
    );

  }

  @DisplayName("Login with incorrect email")
  @Test
  void loginWithIncorrectEmail() {
    Mockito.when(userDAO.getOneByEmail("test@example.com")).thenReturn(null);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertNull(userDTO, "Login returned a user although the email is incorrect");
  }

  @DisplayName("Login with incorrect password")
  @Test
  void loginWithIncorrectPassword() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneByEmail("test@example.com")).thenReturn(user);
    Mockito.when(user.isPasswordCorrect("password")).thenReturn(false);

    UserDTO userDTO = userUCC.login("test@example.com", "password");
    assertNull(userDTO, "Login returned a user although the password is incorrect");
  }

  @DisplayName("Login with null email")
  @Test
  void loginWithNullEmail() {
    UserDTO userDTO = userUCC.login(null, "password");
    assertNull(userDTO, "Login returned a user although the email is null");
  }

  @DisplayName("Login with null password")
  @Test
  void loginWithNullPassword() {
    UserDTO userDTO = userUCC.login("test@example.com", null);
    assertNull(userDTO, "Login returned a user although the password is null");
  }

  @DisplayName("Get list of all users")
  @Test
  void getAllUsers() {
    List<UserDTO> users = new ArrayList<>();
    users.add(Mockito.mock(UserImpl.class));
    Mockito.when(userDAO.getAll("")).thenReturn(users);

    assertEquals(users, userUCC.getUsers(""), "getAllUsers() did not return the correct list");
  }

  @DisplayName("Get a user by id")
  @Test
  void getUserById() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(user);

    assertEquals(user, userUCC.getUserById(1), "getUserById() did not return the correct user");
  }

  @DisplayName("Update user without verifying the current password, email, or phone number")
  @Test
  void updateUserWithoutVerifyingCurrentPassword() {
    // user = pojo passed by the client
    // userDB = data from the database
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);

    // Update user
    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertEquals(userDB, userUCC.updateUser(user), "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with correct current password")
  @Test
  void updateUserWithCorrectCurrentPassword() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);
    Mockito.when(userDB.isPasswordCorrect("password")).thenReturn(true);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertEquals(userDB, userUCC.updateUser(user, "password"),
        "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with incorrect current password")
  @Test
  void updateUserWithIncorrectCurrentPassword() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);
    Mockito.when(userDB.isPasswordCorrect("password")).thenReturn(false);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user, "password"),
        "updateUser() did not throw an exception although the current password is incorrect");
  }

  @DisplayName("Update user with valid email")
  @Test
  void updateUserWithValidEmail() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getEmail()).thenReturn("test@example.com");

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertEquals(userDB, userUCC.updateUser(user),
        "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with invalid email")
  @Test
  void updateUserWithInvalidEmail() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getEmail()).thenReturn("test");

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the email is invalid");
  }

  @DisplayName("Update user with valid phone number")
  @Test
  void updateUserWithValidPhoneNumber() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getPhoneNumber()).thenReturn("0493111111");

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertEquals(userDB, userUCC.updateUser(user),
        "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with invalid phone number")
  @Test
  void updateUserWithInvalidPhoneNumber() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getPhoneNumber()).thenReturn("0493");

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the phone number is invalid");
  }

  @DisplayName("Update user with email already in use")
  @Test
  void updateUserWithExistingEmail() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getEmail()).thenReturn("test@example.com");

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);
    Mockito.when(userDAO.getOneByEmail("test@example.com")).thenReturn(userDB);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the email is already in use");
  }

  @DisplayName("Update user with phone number already in use")
  @Test
  void updateUserWithExistingPhoneNumber() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getPhoneNumber()).thenReturn("0493111111");

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);
    Mockito.when(userDAO.getOneByPhoneNumber("0493111111")).thenReturn(userDB);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the phone number is already in use");
  }

  @DisplayName("Update user with new password")
  @Test
  void updateUserWithNewPassword() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getPassword()).thenReturn("newPassword");

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    try (MockedStatic<User> userMockedStatic = Mockito.mockStatic(User.class)) {
      userMockedStatic.when(() -> User.hashPassword("newPassword")).thenReturn("newPasswordHash");

      userUCC.updateUser(user);

      Mockito.verify(user, Mockito.times(1)).setPassword("newPasswordHash");
    }

    assertEquals(userDB, userUCC.updateUser(user),
        "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with update failure")
  @Test
  void updateUserWithUpdateFailure() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);

    User userDB = Mockito.mock(UserImpl.class);
    Mockito.when(userDAO.getOneById(1)).thenReturn(userDB);

    Mockito.when(userDAO.update(user)).thenReturn(false);

    assertNull(userUCC.updateUser(user), "updateUser() did not return null");
  }

  @DisplayName("Get profile picture of existing user")
  @Test
  void getProfilePictureOfExistingUser() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(userDAO.getOneById(1)).thenReturn(user);

    File file = Mockito.mock(File.class);
    Mockito.when(user.profilePictureFile()).thenReturn(file);

    assertEquals(file, userUCC.getProfilePicture(user),
        "getProfilePicture() did not return the correct file");
  }

  @DisplayName("Get profile picture of non-existing user")
  @Test
  void getProfilePictureOfNonExistingUser() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(userDAO.getOneById(1)).thenReturn(null);

    assertNull(userUCC.getProfilePicture(user), "getProfilePicture() did not return null");
  }

  @DisplayName("Update profile picture of existing user")
  @Test
  void updateProfilePictureOfExistingUser() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(userDAO.getOneById(1)).thenReturn(user);

    InputStream inputStream = Mockito.mock(InputStream.class);
    Mockito.when(user.saveProfilePicture(inputStream)).thenReturn(true);

    Mockito.when(userDAO.update(user)).thenReturn(true);

    assertEquals(user, userUCC.updateProfilePicture(user, inputStream),
        "updateProfilePicture() did not return the correct user");
  }

  @DisplayName("Update profile picture of non-existing user")
  @Test
  void updateProfilePictureOfNonExistingUser() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(userDAO.getOneById(1)).thenReturn(null);

    InputStream inputStream = Mockito.mock(InputStream.class);

    assertNull(userUCC.updateProfilePicture(user, inputStream),
        "updateProfilePicture() did not return null");
  }
}