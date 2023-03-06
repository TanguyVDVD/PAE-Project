package be.vinci.pae.utils;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.DomainFactoryImpl;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.DalServicesImpl;
import be.vinci.pae.services.UserDS;
import be.vinci.pae.services.UserDSImpl;
import be.vinci.pae.ucc.UserUCC;
import be.vinci.pae.ucc.UserUCCImpl;
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
    bind(UserDSImpl.class).to(UserDS.class).in(Singleton.class);
    bind(DalServicesImpl.class).to(DalServices.class).in(Singleton.class);
  }
}
