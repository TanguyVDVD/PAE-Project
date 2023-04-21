package be.vinci.pae.utils;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.DomainFactoryImpl;
import be.vinci.pae.domain.notification.NotificationImpl;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.DALServicesImpl;
import be.vinci.pae.services.DalBackendServices;
import be.vinci.pae.services.availability.AvailabilityDAO;
import be.vinci.pae.services.availability.AvailabilityDAOImpl;
import be.vinci.pae.services.notification.NotificationDAO;
import be.vinci.pae.services.notification.NotificationDAOImpl;
import be.vinci.pae.services.object.ObjectDAO;
import be.vinci.pae.services.object.ObjectDAOImpl;
import be.vinci.pae.services.objecttype.ObjectTypeDAO;
import be.vinci.pae.services.objecttype.ObjectTypeDAOImpl;
import be.vinci.pae.services.user.UserDAO;
import be.vinci.pae.services.user.UserDAOImpl;
import be.vinci.pae.ucc.availability.AvailabilityUCC;
import be.vinci.pae.ucc.availability.AvailabilityUCCImpl;
import be.vinci.pae.ucc.notification.NotificationUCC;
import be.vinci.pae.ucc.object.ObjectUCC;
import be.vinci.pae.ucc.object.ObjectUCCImpl;
import be.vinci.pae.ucc.objecttype.ObjectTypeUCC;
import be.vinci.pae.ucc.objecttype.ObjectTypeUCCImpl;
import be.vinci.pae.ucc.user.UserUCC;
import be.vinci.pae.ucc.user.UserUCCImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * ApplicationBinder class that implements the interfaces.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  /**
   * Method. Method that binds the implementation to the interface and specifies that only a single
   * instance will be created by the application
   */
  @Override
  protected void configure() {
    bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);

    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(ObjectUCCImpl.class).to(ObjectUCC.class).in(Singleton.class);
    bind(ObjectTypeUCCImpl.class).to(ObjectTypeUCC.class).in(Singleton.class);
    bind(AvailabilityUCCImpl.class).to(AvailabilityUCC.class).in(Singleton.class);
    bind(NotificationImpl.class).to(NotificationUCC.class).in(Singleton.class);

    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(ObjectDAOImpl.class).to(ObjectDAO.class).in(Singleton.class);
    bind(ObjectTypeDAOImpl.class).to(ObjectTypeDAO.class).in(Singleton.class);
    bind(AvailabilityDAOImpl.class).to(AvailabilityDAO.class).in(Singleton.class);
    bind(NotificationDAOImpl.class).to(NotificationDAO.class).in(Singleton.class);

    bind(DALServicesImpl.class).to(DalBackendServices.class).to(DALServices.class)
        .in(Singleton.class);
  }
}
