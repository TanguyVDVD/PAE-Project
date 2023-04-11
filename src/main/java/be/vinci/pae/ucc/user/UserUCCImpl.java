package be.vinci.pae.ucc.user;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.DALServices;
import be.vinci.pae.services.user.UserDAO;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;


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

      if (userDB == null) {
        return null;
      }

      if (!userDB.isPasswordCorrect(password)) {
        return null;
      }

      return userDB;
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      MyLogger.log(Level.INFO, "Erreur lors de la connexion");
      throw new WebApplicationException("Erreur lors de la connexion",
          Status.INTERNAL_SERVER_ERROR);
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
      // Check email format
      if (!User.emailIsValid(userDTO.getEmail())) {
        MyLogger.log(Level.INFO, "Adresse mail invalide");
        throw new WebApplicationException("Adresse mail invalide", Response.Status.BAD_REQUEST);
      }

      // Check phone number format
      String phone = User.formatPhoneNumber(userDTO.getPhoneNumber());
      if (phone == null) {
        MyLogger.log(Level.INFO, "Numéro de téléphone invalide");
        throw new WebApplicationException("Numéro de téléphone invalide",
            Response.Status.BAD_REQUEST);
      }
      userDTO.setPhoneNumber(phone);

      // Check if email or phone number already exists
      if (myUserDAO.getOneByEmail(userDTO.getEmail()) != null) {
        MyLogger.log(Level.INFO, "Adresse mail déja utilisé");
        throw new WebApplicationException("Adresse mail déja utilisé", Response.Status.BAD_REQUEST);
      }

      if (myUserDAO.getOneByPhoneNumber(userDTO.getPhoneNumber()) != null) {
        MyLogger.log(Level.INFO, "Numéro de GSM déjà utilisé");
        throw new WebApplicationException("Numéro de GSM déjà utilisé",
            Response.Status.BAD_REQUEST);
      }

      User userTemp = (User) userDTO;
      userTemp.setPassword(User.hashPassword(userTemp.getPassword()));

      userTemp.setVersionNumber(1);

      int id = myUserDAO.insert(userTemp);

      if (id == -1) {
        return null;
      }

      userTemp.setId(id);

      return userTemp;

    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      if (e instanceof WebApplicationException) {
        throw e;
      }
      MyLogger.log(Level.INFO, "Erreur lors de l'inscription");
      throw new WebApplicationException("Erreur lors de l'inscription",
          Status.INTERNAL_SERVER_ERROR);
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
      MyLogger.log(Level.INFO, "Erreur lors de la récupération des utilisateurs");
      throw new WebApplicationException("Erreur lors de la récupération des utilisateurs",
          Status.INTERNAL_SERVER_ERROR);
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
      MyLogger.log(Level.INFO, "Erreur lors de la récupération de l'utilisateur");
      throw new WebApplicationException("Erreur lors de la récupération de l'utilisateur",
          Status.INTERNAL_SERVER_ERROR);
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
        MyLogger.log(Level.INFO, "Mot de passe incorrect");
        throw new WebApplicationException("Mot de passe incorrect", Response.Status.BAD_REQUEST);
      }

      // Check if email is valid and not already used
      String email = userDTO.getEmail();
      if (email != null && !email.equals(userDB.getEmail())) {
        if (!User.emailIsValid(email)) {
          MyLogger.log(Level.INFO, "Adresse mail invalide");
          throw new WebApplicationException("Adresse mail invalide", Response.Status.BAD_REQUEST);
        }

        if (myUserDAO.getOneByEmail(email) != null) {
          MyLogger.log(Level.INFO, "Adresse mail déja utilisé");
          throw new WebApplicationException("Adresse mail déja utilisé",
              Response.Status.BAD_REQUEST);
        }
      }

      // Check if phone number is valid and not already used
      String phoneNumber = userDTO.getPhoneNumber();
      if (phoneNumber != null) {
        String formattedPhoneNumber = User.formatPhoneNumber(phoneNumber);

        if (formattedPhoneNumber == null) {
          MyLogger.log(Level.INFO, "Numéro de GSM invalide");
          throw new WebApplicationException("Numéro de GSM invalide", Response.Status.BAD_REQUEST);
        }

        if (!formattedPhoneNumber.equals(userDB.getPhoneNumber())) {
          if (myUserDAO.getOneByPhoneNumber(formattedPhoneNumber) != null) {
            MyLogger.log(Level.INFO, "Numéro de GSM déjà utilisé");
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

    } catch (Exception e) {
      myDalServices.rollbackTransaction();

      if (e instanceof WebApplicationException) {
        throw e;
      }
      MyLogger.log(Level.INFO, "Erreur lors de la mise à jour de l'utilisateur");
      throw new WebApplicationException("Erreur lors de la mise à jour de l'utilisateur",
          Status.INTERNAL_SERVER_ERROR);
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

  @Override
  public File getProfilePicture(UserDTO userDTO) {
    User user = (User) userDTO;

    return user.profilePictureFile();
  }

  @Override
  public UserDTO updateProfilePicture(UserDTO userDTO, InputStream profilePicture) {
    myDalServices.startTransaction();

    try {
      User user = (User) myUserDAO.getOneById(userDTO.getId());

      if (user == null) {
        return null;
      }

      user.saveProfilePicture(profilePicture);

      user.setPhoto(true);

      if (!myUserDAO.update(user)) {
        return null;
      }

      return myUserDAO.getOneById(userDTO.getId());
    } catch (Exception e) {
      myDalServices.rollbackTransaction();
      MyLogger.log(Level.INFO, "rreur lors de la mise à jour de la photo de profil");
      throw new WebApplicationException("Erreur lors de la mise à jour de la photo de profil",
          Status.INTERNAL_SERVER_ERROR);
    } finally {
      myDalServices.commitTransaction();
    }
  }

}
