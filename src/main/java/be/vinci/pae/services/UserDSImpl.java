package be.vinci.pae.services;

import be.vinci.pae.domain.DomainFactory;
import jakarta.inject.Inject;

public class UserDSImpl implements UserDS {

  @Inject
  private static DomainFactory myDomainFactory;

}
