package be.vinci.pae.domain.user;

import java.io.File;
import java.io.InputStream;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

/**
 * User interface representing a user in the domain.
 */
public interface User extends UserDTO {

  /**
   * Check the validity of an email.
   *
   * @param email the email to check
   * @return boolean representing the validity of the email
   */
  static boolean emailIsValid(String email) {
    EmailValidator emailValidator = EmailValidator.getInstance();

    return emailValidator.isValid(email);
  }

  /**
   * Properly format a phone number.
   *
   * @param phoneNumber the phone number to format
   * @return the formatted phone number, or null if the phone number is not valid
   */
  static String formatPhoneNumber(String phoneNumber) {
    phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
    if (!phoneNumber.startsWith("0")) {
      phoneNumber = "0" + phoneNumber;
    }

    if (!phoneNumber.matches("^0[0-9]{7,9}$")) {
      return null;
    } else {
      return phoneNumber;
    }
  }

  /**
   * Method that hash a password.
   *
   * @param password the password to hash
   * @return the password when it's hash
   */
  static String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  /**
   * Return true if the password is correct, false if he is not.
   *
   * @param password the password to check
   * @return a boolean
   */
  boolean isPasswordCorrect(String password);

  /**
   * Get the profile picture of the user.
   *
   * @return the profile picture of the user
   */
  File profilePictureFile();

  /**
   * Save the profile picture of the user.
   *
   * @param photo the photo to set
   * @return whether the saving was successful
   */
  boolean saveProfilePicture(InputStream photo);

}
