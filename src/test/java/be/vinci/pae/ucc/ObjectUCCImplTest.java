package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.object.ObjectImpl;
import be.vinci.pae.services.object.ObjectDAO;
import be.vinci.pae.ucc.object.ObjectUCC;
import be.vinci.pae.ucc.object.ObjectUCCImpl;
import jakarta.inject.Singleton;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for ObjectUCCImpl.
 */
class ObjectUCCImplTest {

  /**
   * Mocked userDAO.
   */
  private static ObjectDAO objectDAO;

  /**
   * UserUCC to test.
   */
  private static ObjectUCC objectUCC;

  /**
   * Set up the test.
   */
  @BeforeEach
  void setUp() {
    objectDAO = Mockito.mock(ObjectDAO.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(ObjectUCCImpl.class).to(ObjectUCC.class).in(Singleton.class);

        bind(objectDAO).to(ObjectDAO.class);
      }
    });

    objectUCC = locator.getService(ObjectUCC.class);
  }

  @DisplayName("Refuse an object without status and with a reason for refusal")
  @Test
  void refuseWithoutStatusWithReasonForRefusal() {
    ObjectDTO object = Mockito.mock(ObjectImpl.class);
    Mockito.when(objectDAO.getOneById(1)).thenReturn(object);
    Mockito.when(object.getStatus()).thenReturn(null);
    Mockito.when(object.getState()).thenReturn("proposé");
    Mockito.when(object.getReasonForRefusal()).thenReturn(null);

    ObjectDTO objectDTO = objectUCC.refuse(1, "Reason for refusal");
    assertAll(
        () -> assertEquals(objectDTO.getStatus(), "refusé"),
        () -> assertEquals(objectDTO.getState(), "refusé"),
        () -> assertEquals(objectDTO.getReasonForRefusal(), "Reason for refusal")
    );
  }
}