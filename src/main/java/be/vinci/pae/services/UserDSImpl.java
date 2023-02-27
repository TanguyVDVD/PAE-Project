package be.vinci.pae.services;

import be.vinci.pae.domain.DomainFactory;
import jakarta.inject.Inject;

/**
 * Class that implements UserDs interface Provide the different methods
 */
public class UserDSImpl implements UserDS {

  @Inject
  private static DomainFactory myDomainFactory;

}
