package be.vinci.pae.domain.user;

import be.vinci.pae.utils.Config;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
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
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private Boolean photo;
  private LocalDate registerDate;
  private Boolean isHelper;
  private int versionNumber;

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
   * Return if a user has a photo.
   *
   * @return a boolean corresponding to the state of the photo
   */
  @Override
  public Boolean getPhoto() {
    return photo;
  }

  /**
   * Set whether a user has a photo.
   *
   * @param hasPhoto the state of the photo
   */
  @Override
  public void setPhoto(Boolean hasPhoto) {
    this.photo = hasPhoto;
  }

  /**
   * Return the register date of a user.
   *
   * @return a date corresponding to the register date of a user
   */
  @Override
  public LocalDate getRegisterDate() {
    return registerDate;
  }

  /**
   * set the register date of a user.
   *
   * @param registerDate the register date of a user
   */
  @Override
  public void setRegisterDate(LocalDate registerDate) {
    this.registerDate = registerDate;
  }

  /**
   * Return a boolean corresponding if a user is a helper.
   *
   * @return true if the user is a helper false if he is not
   */
  @Override
  public Boolean getIsHelper() {
    return isHelper;
  }

  /**
   * set the boolean value corresponding if the user is a helper.
   *
   * @param isHelper the value
   */
  @Override
  public void setIsHelper(Boolean isHelper) {
    this.isHelper = isHelper;
  }

  /**
   * Return the version number of a user.
   *
   * @return an int corresponding to the version number of a user
   */
  @Override
  public int getVersionNumber() {
    return versionNumber;
  }

  /**
   * set the version number of a user.
   *
   * @param versionNumber the version number of a user
   */
  @Override
  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
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
   * Get the profile picture of the user.
   *
   * @return the profile picture of the user
   */
  @Override
  public File profilePictureFile() {
    String blobPath = Config.getProperty("BlobPath");

    File file = new File(blobPath, "user-" + id + ".jpg");

    return file.exists() ? file : null;
  }

  /**
   * Save the profile picture of the user.
   *
   * @param photo the photo to set
   * @return whether the saving was successful
   */
  @Override
  public boolean saveProfilePicture(InputStream photo) {
    String blobPath = Config.getProperty("BlobPath");

    try {
      // Create the blob directory if it doesn't exist
      Files.createDirectories(Paths.get(blobPath));

      // Resize photo and save
      Thumbnails
          .of(photo)
          .crop(Positions.CENTER)
          .size(128, 128)
          .outputFormat("jpg")
          .toFile(new File(blobPath, "user-" + id + ".jpg"));
    } catch (Exception e) {
      return false;
    }

    return true;
  }
}
