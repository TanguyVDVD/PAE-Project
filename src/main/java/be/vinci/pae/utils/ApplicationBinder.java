package be.vinci.pae.utils;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.DomainFactoryImpl;
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

  }
}
