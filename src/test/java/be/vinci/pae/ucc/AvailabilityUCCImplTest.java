package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.availability.AvailabilityDTO;
import be.vinci.pae.domain.availability.AvailabilityImpl;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import be.vinci.pae.ucc.availability.AvailabilityUCC;
import be.vinci.pae.ucc.availability.AvailabilityUCCImpl;
import be.vinci.pae.utils.exceptions.DALException;
import be.vinci.pae.utils.exceptions.UserException;
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

class AvailabilityUCCImplTest {

  /**
   * Mocked availabilityDAO.
   */
  private static AvailabilityDAO availabilityDAO;


  /**
   * availabilityUCC to test.
   */
  private static AvailabilityUCC availabilityUCC;

  /**
   * Set up the test.
   */
  @BeforeAll
  static void setUp() {
    availabilityDAO = Mockito.mock(AvailabilityDAO.class);

    DALServices myDalServices = Mockito.mock(DALServices.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(AvailabilityUCCImpl.class).to(AvailabilityUCC.class).in(Singleton.class);

        bind(availabilityDAO).to(AvailabilityDAO.class);
        bind(myDalServices).to(DALServices.class);
      }
    });

    availabilityUCC = locator.getService(AvailabilityUCC.class);
  }

  @BeforeEach
  void cleanUp() {
    Mockito.reset(availabilityDAO);
  }

  @DisplayName("Get the list of all availabilities")
  @Test
  void getAllAvailabilities() {
    List<AvailabilityDTO> availabilities = new ArrayList<>();
    AvailabilityDTO availability = Mockito.mock(AvailabilityImpl.class);

    availabilities.add(availability);
    Mockito.when(availabilityDAO.getAll()).thenReturn(availabilities);

    assertEquals(availabilities, availabilityUCC.getAvailabilities(),
        "getAvailabilities() did not return the correct list");
  }

  @DisplayName("Try to get the list of all availabilities")
  @Test
  void getAllAvailabilitiesException() {
    Mockito.when(availabilityDAO.getAll()).thenThrow(new DALException(""));

    assertThrows(DALException.class, () -> availabilityUCC.getAvailabilities(),
        "getAllObjectTypesException did not throw an exception");
  }

  @DisplayName("Add a good availability")
  @Test
  void addOne() {
    AvailabilityDTO availability = Mockito.mock(AvailabilityImpl.class);
    Mockito.when(availabilityDAO.addOne(availability)).thenReturn(availability);

    assertEquals(availability, availabilityUCC.addOne(availability),
        "addOne did not return the correct availability");
  }

  @DisplayName("Try to add an existing availability")
  @Test
  void addExistingOne() {
    AvailabilityDTO availability = Mockito.mock(AvailabilityImpl.class);
    Mockito.when(availabilityDAO.getOneByDate(availability.getDate()))
        .thenReturn(availability);

    assertThrows(UserException.class, () -> availabilityUCC.addOne(availability),
        "addExistingOne did not throw an exception");
  }

  @DisplayName("Try to add an existing availability")
  @Test
  void addOneException() {
    AvailabilityDTO availability = Mockito.mock(AvailabilityImpl.class);
    Mockito.when(availabilityDAO.getOneByDate(availability.getDate()))
        .thenThrow(new DALException(""));

    assertThrows(DALException.class, () -> availabilityUCC.addOne(availability),
        "addOneException did not throw an exception");
  }

  @DisplayName("Delete an availability not linked to any object")
  @Test
  void deleteGoodOne() {
    AvailabilityDTO availability = Mockito.mock(AvailabilityImpl.class);

    Mockito.when(availabilityDAO.deleteOne(availability.getId())).thenReturn(availability);
    Mockito.when(availabilityDAO.isLinked(availability)).thenReturn(false);
    Mockito.when(availabilityDAO.getOneById(availability.getId())).thenReturn(availability);

    assertEquals(availability, availabilityUCC.deleteOne(availability.getId()),
        "deleteOne did not return the correct availability");
  }

  @DisplayName("Try to delete an availability not in the db")
  @Test
  void deleteBadOne() {
    AvailabilityDTO availability = Mockito.mock(AvailabilityImpl.class);

    Mockito.when(availabilityDAO.deleteOne(availability.getId())).thenReturn(availability);
    Mockito.when(availabilityDAO.isLinked(availability)).thenReturn(false);
    Mockito.when(availabilityDAO.getOneById(availability.getId())).thenReturn(null);

    assertThrows(UserException.class, () -> availabilityUCC.deleteOne(availability.getId()),
        "deleteBadOne did not throw an exception");
  }

  @DisplayName("Try to delete an availability already linked to an object")
  @Test
  void deleteGoodOneButLinked() {
    AvailabilityDTO availability = Mockito.mock(AvailabilityImpl.class);

    Mockito.when(availabilityDAO.deleteOne(availability.getId())).thenReturn(availability);
    Mockito.when(availabilityDAO.isLinked(availability)).thenReturn(true);
    Mockito.when(availabilityDAO.getOneById(availability.getId())).thenReturn(availability);

    assertThrows(UserException.class, () -> availabilityUCC.deleteOne(availability.getId()),
        "deleteGoodOneButLinked did not throw an exception");
  }
}