package be.vinci.pae.domain.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * UserDTO interface. Representing a data transfer object (DTO) for a user in the domain The
 * interface only contains getter and setter.
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  /**
   * Return the id of a user.
   *
   * @return an int corresponding to the id of a user
   */
  int getId();

  /**
   * set the id of a user.
   *
   * @param id the id of a user
   */
  void setId(int id);

  /**
   * Return the last name of a user.
   *
   * @return a String corresponding to the last name of a user
   */
  String getLastName();

  /**
   * set the last name of a user.
   *
   * @param lastName the last name of a user
   */
  void setLastName(String lastName);

  /**
   * Return the first name of a user.
   *
   * @return a String corresponding to the first name of a user
   */
  String getFirstName();

  /**
   * set the first name of a user.
   *
   * @param firstName the first name of a user
   */
  void setFirstName(String firstName);

  /**
   * Return the phone number of a user.
   *
   * @return a String corresponding to the phone number of a user
   */
  String getPhoneNumber();

  /**
   * set the phone number of a user.
   *
   * @param phoneNumber the phone number of a user
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Return the email of a user.
   *
   * @return a String corresponding to the email of a user
   */
  String getEmail();

  /**
   * set the email of a user.
   *
   * @param email the email of a user
   */
  void setEmail(String email);

  /**
   * Return the password of a user.
   *
   * @return a String corresponding to the password of a user
   */
  String getPassword();

  /**
   * set the password of a user.
   *
   * @param password the password of a user
   */
  void setPassword(String password);

  /**
   * Return if a user has a photo.
   *
   * @return a boolean corresponding to the state of the photo
   */
  Boolean getPhoto();

  /**
   * Set whether a user has a photo.
   *
   * @param hasPhoto the state of the photo
   */
  void setPhoto(Boolean hasPhoto);

  /**
   * Return the register date of a user.
   *
   * @return a date corresponding to the register date of a user
   */
  LocalDate getRegisterDate();

  /**
   * set the register date of a user.
   *
   * @param registerDate the register date of a user
   */
  void setRegisterDate(LocalDate registerDate);

  /**
   * Return a boolean corresponding if a user is a helper.
   *
   * @return true if the user is a helper false if he is not
   */
  Boolean getIsHelper();

  /**
   * set the boolean value corresponding if the user is a helper.
   *
   * @param isHelper the value
   */
  void setIsHelper(Boolean isHelper);

  /**
   * Return the version number of a user.
   *
   * @return an int corresponding to the version of a user
   */
  int getVersionNumber();

  /**
   * set the version number of a user.
   *
   * @param version the version number of a user
   */
  void setVersionNumber(int version);
}
