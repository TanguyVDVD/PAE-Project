package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.object.Object;
import be.vinci.pae.domain.object.ObjectDTO;
import be.vinci.pae.domain.object.ObjectImpl;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.object.ObjectDAO;
import be.vinci.pae.services.object.ObjectDAOImpl;
import be.vinci.pae.ucc.object.ObjectUCC;
import be.vinci.pae.ucc.object.ObjectUCCImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import java.time.LocalDate;
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

  /**
   * Set up the test.
   */
  @BeforeAll
  static void setUp() {
    objectDAO = Mockito.mock(ObjectDAOImpl.class);

    DALServices myDalServices = Mockito.mock(DALServices.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(ObjectUCCImpl.class).to(ObjectUCC.class).in(Singleton.class);

        bind(objectDAO).to(ObjectDAO.class);
        bind(myDalServices).to(DALServices.class);
      }
    });

    objectUCC = locator.getService(ObjectUCC.class);

  }

  @BeforeEach
  void cleanUp() {
    Mockito.reset(objectDAO);
  }

  @DisplayName("Accept a correct object proposition")
  @Test
  void acceptACorrectObjectProposition() {

    Object object = Mockito.mock(ObjectImpl.class);
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(object.getStatus()).thenReturn(null);
    Mockito.when(object.getVersionNumber()).thenReturn(1);
    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object);
    Mockito.when(object.isStatusAlreadyDefined(object.getStatus())).thenReturn(false);
    Mockito.when(objectDAO.setStatusToAccepted(object.getId(), LocalDate.now(),
        object.getVersionNumber())).thenReturn(object);
    ObjectDTO objectDTO = objectUCC.accept(object.getId(), object.getVersionNumber());

    assertAll(
        () -> assertNotNull(objectDTO, "Accept return null"),
        () -> assertEquals(object, objectDTO, "Accept method does not return the same object")
    );

  }

  @DisplayName("Accept an object already accepted")
  @Test
  void acceptAlreadyAcceptObjectProposition() {

    Object object = Mockito.mock(ObjectImpl.class);
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(object.getStatus()).thenReturn("accepté");
    Mockito.when(object.getVersionNumber()).thenReturn(1);
    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object);
    Mockito.when(object.isStatusAlreadyDefined(object.getStatus())).thenReturn(true);
    Mockito.when(
            objectDAO.setStatusToAccepted(object.getId(), LocalDate.now(), object.getVersionNumber()))
        .thenReturn(object);

    ObjectDTO objectDTO = objectUCC.accept(object.getId(), object.getVersionNumber());

    assertNull(objectDTO, "Accept return is not null");

  }

  @DisplayName("Refuse an object already refused")
  @Test
  void refuseAnObjectProposition() {

    String reasonForRefusal = "Reason for refusal";
    Object object = Mockito.mock(ObjectImpl.class);
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(object.getStatus()).thenReturn(null);
    Mockito.when(object.getVersionNumber()).thenReturn(1);
    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object);
    Mockito.when(object.isStatusAlreadyDefined(object.getStatus())).thenReturn(false);
    Mockito.when(objectDAO.setStatusToRefused(object.getId(), reasonForRefusal, LocalDate.now(),
            object.getVersionNumber()))
        .thenReturn(object);

    ObjectDTO objectDTO = objectUCC.refuse(object.getId(), reasonForRefusal,
        object.getVersionNumber());

    assertAll(
        () -> assertNotNull(objectDTO, "Refuse return null"),
        () -> assertEquals(object, objectDTO, "Refuse method does not return the same object")
    );

  }

  @DisplayName("Refuse an object already refused")
  @Test
  void refuseAlreadyRefuseObjectProposition() {

    String reasonForRefusal = "Reason for refusal";
    Object object = Mockito.mock(ObjectImpl.class);
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(object.getStatus()).thenReturn("refusé");
    Mockito.when(object.getVersionNumber()).thenReturn(1);
    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object);
    Mockito.when(object.isStatusAlreadyDefined(object.getStatus())).thenReturn(true);
    Mockito.when(objectDAO.setStatusToRefused(object.getId(), reasonForRefusal, LocalDate.now(),
            object.getVersionNumber()))
        .thenReturn(object);

    ObjectDTO objectDTO = objectUCC.refuse(object.getId(), reasonForRefusal,
        object.getVersionNumber());

    assertNull(objectDTO, "Refuse return is not null");

  }

  @DisplayName("Update an object state, set to in workshop")
  @Test
  void updateAnObjectStateToInWorkshop() {
    LocalDate dateToday = LocalDate.now();

    Object object = Mockito.mock(ObjectImpl.class);
    Object object1 = Mockito.mock(ObjectImpl.class);
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(object.getStatus()).thenReturn("accepté");
    Mockito.when(object.getState()).thenReturn("à l'atelier");

    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object1);

    Mockito.when(object1.getId()).thenReturn(1);
    Mockito.when(object1.getStatus()).thenReturn("accepté");
    Mockito.when(object1.getState()).thenReturn("accepté");

    Mockito.when(objectDAO.updateObject(object1.getId(), object1)).thenReturn(object);

    ObjectDTO objectDTO = objectUCC.update(object.getId(), object, dateToday);

    assertAll(
        () -> assertNotNull(objectDTO, "Update return null"),
        () -> assertEquals(object, objectDTO, "Update method does not return the same object")
    );

  }

  @DisplayName("Update an object state, set to in shop")
  @Test
  void updateAnObjectStateToInShop() {

    Object object = Mockito.mock(ObjectImpl.class);
    Object object1 = Mockito.mock(ObjectImpl.class);
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(object.getStatus()).thenReturn("accepté");
    Mockito.when(object.getState()).thenReturn("en magasin");

    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object1);

    Mockito.when(object1.getId()).thenReturn(1);
    Mockito.when(object1.getStatus()).thenReturn("accepté");
    Mockito.when(object1.getState()).thenReturn("accepté");

    LocalDate dateToday = LocalDate.now();

    Mockito.when(objectDAO.updateObject(object1.getId(), object1)).thenReturn(object);

    ObjectDTO objectDTO = objectUCC.update(object.getId(), object, dateToday);

    assertAll(
        () -> assertNotNull(objectDTO, "Update return null"),
        () -> assertEquals(object, objectDTO, "Update method does not return the same object")
    );

  }

  @DisplayName("Update an object state, with null object")
  @Test
  void updateAnObjectStateNullObject() {

    Object object = Mockito.mock(ObjectImpl.class);
    Object object1 = Mockito.mock(ObjectImpl.class);
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(object.getStatus()).thenReturn("accepté");
    Mockito.when(object.getState()).thenReturn("en magasin");

    Mockito.when(objectDAO.getOneById(object.getId())).thenReturn(object1);

    Mockito.when(object1.getId()).thenReturn(1);
    Mockito.when(object1.getStatus()).thenReturn("accepté");
    Mockito.when(object1.getState()).thenReturn("accepté");

    Mockito.when(objectDAO.updateObject(object1.getId(), object1)).thenReturn(object);

    LocalDate dateToday = LocalDate.now();

    assertThrows(WebApplicationException.class,
        () -> objectUCC.update(object.getId(), null, dateToday),
        "Update return is not null");

  }

  @DisplayName("Get a object by id")
  @Test
  void getObjectById() {
    Object object = Mockito.mock(ObjectImpl.class);
    Mockito.when(objectDAO.getOneById(1)).thenReturn(object);

    assertEquals(object, objectUCC.getOne(1),
        "getOne did not return the correct object");
  }

  @DisplayName("Exception when getting an object by id")
  @Test
  void exceptionGetAnObjectByID() {
    Mockito.when(objectDAO.getOneById(1)).thenThrow(new RuntimeException());
    assertThrows(WebApplicationException.class, () -> objectUCC.getOne(1),
        "getOneByID did not throw an exception");

  }

  @DisplayName("Get list of all objects")
  @Test
  void getAllObjects() {
    List<ObjectDTO> objects = new ArrayList<>();
    ObjectDTO object = Mockito.mock(ObjectImpl.class);

    objects.add(object);
    Mockito.when(objectDAO.getAll("")).thenReturn(objects);

    assertEquals(objects, objectUCC.getObjects(""), "getObjects() did not return the correct list");
  }

  @DisplayName("Exception when getting list of all objects")
  @Test
  void exceptionWhenGettingAllObjects() {
    Mockito.when(objectDAO.getAll("")).thenThrow(new RuntimeException());

    assertThrows(WebApplicationException.class, () -> objectUCC.getObjects(""),
        "getObjects() did not throw an exception");
  }

  @DisplayName("Get list of all offers")
  @Test
  void getAllOffers() {
    List<ObjectDTO> offers = new ArrayList<>();
    ObjectDTO object = Mockito.mock(ObjectImpl.class);

    offers.add(object);
    Mockito.when(objectDAO.getOffers("")).thenReturn(offers);

    assertEquals(offers, objectUCC.getOffers(""), "getOffers() did not return the correct list");
  }

  @DisplayName("Exception when getting list of all offers")
  @Test
  void exceptionWhenGettingAllOffers() {
    Mockito.when(objectDAO.getOffers("")).thenThrow(new WebApplicationException());

    assertThrows(WebApplicationException.class, () -> objectUCC.getOffers(""),
        "getOffers() did not throw an exception");
  }

  @DisplayName("Get an object list by User id")
  @Test
  void getAllObjectsByUserID() {
    List<ObjectDTO> objects = new ArrayList<>();
    ObjectDTO object = Mockito.mock(ObjectImpl.class);

    objects.add(object);

    Mockito.when(objectDAO.getAllByUser(1)).thenReturn(objects);

    assertEquals(objects, objectUCC.getObjectsByUser(1),
        "getObject by id user not return the correct list");

  }

  @DisplayName("Exception while getting an object list by User id")
  @Test
  void exceptionGetAllObjectsByUserID() {
    List<ObjectDTO> objects = new ArrayList<>();
    ObjectDTO object = Mockito.mock(ObjectImpl.class);

    objects.add(object);

    Mockito.when(objectDAO.getAllByUser(1)).thenThrow(new RuntimeException());

    assertThrows(WebApplicationException.class, () -> objectUCC.getObjectsByUser(1),
        "getObject by id user did not return exception");

  }

  //test update object with wrong version number
  @DisplayName("Update an object with wrong version number")
  @Test
  void updateAnObjectWithWrongVersionNumber() {
    Object object = Mockito.mock(ObjectImpl.class);
    Mockito.when(object.getId()).thenReturn(1);
    Mockito.when(object.getVersionNumber()).thenReturn(46545345);
    assertThrows(WebApplicationException.class, () -> objectUCC.update(1, object, LocalDate.now()),
        "Update did not throw an exception");
  }


}