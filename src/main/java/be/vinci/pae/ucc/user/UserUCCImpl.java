package be.vinci.pae.ucc.user;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.user.UserDAO;
import be.vinci.pae.utils.exceptions.UserException;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
import java.io.InputStream;
import java.util.List;


/**
 * UserUCCImpl class that implements the UserUCC interface provide the declared class login and
 * logout.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private DALServices myDalServices;

  /**
   * Method that login a user if the parameters are correct.
   *
   * @param email    the email of user
   * @param password the password of user
   * @return the user that has been logged
   */
  @Override
  public UserDTO login(String email, String password) {
    myDalServices.startTransaction();

    try {
      User userDB = (User) myUserDAO.getOneByEmail(email);

      if (userDB == null || !userDB.isPasswordCorrect(password)) {
        throw new UserException("Adresse mail ou mot de passe incorrect");
      }

      return userDB;
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Method that register a user if the parameters are correct.
   *
   * @param userDTO the user to register
   * @return the user that has been registered
   */
  @Override
  public UserDTO register(UserDTO userDTO) {
    myDalServices.startTransaction();

    try {
      // Check if email or phone number already exists
      if (myUserDAO.getOneByEmail(userDTO.getEmail()) != null) {
        throw new UserException("Adresse mail déjà utilisé");
      }

      if (myUserDAO.getOneByPhoneNumber(userDTO.getPhoneNumber()) != null) {
        throw new UserException("Numéro de GSM déjà utilisé");
      }

      userDTO.setPassword(User.hashPassword(userDTO.getPassword()));

      userDTO.setVersionNumber(1);

      return myUserDAO.insert(userDTO);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Returns a list of all users.
   *
   * @param query query to filter users
   * @return a list of all users
   */
  public List<UserDTO> getUsers(String query) {
    myDalServices.startTransaction();

    try {
      return myUserDAO.getAll(query);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Get a user by the id.
   *
   * @param id the user
   * @return the user corresponding to the id
   */
  @Override
  public UserDTO getUserById(int id) {
    myDalServices.startTransaction();

    try {
      return myUserDAO.getOneById(id);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Update a user, after verifying password.
   *
   * @param userDTO  the user to update
   * @param password the password of the user
   * @return the updated user
   */
  @Override
  public UserDTO updateUser(UserDTO userDTO, String password) {
    myDalServices.startTransaction();

    try {
      User userDB = (User) myUserDAO.getOneById(userDTO.getId());

      // Check if user exists
      if (userDB == null) {
        return null;
      }

      // Check if password is correct
      if (password != null && !userDB.isPasswordCorrect(password)) {
        throw new UserException("Mot de passe incorrect");
      }

      // Check if email is not already used
      String email = userDTO.getEmail();
      if (email != null && !email.equals(userDB.getEmail())
          && myUserDAO.getOneByEmail(email) != null) {
        throw new UserException("Adresse mail déjà utilisé");
      }

      // Check if phone number is not already used
      String phoneNumber = userDTO.getPhoneNumber();
      if (phoneNumber != null && !phoneNumber.equals(userDB.getPhoneNumber())
          && myUserDAO.getOneByPhoneNumber(phoneNumber) != null) {
        throw new UserException("Numéro de GSM déjà utilisé");
      }

      // Hash password if it has been changed
      if (userDTO.getPassword() != null) {
        userDTO.setPassword(User.hashPassword(userDTO.getPassword()));
      }

      // Update the user
      return myUserDAO.update(userDTO);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }


  /**
   * Update a user.
   *
   * @param userDTO the user to update
   * @return the updated user
   */
  @Override
  public UserDTO updateUser(UserDTO userDTO) {
    return updateUser(userDTO, null);
  }

  /**
   * Get a user's profile picture.
   *
   * @param userDTO the user
   * @return the user's profile picture
   */
  @Override
  public File getProfilePicture(UserDTO userDTO) {
    User user = (User) userDTO;

    return user.profilePictureFile();
  }

  /**
   * Update a user's profile picture.
   *
   * @param userDTO        the user to update
   * @param password the password to verify
   * @param file     the new profile picture
   * @return the updated user
   */
  @Override
  public UserDTO updateProfilePicture(UserDTO userDTO, String password, InputStream file) {
    myDalServices.startTransaction();

    try {
      User user = (User) myUserDAO.getOneById(userDTO.getId());

      if (user == null) {
        return null;
      }

      if (!user.isPasswordCorrect(password)) {
        throw new UserException("Mot de passe incorrect");
      }

      user.saveProfilePicture(file);

      user.setPhoto(true);

      return myUserDAO.update(user);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

  /**
   * Remove a user's profile picture.
   *
   * @param userDTO  the user
   * @param password the password to verify
   * @return the updated user
   */
  @Override
  public UserDTO removeProfilePicture(UserDTO userDTO, String password) {
    myDalServices.startTransaction();

    try {
      User user = (User) myUserDAO.getOneById(userDTO.getId());

      if (user == null) {
        return null;
      }

      if (!user.isPasswordCorrect(password)) {
        throw new UserException("Mot de passe incorrect");
      }

      user.removeProfilePicture();

      user.setPhoto(false);

      return myUserDAO.update(user);
    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      throw e;
    } finally {
      myDalServices.commitTransaction();
    }
  }

}
