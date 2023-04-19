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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.AfterAll;
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
   * Mocked valid UserDTO.
   */
  private static User validUser;

  /**
   * Mocked static User.
   */
  private static MockedStatic<User> userMockedStatic;

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
    userMockedStatic = Mockito.mockStatic(User.class);
  }

  /**
   * Clean up.
   */
  @AfterAll
  static void cleanUp() {
    userMockedStatic.close();
  }

  /**
   * Set up mocked objects.
   */
  @BeforeEach
  void setUpMockedObjects() {
    Mockito.reset(userDAO);
    userMockedStatic.reset();

    validUser = Mockito.mock(UserImpl.class);
    setUpValidUser(validUser);
    Mockito.when(validUser.getId()).thenReturn(1);
    Mockito.when(validUser.getEmail()).thenReturn("validUser@example.com");
    Mockito.when(validUser.getPhoneNumber()).thenReturn("0493111111");

    Mockito.when(userDAO.getOneById(1)).thenReturn(validUser);
    Mockito.when(userDAO.getOneByEmail("validUser@example.com")).thenReturn(validUser);
    Mockito.when(userDAO.getOneByPhoneNumber("0493111111")).thenReturn(validUser);

    Mockito.when(userDAO.getOneByEmail("exception@example.com")).thenThrow(new RuntimeException());

    Mockito.when(userDAO.insert(validUser)).thenReturn(1);
    Mockito.when(userDAO.update(Mockito.any())).thenReturn(true);

    userMockedStatic.when(() -> User.emailIsValid("validUser@example.com")).thenReturn(true);
    userMockedStatic.when(() -> User.emailIsValid("anotherValidUser@example.com")).thenReturn(true);
    userMockedStatic.when(() -> User.formatPhoneNumber("0493111111")).thenReturn("0493111111");
    userMockedStatic.when(() -> User.formatPhoneNumber("493111111")).thenReturn("0493111111");
    userMockedStatic.when(() -> User.formatPhoneNumber("493999999")).thenReturn("0493999999");
    userMockedStatic.when(() -> User.formatPhoneNumber("0493999999")).thenReturn("0493999999");
    userMockedStatic.when(() -> User.hashPassword("password")).thenReturn("hashedPassword");
  }

  /**
   * Set up a valid user.
   *
   * @param validUser the user to set up
   */
  void setUpValidUser(User validUser) {
    Mockito.when(validUser.getId()).thenReturn(2);
    Mockito.when(validUser.getEmail()).thenReturn("anotherValidUser@example.com");
    Mockito.when(validUser.getPassword()).thenReturn("password");
    Mockito.when(validUser.getFirstName()).thenReturn("John");
    Mockito.when(validUser.getLastName()).thenReturn("Doe");
    Mockito.when(validUser.getPhoneNumber()).thenReturn("0493999999");
    Mockito.when(validUser.getPassword()).thenReturn("password");
    Mockito.when(validUser.getPhoto()).thenReturn(true);
    Mockito.when(validUser.getRegisterDate()).thenReturn(LocalDate.now());
    Mockito.when(validUser.getRole()).thenReturn("aidant");

    Mockito.when(validUser.isPasswordCorrect("password")).thenReturn(true);
    Mockito.when(validUser.isPasswordCorrect("wrongPassword")).thenReturn(false);

    Mockito.when(validUser.getPhoto()).thenReturn(true);

    Mockito.when(userDAO.getOneByEmail("anotherValidUser@example.com")).thenReturn(validUser);
    Mockito.when(userDAO.getOneByPhoneNumber("0493999999")).thenReturn(validUser);
  }

  @DisplayName("Login with correct email and password")
  @Test
  void loginWithCorrectEmailAndPassword() {
    UserDTO userDTO = userUCC.login("validUser@example.com", "password");
    assertAll(
        () -> assertNotNull(userDTO, "Login have not return a user"),
        () -> assertEquals(validUser, userDTO, "Login have not return the same user")
    );
  }

  @DisplayName("Login with incorrect email")
  @Test
  void loginWithIncorrectEmail() {
    UserDTO userDTO = userUCC.login("unknownUser@example.com", "password");
    assertNull(userDTO, "Login returned a user although the email is incorrect");
  }

  @DisplayName("Login with incorrect password")
  @Test
  void loginWithIncorrectPassword() {
    UserDTO userDTO = userUCC.login("validUser@example.com", "wrongPassword");
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
    UserDTO userDTO = userUCC.login("validUser@example.com", null);
    assertNull(userDTO, "Login returned a user although the password is null");
  }

  @DisplayName("Exception during login")
  @Test
  void loginException() {
    assertThrows(WebApplicationException.class,
        () -> userUCC.login("exception@example.com", "password"),
        "Login did not throw an exception");
  }

  @DisplayName("Register with correct credentials")
  @Test
  void registerWithCorrectCredentials() {
    Mockito.when(userDAO.getOneByEmail("validUser@example.com")).thenReturn(null);
    Mockito.when(userDAO.getOneByPhoneNumber("0493111111")).thenReturn(null);

    assertEquals(validUser, userUCC.register(validUser),
        "register() did not return the correct user");
  }

  @DisplayName("Register with incorrect email")
  @Test
  void registerWithIncorrectEmail() {
    Mockito.when(validUser.getEmail()).thenReturn("test");
    assertThrows(WebApplicationException.class, () -> userUCC.register(validUser),
        "register() did not throw an exception");
  }

  @DisplayName("Register with incorrect phone number")
  @Test
  void registerWithIncorrectPhoneNumber() {
    Mockito.when(validUser.getPhoneNumber()).thenReturn("aaa");

    assertThrows(WebApplicationException.class, () -> userUCC.register(validUser),
        "register() did not throw an exception");
  }

  @DisplayName("Register with existing email")
  @Test
  void registerWithExistingEmail() {
    assertThrows(WebApplicationException.class, () -> userUCC.register(validUser),
        "register() did not throw an exception");
  }

  @DisplayName("Register with existing phone number")
  @Test
  void registerWithExistingPhoneNumber() {
    Mockito.when(userDAO.getOneByEmail("validUser@example.com")).thenReturn(null);
    assertThrows(WebApplicationException.class, () -> userUCC.register(validUser),
        "register() did not throw an exception");
  }

  @DisplayName("Register with insert not returning id")
  @Test
  void registerWithInsertNotReturningId() {
    Mockito.when(userDAO.getOneByEmail("validUser@example.com")).thenReturn(null);
    Mockito.when(userDAO.getOneByPhoneNumber("0493111111")).thenReturn(null);

    Mockito.when(userDAO.insert(validUser)).thenReturn(-1);

    assertNull(userUCC.register(validUser), "register() did not return null");
  }

  @DisplayName("Exception during registration")
  @Test
  void exceptionDuringRegistration() {
    Mockito.when(userDAO.getOneByEmail("validUser@example.com")).thenReturn(null);
    Mockito.when(userDAO.getOneByPhoneNumber("0493111111")).thenReturn(null);

    Mockito.when(userDAO.insert(validUser)).thenThrow(new RuntimeException());

    assertThrows(WebApplicationException.class, () -> userUCC.register(validUser),
        "register() did not throw an exception");
  }

  @DisplayName("Get list of all users")
  @Test
  void getAllUsers() {
    List<UserDTO> users = new ArrayList<>();
    users.add(validUser);
    Mockito.when(userDAO.getAll("")).thenReturn(users);

    assertEquals(users, userUCC.getUsers(""), "getAllUsers() did not return the correct list");
  }

  @DisplayName("Exception when getting list of all users")
  @Test
  void exceptionWhenGettingAllUsers() {
    Mockito.when(userDAO.getAll("")).thenThrow(new RuntimeException());

    assertThrows(WebApplicationException.class, () -> userUCC.getUsers(""),
        "getUsers() did not throw an exception");
  }

  @DisplayName("Get a user by id")
  @Test
  void getUserById() {
    assertEquals(validUser, userUCC.getUserById(1),
        "getUserById() did not return the correct user");
  }

  @DisplayName("Exception when getting a user by id")
  @Test
  void exceptionWhenGettingUserById() {
    Mockito.when(userDAO.getOneById(1)).thenThrow(new RuntimeException());

    assertThrows(WebApplicationException.class, () -> userUCC.getUserById(1),
        "getUserById() did not throw an exception");
  }

  @DisplayName("Update user without verifying the current password, email, or phone number")
  @Test
  void updateUserWithoutVerifyingCurrentPassword() {
    assertEquals(validUser, userUCC.updateUser(validUser),
        "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with correct current password")
  @Test
  void updateUserWithCorrectCurrentPassword() {
    assertEquals(validUser, userUCC.updateUser(validUser, "password"),
        "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with incorrect current password")
  @Test
  void updateUserWithIncorrectCurrentPassword() {
    Mockito.when(validUser.isPasswordCorrect("password")).thenReturn(false);

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(validUser, "password"),
        "updateUser() did not throw an exception although the current password is incorrect");
  }

  @DisplayName("Update user with valid email")
  @Test
  void updateUserWithValidEmail() {
    Mockito.when(validUser.getPhoneNumber()).thenReturn(null);
    Mockito.when(validUser.getPassword()).thenReturn(null);

    assertEquals(validUser, userUCC.updateUser(validUser),
        "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with invalid email")
  @Test
  void updateUserWithInvalidEmail() {
    User user = Mockito.mock(UserImpl.class);
    setUpValidUser(user);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getEmail()).thenReturn("test");

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the email is invalid");
  }

  @DisplayName("Update user with valid phone number")
  @Test
  void updateUserWithValidPhoneNumber() {
    User user = Mockito.mock(UserImpl.class);
    setUpValidUser(user);
    Mockito.when(user.getId()).thenReturn(1);

    Mockito.when(userDAO.getOneByEmail("anotherValidUser@example.com")).thenReturn(null);
    Mockito.when(userDAO.getOneByPhoneNumber("0493999999")).thenReturn(null);

    assertEquals(validUser, userUCC.updateUser(user),
        "updateUser() did not return the correct user");
  }

  @DisplayName("Update user with invalid phone number")
  @Test
  void updateUserWithInvalidPhoneNumber() {
    User user = Mockito.mock(UserImpl.class);
    setUpValidUser(user);
    Mockito.when(userDAO.getOneById(2)).thenReturn(validUser);
    Mockito.when(userDAO.getOneByEmail("anotherValidUser@example.com")).thenReturn(null);
    Mockito.when(user.getPhoneNumber()).thenReturn("0493");

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the phone number is invalid");
  }

  @DisplayName("Update user with email already in use")
  @Test
  void updateUserWithExistingEmail() {
    User user = Mockito.mock(UserImpl.class);
    setUpValidUser(user);
    Mockito.when(user.getId()).thenReturn(1);

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the email is already in use");
  }

  @DisplayName("Update user with phone number already in use")
  @Test
  void updateUserWithExistingPhoneNumber() {
    User user = Mockito.mock(UserImpl.class);
    setUpValidUser(user);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getEmail()).thenReturn(null);

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the phone number is already in use");
  }

  @DisplayName("Update user with new password")
  @Test
  void updateUserWithNewPassword() {
    User user = Mockito.mock(UserImpl.class);
    setUpValidUser(user);
    Mockito.when(user.getId()).thenReturn(1);

    Mockito.when(userDAO.getOneByEmail("anotherValidUser@example.com")).thenReturn(null);
    Mockito.when(userDAO.getOneByPhoneNumber("0493999999")).thenReturn(null);

    userUCC.updateUser(user);

    Mockito.verify(user, Mockito.times(1)).setPassword("hashedPassword");
  }

  @DisplayName("Update user with update failure")
  @Test
  void updateUserWithUpdateFailure() {
    Mockito.when(userDAO.update(validUser)).thenReturn(false);

    assertNull(userUCC.updateUser(validUser), "updateUser() did not return null");
  }

  @DisplayName("Update user with non-existing user")
  @Test
  void updateUserWithNonExistingUser() {
    UserDTO user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);

    Mockito.when(userDAO.getOneById(1)).thenReturn(null);

    assertNull(userUCC.updateUser(user), "updateUser() did not return null");
  }

  @DisplayName("Exception when updating user")
  @Test
  void exceptionWhenUpdatingUser() {
    Mockito.when(userDAO.update(validUser)).thenThrow(new RuntimeException());

    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(validUser),
        "updateUser() did not throw an exception");
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

  @DisplayName("Update profile picture with update failure")
  @Test
  void updateProfilePictureWithUpdateFailure() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(userDAO.getOneById(1)).thenReturn(user);

    InputStream inputStream = Mockito.mock(InputStream.class);
    Mockito.when(user.saveProfilePicture(inputStream)).thenReturn(true);

    Mockito.when(userDAO.update(user)).thenReturn(false);

    assertNull(userUCC.updateProfilePicture(user, inputStream),
        "updateProfilePicture() did not return null");
  }

  @DisplayName("Exception during update profile picture")
  @Test
  void updateProfilePictureWithException() {
    User user = Mockito.mock(UserImpl.class);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(userDAO.getOneById(1)).thenReturn(user);

    InputStream inputStream = Mockito.mock(InputStream.class);
    Mockito.when(user.saveProfilePicture(inputStream)).thenThrow(new RuntimeException());

    assertThrows(WebApplicationException.class,
        () -> userUCC.updateProfilePicture(user, inputStream),
        "updateProfilePicture() did not throw an exception");
  }

  @DisplayName("Update user with wrong version number")
  @Test
  void updateUserWithWrongVersionNumber() {
    User user = Mockito.mock(UserImpl.class);
    setUpValidUser(user);
    Mockito.when(user.getId()).thenReturn(1);
    Mockito.when(user.getVersionNumber()).thenReturn(1549445);
    assertThrows(WebApplicationException.class, () -> userUCC.updateUser(user),
        "updateUser() did not throw an exception although the version number is wrong");
  }
}