package be.vinci.pae.domain;

import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

/**
 * UserImpl class that implements the UserDTO interface Contains all the attribute of a user.
 */
public class UserImpl implements User {

  private int id;
  private String lastName;
  private String firstName;
  private String phoneNumber;
  private String email;
  private String password;
  private String photo;
  private Date registerDate;
  private boolean isHelper;

  /**
   * Return the id of a user.
   *
   * @return an int corresponding to the id of a user
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * set the id of a user.
   *
   * @param id the id of a user
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Return the last name of a user.
   *
   * @return a String corresponding to the last name of a user
   */
  @Override
  public String getLastName() {
    return lastName;
  }

  /**
   * set the last name of a user.
   *
   * @param lastName the last name of a user
   */
  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Return the first name of a user.
   *
   * @return a String corresponding to the first name of a user
   */
  @Override
  public String getFirstName() {
    return firstName;
  }

  /**
   * set the first name of a user.
   *
   * @param firstName the first name of a user
   */
  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Return the phone number of a user.
   *
   * @return a String corresponding to the phone number of a user
   */
  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * set the phone number of a user.
   *
   * @param phoneNumber the phone number of a user
   */
  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Return the email of a user.
   *
   * @return a String corresponding to the email of a user
   */
  @Override
  public String getEmail() {
    return email;
  }

  /**
   * set the email of a user.
   *
   * @param email the email of a user
   */
  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Return the password of a user.
   *
   * @return a String corresponding to the password of a user
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * set the password of a user.
   *
   * @param password the password of a user
   */
  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Return the photo link of a user.
   *
   * @return a String corresponding to the link of the photo
   */
  @Override
  public String getPhoto() {
    return photo;
  }

  /**
   * set the new photo link of a user.
   *
   * @param photo the new photo link of a user
   */
  @Override
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  /**
   * Return the register date of a user.
   *
   * @return a date corresponding to the register date of a user
   */
  @Override
  public Date getRegisterDate() {
    return registerDate;
  }

  /**
   * set the register date of a user.
   *
   * @param registerDate the register date of a user
   */
  @Override
  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
  }

  /**
   * Return a boolean corresponding if a user is a helper.
   *
   * @return true if the user is a helper false if he is not
   */
  @Override
  public boolean isHelper() {
    return isHelper;
  }

  /**
   * set the boolean value corresponding if the user is a helper.
   *
   * @param isHelper the value
   */
  @Override
  public void setIsHelper(boolean isHelper) {
    this.isHelper = isHelper;
  }

  /**
   * Return true if the password is correct, false if he is not.
   *
   * @return a boolean
   */
  @Override
  public boolean isPasswordCorrect(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  /**
   * Method that hash a password.
   *
   * @param password the password to hash
   * @return the password when it's hash
   */
  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

}
