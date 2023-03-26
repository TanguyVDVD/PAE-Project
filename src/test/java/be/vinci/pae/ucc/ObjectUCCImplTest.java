package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.object.Object;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.object.ObjectImpl;
import be.vinci.pae.services.object.ObjectDAO;
import be.vinci.pae.services.object.ObjectDAOImpl;
import be.vinci.pae.ucc.object.ObjectUCC;
import be.vinci.pae.ucc.object.ObjectUCCImpl;
import jakarta.inject.Singleton;
import java.time.LocalDate;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for ObjectUCCImpl.
 */
class ObjectUCCImplTest {

  /**
   * Mocked objectDAO.
   */
  private static ObjectDAO objectDAO;

  /**
   * ObjectUCC to test.
   */
  private static ObjectUCC objectUCC;

  private static DomainFactory myDomainFactory;

  /**
   * Set up the test.
   */
  @BeforeAll
  static void setUp() {
    objectDAO = Mockito.mock(ObjectDAOImpl.class);
    myDomainFactory = Mockito.mock(DomainFactory.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(ObjectUCCImpl.class).to(ObjectUCC.class).in(Singleton.class);

        bind(objectDAO).to(ObjectDAO.class);
        bind(myDomainFactory).to(DomainFactory.class);
      }
    });

    objectUCC = locator.getService(ObjectUCC.class);

  }

  @DisplayName("Accept a correct object proposition")
  @Test
  void AcceptACorrectObjectProposition() {

    Object object = Mockito.mock(ObjectImpl.class);
    //object.setStatus(null);
    System.out.println(object);
    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object);
    //Mockito.when(object.getStatus()).thenReturn(null);
    //Mockito.when(object.isStatusAlreadyDefined(object.getStatus())).thenReturn(false);
    Mockito.when(objectDAO.setStatusToAccepted(object.getId(), LocalDate.now())).thenReturn(object);
    ObjectDTO objectDTO = objectUCC.accept(object.getId());

    assertAll(
        () -> assertNotNull(objectDTO, "Accept return null"),
        () -> assertEquals(object, objectDTO, "Accept method does not return the same object")
    );

  }

  @DisplayName("Accept an object already accepted")
  @Test
  void AcceptAlreadyAcceptObjectProposition() {

    Object object = Mockito.mock(ObjectImpl.class);
    object.setStatus("accepté");
    System.out.println(object);
    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object);
    //Mockito.when(object.getStatus()).thenReturn(null);
    Mockito.when(object.isStatusAlreadyDefined(object.getStatus())).thenReturn(null);
    Mockito.when(objectDAO.setStatusToAccepted(object.getId(), LocalDate.now())).thenReturn(object);
    ObjectDTO objectDTO = objectUCC.accept(object.getId());

    assertNull(objectDTO, "Accept return is not null");

  }

}