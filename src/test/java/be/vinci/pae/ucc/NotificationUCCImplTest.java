package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.notification.Notification;
import be.vinci.pae.domain.notification.NotificationDTO;
import be.vinci.pae.domain.notification.NotificationImpl;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.object.ObjectImpl;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserImpl;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.notification.NotificationDAO;
import be.vinci.pae.services.notification.NotificationDAOImpl;
import be.vinci.pae.ucc.notification.NotificationUCC;
import be.vinci.pae.ucc.notification.NotificationUCCImpl;
import be.vinci.pae.utils.exceptions.DALException;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


/**
 * Test class for NotificationUCCImpl .
 */
public class NotificationUCCImplTest {


  /**
   * Mocked notificationDAO.
   */
  private static NotificationDAO notificationDAO;


  /**
   * NotificationUCC to test.
   */
  private static NotificationUCC notificationUCC;

  /**
   * DomainFactory to test.
   */
  private static DomainFactory domainFactory;

  /**
   * Set up the test.
   */
  @BeforeAll
  static void setUp() {
    notificationDAO = Mockito.mock(NotificationDAOImpl.class);

    DALServices myDalServices = Mockito.mock(DALServices.class);

    domainFactory = Mockito.mock(DomainFactory.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(NotificationUCCImpl.class).to(NotificationUCC.class).in(Singleton.class);

        bind(domainFactory).to(DomainFactory.class);
        bind(notificationDAO).to(NotificationDAO.class);
        bind(myDalServices).to(DALServices.class);
      }
    });

    notificationUCC = locator.getService(NotificationUCC.class);

  }

  @BeforeEach
  void cleanUp() {
    Mockito.reset(notificationDAO);
  }


  @DisplayName("create an accepted notification object")
  @Test
  void createAcceptedRefusedObjectNotificationWorking() {

    ObjectDTO object = Mockito.mock(ObjectImpl.class);
    User user = Mockito.mock(UserImpl.class);

    Mockito.when(object.getUser()).thenReturn(user);
    Mockito.when(object.getStatus()).thenReturn("accepté");
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(user.getId()).thenReturn(1);

    Notification notificationDTOStart = Mockito.mock(NotificationImpl.class);

    Mockito.when(domainFactory.getNotification()).thenReturn(notificationDTOStart);

    Mockito.when(notificationDTOStart.setUpNotificationText(object, notificationDTOStart))
        .thenReturn(notificationDTOStart);

    Mockito.when(notificationDAO.createObjectNotification(notificationDTOStart))
        .thenReturn(notificationDTOStart);

    Mockito.when(notificationDTOStart.setUpNotificationUser(notificationDTOStart, 1))
        .thenReturn(notificationDTOStart);

    Mockito.when(notificationDAO.createObjectUserNotification(notificationDTOStart))
        .thenReturn(notificationDTOStart);

    NotificationDTO notificationDTOF = notificationUCC.createAcceptedRefusedObjectNotification(
        object);

    assertAll(
        () -> assertNotNull(notificationDTOF, "Accept return null"),
        () -> assertEquals(notificationDTOStart, notificationDTOF,
            "Accept method does not return the same notification")
    );

  }

  @DisplayName("create an accepted notification object who raise an exception")
  @Test
  void createAcceptedRefusedObjectNotificationNotWorking() {

    ObjectDTO object = Mockito.mock(ObjectImpl.class);
    User user = Mockito.mock(UserImpl.class);

    Mockito.when(object.getUser()).thenReturn(user);
    Mockito.when(object.getStatus()).thenReturn("accepté");
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(user.getId()).thenReturn(1);

    Notification notificationDTOStart = Mockito.mock(NotificationImpl.class);

    Mockito.when(domainFactory.getNotification()).thenReturn(notificationDTOStart);

    Mockito.when(notificationDTOStart.setUpNotificationText(object, notificationDTOStart))
        .thenReturn(notificationDTOStart);

    Mockito.when(notificationDAO.createObjectNotification(notificationDTOStart))
        .thenThrow(new DALException(""));

    assertThrows(Exception.class, () -> notificationUCC.createAcceptedRefusedObjectNotification(
        object));

  }

  @DisplayName("do not create a notification no user")
  @Test
  void createAcceptedRefusedObjectNotificationNReturnNullNoUser() {

    ObjectDTO object = Mockito.mock(ObjectImpl.class);

    Mockito.when(object.getUser()).thenReturn(null);

    assertNull(notificationUCC.createAcceptedRefusedObjectNotification(object),
        "it should return null");

  }


  @DisplayName("create correct new object notification")
  @Test
  void createNewObjectNotification() {

    ObjectDTO object = Mockito.mock(ObjectImpl.class);
    User user = Mockito.mock(UserImpl.class);

    Mockito.when(object.getUser()).thenReturn(user);
    Mockito.when(user.getId()).thenReturn(1);

    NotificationDTO notificationDTOStart = Mockito.mock(NotificationImpl.class);
    Mockito.when(domainFactory.getNotification()).thenReturn(notificationDTOStart);

    notificationDTOStart.setIdObject(1);
    notificationDTOStart.setNotificationText("New");

    Mockito.when(notificationDAO.createObjectNotification(notificationDTOStart))
        .thenReturn(notificationDTOStart);

    ArrayList<Integer> arrayList = new ArrayList<>();
    arrayList.add(1);
    arrayList.add(2);

    Mockito.when(notificationDAO.getAllHelperId()).thenReturn(arrayList);

    Mockito.when(notificationDAO.createObjectUserNotification(notificationDTOStart))
        .thenReturn(notificationDTOStart);

    NotificationDTO notificationDTO = notificationUCC.createNewObjectPropositionNotification(
        object);

    assertAll(
        () -> assertNotNull(notificationDTO, "Accept return null"),
        () -> assertEquals(notificationDTOStart, notificationDTO,
            "new proposition notification method does not return the same notification")
    );


  }

  @DisplayName("create correct new object notification raise an exception")
  @Test
  void createNewObjectNotificationNotWorking() {

    ObjectDTO object = Mockito.mock(ObjectImpl.class);
    User user = Mockito.mock(UserImpl.class);

    Mockito.when(object.getUser()).thenReturn(user);
    Mockito.when(user.getId()).thenReturn(1);

    NotificationDTO notificationDTOStart = Mockito.mock(NotificationImpl.class);
    Mockito.when(domainFactory.getNotification()).thenReturn(notificationDTOStart);

    notificationDTOStart.setIdObject(1);
    notificationDTOStart.setNotificationText("New");

    Mockito.when(notificationDAO.createObjectNotification(notificationDTOStart))
        .thenThrow(new DALException(""));

    assertThrows(Exception.class,
        () -> notificationUCC.createNewObjectPropositionNotification(object));


  }

  @DisplayName("get all the notification of a user")
  @Test
  void getNotificationUserWorking() {

    NotificationDTO notificationDTONumber1 = Mockito.mock(NotificationImpl.class);
    NotificationDTO notificationDTONumber2 = Mockito.mock(NotificationImpl.class);
    List<NotificationDTO> notificationDTOList = new ArrayList<>();
    notificationDTOList.add(notificationDTONumber1);
    notificationDTOList.add(notificationDTONumber2);
    Mockito.when(notificationDAO.getNotificationsByUserID(1)).thenReturn(notificationDTOList);

    List<NotificationDTO> notificationDTOS = notificationUCC.getNotificationsByUserID(1);

    assertAll(() -> assertNotNull(notificationDTOS, "Get all notifications have not return a list"),
        () -> assertEquals(2, notificationDTOS.size(),
            "Get all notifications have not return the correct number of notification"));

  }

  @DisplayName("get all the notification of a user with exception")
  @Test
  void getNotificationUserWithException() {
    Mockito.when(notificationDAO.getNotificationsByUserID(-1)).thenThrow(new DALException(""));

    assertThrows(Exception.class, () -> notificationUCC.getNotificationsByUserID(-1));
  }

  @DisplayName("mark a notification as read correctly working")
  @Test
  void markANotificationAsReadWorking() {
    NotificationDTO notificationDTO = Mockito.mock(NotificationImpl.class);
    Mockito.when(notificationDAO.markANotificationAsRead(notificationDTO))
        .thenReturn(notificationDTO);

    assertEquals(notificationDTO, notificationUCC.markANotificationAsRead(notificationDTO),
        "Mark as read has not returned the same notification");
  }

  @DisplayName("mark a notification as read correctly not working")
  @Test
  void markANotificationAsReadNotWorking() {
    NotificationDTO notificationDTO = Mockito.mock(NotificationImpl.class);
    Mockito.when(notificationDAO.markANotificationAsRead(notificationDTO))
        .thenReturn(null);

    assertNull(notificationUCC.markANotificationAsRead(notificationDTO),
        "Mark as read do not return null notification");
  }

  @DisplayName("mark a notification as read correctly not working and raise exception")
  @Test
  void markANotificationAsReadRaiseException() {
    NotificationDTO notificationDTO = Mockito.mock(NotificationImpl.class);
    Mockito.when(notificationDAO.markANotificationAsRead(notificationDTO))
        .thenThrow(new DALException(""));

    assertThrows(Exception.class, () -> notificationUCC.markANotificationAsRead(notificationDTO),
        "Mark as read do not raise exception");

  }

}
