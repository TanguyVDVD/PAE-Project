package be.vinci.pae.ucc.user;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.user.UserDAO;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * UserUCCImpl class that implements the UserUCC interface provide the declared class login and
 * logout.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  /**
   * Method that login a user if the parameters are correct.
   *
   * @param email    the email of user
   * @param password the password of user
   * @return the user that has been logged
   */
  @Override
  public UserDTO login(String email, String password) {

    User userDB = (User) myUserDAO.getOneByEmail(email);

    if (userDB == null) {
      return null;
    }

    if (!userDB.isPasswordCorrect(password)) {
      return null;
    }

    return userDB;
  }

  @Override
  public UserDTO register(UserDTO userDTO) {
    // Check email format
    if (!User.emailIsValid(userDTO.getEmail())) {
      throw new WebApplicationException("Adresse mail invalide", Response.Status.BAD_REQUEST);
    }

    // Check phone number format
    String phone = User.formatPhoneNumber(userDTO.getPhoneNumber());
    if (phone == null) {
      throw new WebApplicationException("Numéro de téléphone invalide",
          Response.Status.BAD_REQUEST);
    }
    userDTO.setPhoneNumber(phone);

    // Check if email or phone number already exists
    if (myUserDAO.getOneByEmail(userDTO.getEmail()) != null) {
      throw new WebApplicationException("Adresse mail déja utilisé", Response.Status.BAD_REQUEST);
    }

    if (myUserDAO.getOneByPhoneNumber(userDTO.getPhoneNumber()) != null) {
      throw new WebApplicationException("Numéro de GSM déjà utilisé", Response.Status.BAD_REQUEST);
    }

    User userTemp = (User) userDTO;
    userTemp.setPassword(User.hashPassword(userTemp.getPassword()));

    int id = myUserDAO.insert(userTemp);

    if (id == -1) {
      return null;
    }

    userTemp.setId(id);

    return userTemp;
  }

  public List<UserDTO> getUsers(String query) {
    return myUserDAO.getAll(query);
  }

  @Override
  public UserDTO getUserById(int id) {
    return myUserDAO.getOneById(id);
  }

  @Override
  public UserDTO updateUser(UserDTO userDTO) {
    User userDB = (User) myUserDAO.getOneById(userDTO.getId());

    // Check if user exists
    if (userDB == null) {
      return null;
    }

    // Check if email is valid and not already used
    String email = userDTO.getEmail();
    if (email != null && !email.equals(userDB.getEmail())) {
      if (!User.emailIsValid(email)) {
        throw new WebApplicationException("Adresse mail invalide", Response.Status.BAD_REQUEST);
      }

      if (myUserDAO.getOneByEmail(email) != null) {
        throw new WebApplicationException("Adresse mail déja utilisé", Response.Status.BAD_REQUEST);
      }
    }

    // Check if phone number is valid and not already used
    String phoneNumber = userDTO.getPhoneNumber();
    if (phoneNumber != null) {
      String formattedPhoneNumber = User.formatPhoneNumber(phoneNumber);

      if (formattedPhoneNumber == null) {
        throw new WebApplicationException("Numéro de GSM invalide", Response.Status.BAD_REQUEST);
      }

      if (!formattedPhoneNumber.equals(userDB.getPhoneNumber())) {
        if (myUserDAO.getOneByPhoneNumber(formattedPhoneNumber) != null) {
          throw new WebApplicationException("Numéro de GSM déjà utilisé",
              Response.Status.BAD_REQUEST);
        }

        userDTO.setPhoneNumber(formattedPhoneNumber);
      }
    }

    // Hash password if it has been changed
    if (userDTO.getPassword() != null) {
      userDTO.setPassword(User.hashPassword(userDTO.getPassword()));
    }

    // Update the user
    if (!myUserDAO.update(userDTO)) {
      return null;
    }

    // Return the updated user
    return myUserDAO.getOneById(userDTO.getId());
  }

  @Override
  public File getProfilePicture(int id) {
    String blobPath = Config.getProperty("BlobPath");

    File file = new File(blobPath, "user-" + id + ".jpg");

    return file.exists() ? file : null;
  }

  @Override
  public boolean updateProfilePicture(int id, InputStream photo) {
    // TODO: process the photo

    try {
      String blobPath = Config.getProperty("BlobPath");

      // Create the blob directory if it doesn't exist
      Files.createDirectories(Paths.get(blobPath));

      Files.copy(photo, Paths.get(blobPath, "user-" + id + ".jpg"));
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }

    // TODO: Update the user in the database

    return true;
  }
}
