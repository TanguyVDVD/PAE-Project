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
import be.vinci.pae.utils.exceptions.BusinessException;
import be.vinci.pae.utils.exceptions.DALException;
import jakarta.inject.Singleton;
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
   * Mocked valid UserDTO.
   */
  private static User validUser2;

  /**
   * Mocked valid UserDTO that is not in the database.
   */
  private static User notInDBUser;

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
    validUser2 = Mockito.mock(UserImpl.class);
    notInDBUser = Mockito.mock(UserImpl.class);
    setUpValidUser(validUser, 1, true);
    setUpValidUser(validUser2, 2, true);
    setUpValidUser(notInDBUser, 3, false);

    userMockedStatic.when(() -> User.hashPassword("password")).thenReturn("hashedPassword");
  }

  /**
   * Set up a valid user.
   *
   * @param validUser the user to set up
   */
  void setUpValidUser(User validUser, int id, boolean inDB) {
    Mockito.when(validUser.getId()).thenReturn(id);
    Mockito.when(validUser.getEmail()).thenReturn("validUser" + id + "@example.com");
    Mockito.when(validUser.getPassword()).thenReturn("password");
    Mockito.when(validUser.getFirstName()).thenReturn("John");
    Mockito.when(validUser.getLastName()).thenReturn("Doe");
    Mockito.when(validUser.getPhoneNumber()).thenReturn("049311111" + id);
    Mockito.when(validUser.getPassword()).thenReturn("password");
    Mockito.when(validUser.getPhoto()).thenReturn(true);
    Mockito.when(validUser.getRegisterDate()).thenReturn(LocalDate.now());
    Mockito.when(validUser.getRole()).thenReturn("aidant");

    Mockito.when(validUser.isPasswordCorrect("password")).thenReturn(true);
    Mockito.when(validUser.isPasswordCorrect("wrongPassword")).thenReturn(false);

    Mockito.when(validUser.getPhoto()).thenReturn(true);

    if (inDB) {
      Mockito.when(userDAO.getOneById(id)).thenReturn(validUser);
      Mockito.when(userDAO.getOneByEmail("validUser" + id + "@example.com")).thenReturn(validUser);
      Mockito.when(userDAO.getOneByPhoneNumber("049311111" + id)).thenReturn(validUser);
      Mockito.when(userDAO.update(validUser)).thenReturn(validUser);
    } else {
      Mockito.when(userDAO.insert(validUser)).thenReturn(validUser);
    }
  }

  @DisplayName("Login with correct email and password")
  @Test
  void loginWithCorrectEmailAndPassword() {
    UserDTO userDTO = userUCC.login("validUser1@example.com", "password");

    assertEquals(validUser, userDTO, "Login hasn't returned the same user");
  }

  @DisplayName("Login with incorrect email")
  @Test
  void loginWithIncorrectEmail() {
    assertThrows(BusinessException.class, () -> userUCC.login("invalid@example.com", "password"));
  }

  @DisplayName("Login with incorrect password")
  @Test
  void loginWithIncorrectPassword() {
    assertThrows(BusinessException.class,
        () -> userUCC.login("validUser1@example.com", "wrongPassword"));
  }

  @DisplayName("Register with valid user")
  @Test
  void registerWithValidUser() {
    UserDTO userDTO = userUCC.register(notInDBUser);

    assertEquals(notInDBUser, userDTO, "Register have not return the same user");
  }

  @DisplayName("Register with already used email")
  @Test
  void registerWithAlreadyUsedEmail() {
    Mockito.when(notInDBUser.getEmail()).thenReturn("validUser1@example.com");

    assertThrows(BusinessException.class, () -> userUCC.register(notInDBUser));
  }

  @DisplayName("Register with already used phone number")
  @Test
  void registerWithAlreadyUsedPhoneNumber() {
    Mockito.when(notInDBUser.getPhoneNumber()).thenReturn("0493111111");

    assertThrows(BusinessException.class, () -> userUCC.register(notInDBUser));
  }

  @DisplayName("Get all users")
  @Test
  void getAllUsers() {
    String query = "query";
    List<UserDTO> usersList = new ArrayList<>();
    usersList.add(validUser);
    usersList.add(validUser2);
    Mockito.when(userDAO.getAll(query)).thenReturn(usersList);

    List<UserDTO> users = userUCC.getUsers(query);

    assertAll(() -> assertNotNull(users, "Get all users have not return a list"),
        () -> assertEquals(2, users.size(),
            "Get all users have not return the correct number of users"));
  }

  @DisplayName("Get all users with exception")
  @Test
  void getAllUsersWithException() {
    Mockito.when(userDAO.getAll("")).thenThrow(new DALException(""));

    assertThrows(Exception.class, () -> userUCC.getUsers(""));
  }

  @DisplayName("Get user in DB by id")
  @Test
  void getUserById() {
    UserDTO user = userUCC.getUserById(1);

    assertEquals(validUser, user, "Get one user by id have not return the correct user");
  }

  @DisplayName("Get user not in DB by id")
  @Test
  void getUserNotInDBById() {
    assertNull(userUCC.getUserById(3));
  }

  @DisplayName("Get user by id with exception")
  @Test
  void getUserByIdWithException() {
    Mockito.when(userDAO.getOneById(1)).thenThrow(new DALException(""));

    assertThrows(Exception.class, () -> userUCC.getUserById(1));
  }

  @DisplayName("Update user with data already set")
  @Test
  void updateWithAlreadySetData() {
    assertEquals(validUser, userUCC.updateUser(validUser),
        "Update have not return the same user");
  }

  @DisplayName("Update user with no data")
  @Test
  void updateValidUser() {
    UserDTO userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getId()).thenReturn(1);
    Mockito.when(userDAO.update(userDTO)).thenReturn(userDTO);

    assertEquals(userDTO, userUCC.updateUser(userDTO),
        "Update have not return the same user");
  }

  @DisplayName("Update user that doesn't exist")
  @Test
  void updateNotInDBUser() {
    assertNull(userUCC.updateUser(notInDBUser));
  }

  @DisplayName("Update user with valid current password")
  @Test
  void updateWithValidCurrentPassword() {
    UserDTO userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getId()).thenReturn(1);
    Mockito.when(userDAO.update(userDTO)).thenReturn(userDTO);

    assertEquals(userDTO, userUCC.updateUser(userDTO, "password"),
        "Update have not return the same user");
  }

  @DisplayName("Update user with invalid current password")
  @Test
  void updateWithInvalidCurrentPassword() {
    UserDTO userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getId()).thenReturn(1);

    assertThrows(BusinessException.class, () -> userUCC.updateUser(userDTO, "wrongPassword"));
  }

  @DisplayName("Update user with already used email")
  @Test
  void updateWithAlreadyUsedEmail() {
    UserDTO userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getId()).thenReturn(1);
    Mockito.when(userDTO.getEmail()).thenReturn("validUser2@example.com");

    assertThrows(BusinessException.class, () -> userUCC.updateUser(userDTO),
        "Update hasn't thrown an exception");
  }

  @DisplayName("Update user with valid new email")
  @Test
  void updateWithValidNewEmail() {
    UserDTO userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getId()).thenReturn(1);
    Mockito.when(userDTO.getEmail()).thenReturn("newEmail@example.com");
    Mockito.when(userDAO.update(userDTO)).thenReturn(userDTO);

    assertEquals(userDTO, userUCC.updateUser(userDTO), "Update have not return the same user");
  }

  @DisplayName("Update user with already used phone number")
  @Test
  void updateWithAlreadyUsedPhoneNumber() {
    UserDTO userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getId()).thenReturn(1);
    Mockito.when(userDTO.getPhoneNumber()).thenReturn("0493111112");

    assertThrows(BusinessException.class, () -> userUCC.updateUser(userDTO),
        "Update hasn't thrown an exception");
  }

  @DisplayName("Update user with valid new phone number")
  @Test
  void updateWithValidNewPhoneNumber() {
    UserDTO userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getId()).thenReturn(1);
    Mockito.when(userDTO.getPhoneNumber()).thenReturn("0493111119");
    Mockito.when(userDAO.update(userDTO)).thenReturn(userDTO);

    assertEquals(userDTO, userUCC.updateUser(userDTO), "Update have not return the same user");
  }

  @DisplayName("Update user with new password")
  @Test
  void updateWithNewPassword() {
    UserDTO userDTO = Mockito.mock(UserDTO.class);
    Mockito.when(userDTO.getId()).thenReturn(1);
    Mockito.when(userDTO.getPassword()).thenReturn("newPassword");
    Mockito.when(userDAO.update(userDTO)).thenReturn(userDTO);

    assertEquals(userDTO, userUCC.updateUser(userDTO), "Update have not return the same user");
  }

  @DisplayName("Get profile picture")
  @Test
  void getProfilePicture() {
    File file = Mockito.mock(File.class);
    Mockito.when(validUser.profilePictureFile()).thenReturn(file);

    assertEquals(file, userUCC.getProfilePicture(validUser),
        "Get profile picture have not return the correct file");
  }

  @DisplayName("Update profile picture with existing user and correct password")
  @Test
  void updateProfilePictureWithExistingUserAndCorrectPassword() {
    InputStream file = Mockito.mock(InputStream.class);

    assertEquals(validUser, userUCC.updateProfilePicture(validUser, "password", file),
        "Update profile picture have not return the correct user");
  }

  @DisplayName("Update profile picture with non-existing user")
  @Test
  void updateProfilePictureWithNonExistingUser() {
    InputStream file = Mockito.mock(InputStream.class);

    assertNull(userUCC.updateProfilePicture(notInDBUser, "password", file),
        "Update profile picture have not return null");
  }

  @DisplayName("Update profile picture with existing user and wrong password")
  @Test
  void updateProfilePictureWithExistingUserAndWrongPassword() {
    InputStream file = Mockito.mock(InputStream.class);

    assertThrows(BusinessException.class,
        () -> userUCC.updateProfilePicture(validUser, "wrongPassword", file),
        "Update profile picture hasn't thrown an exception");
  }

  @DisplayName("Update profile picture with exception")
  @Test
  void updateProfilePictureWithException() {
    InputStream file = Mockito.mock(InputStream.class);
    Mockito.when(userDAO.update(validUser)).thenThrow(new DALException(""));

    assertThrows(Exception.class, () -> userUCC.updateProfilePicture(validUser, "password", file),
        "Update profile picture hasn't thrown an exception");
  }

  @DisplayName("Remove profile picture with existing user and correct password")
  @Test
  void removeProfilePictureWithExistingUserAndCorrectPassword() {
    assertEquals(validUser, userUCC.removeProfilePicture(validUser, "password"),
        "Remove profile picture have not return the correct user");
  }

  @DisplayName("Remove profile picture with non-existing user")
  @Test
  void removeProfilePictureWithNonExistingUser() {
    assertNull(userUCC.removeProfilePicture(notInDBUser, "password"),
        "Remove profile picture have not return null");
  }

  @DisplayName("Remove profile picture with existing user and wrong password")
  @Test
  void removeProfilePictureWithExistingUserAndWrongPassword() {
    assertThrows(BusinessException.class,
        () -> userUCC.removeProfilePicture(validUser, "wrongPassword"),
        "Remove profile picture hasn't thrown an exception");
  }

  @DisplayName("Remove profile picture with exception")
  @Test
  void removeProfilePictureWithException() {
    Mockito.when(userDAO.update(validUser)).thenThrow(new DALException(""));

    assertThrows(Exception.class, () -> userUCC.removeProfilePicture(validUser, "password"),
        "Remove profile picture hasn't thrown an exception");
  }
}