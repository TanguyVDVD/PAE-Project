package be.vinci.pae.ucc.availability;

import be.vinci.pae.domain.availability.AvailabilityDTO;
import java.util.List;

/**
 * AvailabilityUCC interface that provide the methods of an availability.
 */
public interface AvailabilityUCC {

  /**
   * Returns a list of all availabilities.
   *
   * @return a list of all availabilities
   */
  List<AvailabilityDTO> getAvailabilities();

}
