package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.objecttype.ObjectTypeDTO;
import be.vinci.pae.domain.objecttype.ObjectTypeImpl;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.objecttype.ObjectTypeDAO;
import be.vinci.pae.ucc.objecttype.ObjectTypeUCC;
import be.vinci.pae.ucc.objecttype.ObjectTypeUCCImpl;
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

class ObjectTypeUCCImplTest {

  /**
   * Mocked objectTypeDAO.
   */
  private static ObjectTypeDAO objectTypeDAO;

  /**
   * objectTypeUCC to test.
   */
  private static ObjectTypeUCC objectTypeUCC;

  /**
   * Set up the test.
   */
  @BeforeAll
  static void setUp() {
    objectTypeDAO = Mockito.mock(ObjectTypeDAO.class);

    DALServices myDalServices = Mockito.mock(DALServices.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(ObjectTypeUCCImpl.class).to(ObjectTypeUCC.class).in(Singleton.class);

        bind(objectTypeDAO).to(ObjectTypeDAO.class);
        bind(myDalServices).to(DALServices.class);
      }
    });

    objectTypeUCC = locator.getService(ObjectTypeUCC.class);

  }

  @BeforeEach
  void cleanUp() {
    Mockito.reset(objectTypeDAO);
  }

  @DisplayName("Get list of all object types")
  @Test
  void getAllObjectTypes() {
    List<ObjectTypeDTO> objectTypes = new ArrayList<>();
    ObjectTypeDTO objectType = Mockito.mock(ObjectTypeImpl.class);

    objectTypes.add(objectType);
    Mockito.when(objectTypeDAO.getAll()).thenReturn(objectTypes);

    assertEquals(objectTypes, objectTypeUCC.getObjectTypes(),
        "getObjectTypes() did not return the correct list");
  }

  @DisplayName("Try to get list of all object types")
  @Test
  void getAllObjectTypesException() {
    List<ObjectTypeDTO> objectTypes = new ArrayList<>();
    ObjectTypeDTO objectType = Mockito.mock(ObjectTypeImpl.class);

    objectTypes.add(objectType);
    Mockito.when(objectTypeDAO.getAll()).thenThrow(new DALException(""));

    assertThrows(DALException.class, () -> objectTypeUCC.getObjectTypes(),
        "getAllObjectTypesException did not throw an exception");
  }
}