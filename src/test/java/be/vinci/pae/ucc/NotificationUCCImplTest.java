package be.vinci.pae.ucc;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.DomainFactoryImpl;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.notification.NotificationDAO;
import be.vinci.pae.services.notification.NotificationDAOImpl;
import be.vinci.pae.ucc.notification.NotificationUCC;
import be.vinci.pae.ucc.notification.NotificationUCCImpl;
import jakarta.inject.Singleton;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class NotificationUCCImplTest {


  /**
   * Mocked notificationDAO.
   */
  private static NotificationDAO notificationDAO;


  /**
   * NotificationUCC to test.
   */
  private static NotificationUCC notificationUCC;

  private static DomainFactory domainFactory;

  /**
   * Set up the test.
   */
  @BeforeAll
  static void setUp() {
    notificationDAO = Mockito.mock(NotificationDAOImpl.class);

    DALServices myDalServices = Mockito.mock(DALServices.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);
        bind(NotificationUCCImpl.class).to(NotificationUCC.class).in(Singleton.class);

        bind(notificationDAO).to(NotificationDAO.class);
        bind(myDalServices).to(DALServices.class);
      }
    });

    domainFactory = locator.getService(DomainFactory.class);

    notificationUCC = locator.getService(NotificationUCC.class);

  }

  @BeforeEach
  void cleanUp() {
    Mockito.reset(notificationDAO);
  }

  /**
   @DisplayName("create an accepted notification object")
   @Test void createAcceptedRefusedObjectNotificationWorking() {

   Notification notificationDTO = Mockito.mock(NotificationImpl.class);
   Object object = Mockito.mock(ObjectImpl.class);
   Mockito.when(object.getStatus()).thenReturn("accepté");
   User user = Mockito.mock(UserImpl.class);
   Mockito.when(object.getUser()).thenReturn(user);
   Mockito.when(user.getId()).thenReturn(1);

   Notification notification = Mockito.mock(NotificationImpl.class);
   Notification notificationDTO1 = Mockito.mock(NotificationImpl.class);
   Notification notificationDTO2 = Mockito.mock(NotificationImpl.class);
   Notification notificationDTO3 = Mockito.mock(NotificationImpl.class);
   Notification notificationDTO4 = Mockito.mock(NotificationImpl.class);
   Mockito.when(notification.getRead()).thenReturn(false);

   Mockito.when(notification.setUpNotificationText(object, notification))
   .thenReturn(notificationDTO1);

   Mockito.when(notificationDAO.createObjectNotification(notificationDTO1))
   .thenReturn(notificationDTO2);

   Mockito.when(notificationDTO2.setUpNotificationUser(notification, 1))
   .thenReturn(notificationDTO3);

   Mockito.when(notificationDAO.createObjectUserNotification(notificationDTO3))
   .thenReturn(notificationDTO4);

   NotificationDTO notificationDTOF = notificationUCC.createAcceptedRefusedObjectNotification(
   object);

   assertAll(
   () -> assertNotNull(notificationDTOF, "Accept return null"),
   () -> assertEquals(notification, notificationDTOF,
   "Accept method does not return the same object")
   );

   }
   **/

}
