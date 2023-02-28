package be.vinci.pae.domain;

import java.util.Date;

/**
 * UserDTO interface. Representing a data transfer object (DTO) for a user in the domain The
 * interface only contains getter and setter.
 */
public interface UserDTO extends User {

  String getLastName();

  void setLastName(String lastName);

  String getFirstName();

  void setFirstName(String fristName);

  String getPhoneNumber();

  void setPhoneNumber(String phoneNumber);

  String getEmail();

  void setEmail(String email);

  String getPassword();

  void setPassword(String password);

  String getPhoto();

  void setPhoto(String photo);

  Date getRegisterDate();

  void setRegisterDate(Date registerDate);

  boolean isHelper();

  void setIsHelper(boolean isHelper);
}
