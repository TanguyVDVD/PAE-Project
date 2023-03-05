package be.vinci.pae.domain;

import java.util.Date;

/**
 * UserDTO interface. Representing a data transfer object (DTO) for a user in the domain The
 * interface only contains getter and setter.
 */
public interface UserDTO {

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
   * Return the photo link of a user.
   *
   * @return a String corresponding to the link of the photo
   */
  String getPhoto();

  /**
   * set the new photo link of a user.
   *
   * @param photo the new photo link of a user
   */
  void setPhoto(String photo);

  /**
   * Return the register date of a user.
   *
   * @return a date corresponding to the register date of a user
   */
  Date getRegisterDate();

  /**
   * set the register date of a user.
   *
   * @param registerDate the register date of a user
   */
  void setRegisterDate(Date registerDate);

  /**
   * Return a boolean corresponding if a user is a helper.
   *
   * @return true if the user is a helper false if he is not
   */
  boolean isHelper();

  /**
   * set the boolean value corresponding if the user is a helper.
   *
   * @param isHelper the value
   */
  void setIsHelper(boolean isHelper);
}
