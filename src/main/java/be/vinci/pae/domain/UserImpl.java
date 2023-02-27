package be.vinci.pae.domain;

import java.util.Date;

public class UserImpl implements UserDTO {

  private String lastName;
  private String firstName;
  private String phoneNumber;
  private String email;
  private String password;
  private String photo;
  private Date registerDate;
  private boolean isHelper;

  /**
   * @return the last name of a user
   */
  @Override
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName set the last name of a user
   */
  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the first name of a user
   */
  @Override
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName set the first name of a user
   */
  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the phone number of a user
   */
  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * @param phoneNumber set the phone number of a user
   */
  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * @return the email of a user
   */
  @Override
  public String getEmail() {
    return email;
  }

  /**
   * @param email set the email of a user
   */
  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the password of a user
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * @param password set the password of a user
   */
  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the photo link of the user
   */
  @Override
  public String getPhoto() {
    return photo;
  }

  /**
   * @param photo set the new photo link of a user
   */
  @Override
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  /**
   * @return the register date of a user
   */
  @Override
  public Date getRegisterDate() {
    return registerDate;
  }

  /**
   * @param registerDate set de register date of a user
   */
  @Override
  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
  }

  /**
   * @return true if the user is a helper false if he is not
   */
  @Override
  public boolean isHelper() {
    return isHelper;
  }

  /**
   * @param isHelper set the boolean value corresponding if the user is a helper
   */
  @Override
  public void setIsHelper(boolean isHelper) {
    this.isHelper = isHelper;
  }

  /**
   * @return true if the password is correct, false if he is not
   */
  @Override
  public boolean isPasswordCorrect() {
    return false;
  }
}
