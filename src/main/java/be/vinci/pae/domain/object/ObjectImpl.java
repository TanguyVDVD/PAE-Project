package be.vinci.pae.domain.object;

import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exceptions.UserException;
import java.awt.Color;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * ObjectImpl class that implements the ObjectDTO interface Contains all the attribute of an
 * object.
 */
public class ObjectImpl implements Object {

  private int id;
  private String description;
  private boolean isVisible;
  private double price;
  private String state;
  private LocalDate offerDate;
  private LocalDate acceptanceDate;
  private LocalDate refusalDate;
  private LocalDate workshopDate;
  private LocalDate depositDate;
  private LocalDate sellingDate;
  private LocalDate withdrawalDate;
  private LocalDate onSaleDate;
  private String timeSlot;
  private String status;
  private String reasonForRefusal;
  private String phoneNumber;
  private LocalDate receiptDate;
  private UserDTO user;
  private String objectType;
  private int versionNumber;

  /**
   * Return the id of an object.
   *
   * @return an int corresponding to the id of an object
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * set the id of an object.
   *
   * @param id the id of an object
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Return the description of an object.
   *
   * @return a String corresponding to the description of an object
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * set the description of an object.
   *
   * @param description the description of an object
   */
  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Return the visibility of an object.
   *
   * @return true if the object is visible, else false
   */
  @Override
  public Boolean getisVisible() {
    return isVisible;
  }

  /**
   * set the visibility of an object.
   *
   * @param isVisible the visibility of an object
   */
  @Override
  public void setIsVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }

  /**
   * Return the price of an object.
   *
   * @return a double corresponding to the price of an object
   */
  @Override
  public double getPrice() {
    return price;
  }

  /**
   * set the price of an object.
   *
   * @param price the price of an object
   */
  @Override
  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * Return the state of an object.
   *
   * @return a String corresponding to the state of an object
   */
  @Override
  public String getState() {
    return state;
  }

  /**
   * set the state of an object.
   *
   * @param state the password of an object
   */
  @Override
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Return the offer date of an object.
   *
   * @return a String corresponding to the offer date of the object
   */
  @Override
  public LocalDate getOfferDate() {
    return offerDate;
  }

  /**
   * set the acceptance date of an object.
   *
   * @param offerDate the acceptance date of an object
   */
  @Override
  public void setOfferDate(LocalDate offerDate) {
    this.offerDate = offerDate;
  }

  /**
   * Return the acceptance date of an object.
   *
   * @return a String corresponding to the acceptance date of the object
   */
  @Override
  public LocalDate getAcceptanceDate() {
    return acceptanceDate;
  }

  /**
   * set the acceptance date of an object.
   *
   * @param acceptanceDate the acceptance date of an object
   */
  @Override
  public void setAcceptanceDate(LocalDate acceptanceDate) {
    this.acceptanceDate = acceptanceDate;
  }

  /**
   * Return the refusal date of an object.
   *
   * @return a LocalDate corresponding to the refusal date of the object
   */
  @Override
  public LocalDate getRefusalDate() {
    return refusalDate;
  }

  /**
   * set the refusal date of an object.
   *
   * @param refusalDate the refusal date of an object
   */
  @Override
  public void setRefusalDate(LocalDate refusalDate) {
    this.refusalDate = refusalDate;
  }

  /**
   * Return the date an object was dropped in the workshop.
   *
   * @return a String corresponding to the workshop deposit date
   */
  @Override
  public LocalDate getWorkshopDate() {
    return workshopDate;
  }

  /**
   * Set the date an object was dropped in the workshop.
   *
   * @param workshopDate the date to set
   */
  @Override
  public void setWorkshopDate(LocalDate workshopDate) {
    this.workshopDate = workshopDate;
  }

  /**
   * Return the deposit date of an object.
   *
   * @return a String corresponding to the deposit date of the object
   */
  @Override
  public LocalDate getDepositDate() {
    return depositDate;
  }

  /**
   * set the deposit date of an object.
   *
   * @param depositDate the deposit date of an object
   */
  @Override
  public void setDepositDate(LocalDate depositDate) {
    this.depositDate = depositDate;
  }

  /**
   * Return the selling date of an object.
   *
   * @return a String corresponding to the selling date of an object
   */
  @Override
  public LocalDate getSellingDate() {
    return sellingDate;
  }

  /**
   * set the selling date of an object.
   *
   * @param sellingDate the selling date of an object
   */
  @Override
  public void setSellingDate(LocalDate sellingDate) {
    this.sellingDate = sellingDate;
  }

  /**
   * Return the withdrawal date of an object.
   *
   * @return a String corresponding to the withdrawal date of an object
   */
  @Override
  public LocalDate getWithdrawalDate() {
    return withdrawalDate;
  }

  /**
   * set the withdrawal date of an object.
   *
   * @param withdrawalDate the withdrawal date of an object
   */
  @Override
  public void setWithdrawalDate(LocalDate withdrawalDate) {
    this.withdrawalDate = withdrawalDate;
  }

  /**
   * Return the time slot of an object.
   *
   * @return a String corresponding to the time slot of an object
   */
  @Override
  public String getTimeSlot() {
    return timeSlot;
  }

  /**
   * set the time slot of an object.
   *
   * @param timeSlot the time slot of an object
   */
  @Override
  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  /**
   * Return the status of an object.
   *
   * @return a String corresponding to the status of an object
   */
  @Override
  public String getStatus() {
    return status;
  }

  /**
   * set the status of an object.
   *
   * @param status the status of the object
   */
  @Override
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Return the reason for refusal of an object.
   *
   * @return a String corresponding to the reason for refusal of an object
   */
  @Override
  public String getReasonForRefusal() {
    return reasonForRefusal;
  }

  /**
   * set the reason for refusal of an object.
   *
   * @param reasonForRefusal the reason for refusal of an object
   */
  @Override
  public void setReasonForRefusal(String reasonForRefusal) {
    this.reasonForRefusal = reasonForRefusal;
  }

  /**
   * Return the phone number of an object.
   *
   * @return a String corresponding to the phone number of an object
   */
  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * set the phone number of an object.
   *
   * @param phoneNumber the phone number of an object
   */
  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Return the receipt date of an object.
   *
   * @return a date corresponding to the receipt date of an object
   */
  @Override
  public LocalDate getReceiptDate() {
    return receiptDate;
  }

  /**
   * set the receipt date of an object.
   *
   * @param receiptDate the receipt date of an object
   */
  @Override
  public void setReceiptDate(LocalDate receiptDate) {
    this.receiptDate = receiptDate;
  }

  /**
   * Return the user of an object.
   *
   * @return a UserDTO corresponding to the user of an object
   */
  @Override
  public UserDTO getUser() {
    return user;
  }

  /**
   * set the user of an object.
   *
   * @param user the user of an object
   */
  @Override
  public void setUser(UserDTO user) {
    this.user = user;
  }

  /**
   * Return the type of the object.
   *
   * @return a String corresponding to the type of the object
   */
  @Override
  public String getObjectType() {
    return objectType;
  }

  /**
   * set the type of the object.
   *
   * @param objectType the type of the object
   */
  @Override
  public void setObjectType(String objectType) {
    this.objectType = objectType;
  }


  @Override
  public LocalDate getOnSaleDate() {
    return onSaleDate;
  }

  @Override
  public void setOnSaleDate(LocalDate date) {
    this.onSaleDate = date;
  }

  /**
   * Return the version number of the object.
   *
   * @return an int corresponding to the version number of the object
   */
  @Override
  public int getVersionNumber() {
    return versionNumber;
  }

  /**
   * Set the version number of the object.
   *
   * @param versionNumber the version number of the object
   */
  @Override
  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }

  /**
   * Check if the object is already accepted or rejected.
   *
   * @param status the status of the object
   * @return true if the status of the object is already defined, else false
   */
  @Override
  public boolean isStatusAlreadyDefined(String status) {
    if (status == null) {
      return false;
    }
    return status.equals("accepté") || status.equals("refusé");
  }

  /**
   * Get the photo of the object.
   *
   * @return the photo of the object
   */
  @Override
  public File photoFile() {
    String blobPath = Config.getProperty("BlobPath");

    File file = new File(blobPath, "object-" + id + ".jpg");

    return file.exists() ? file : null;
  }

  /**
   * Save the photo of the object.
   *
   * @param photo the photo to set
   * @return whether the saving was successful
   */
  @Override
  public boolean savePhoto(InputStream photo) {
    String blobPath = Config.getProperty("BlobPath");

    try {
      // Create the blob directory if it doesn't exist
      Files.createDirectories(Paths.get(blobPath));

      // Resize photo and save
      Thumbnails
          .of(photo)
          .crop(Positions.CENTER)
          .size(400, 400)
          .addFilter(new Canvas(400, 400, Positions.CENTER, Color.WHITE))
          .outputFormat("jpg")
          .toFile(new File(blobPath, "object-" + id + ".jpg"));
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Check if the state is "en magasain" of "à l'atelier".
   *
   * @param objectDTO the object to check the state
   * @return true if the state correspond else false
   */
  @Override
  public boolean isStateWorkshopOrShop(ObjectDTO objectDTO) {
    return objectDTO.getState().equals("à l'atelier") || objectDTO.getState().equals("en magasin");
  }

  /**
   * Set the correct change state date.
   *
   * @param objectDTO       the object after the state change
   * @param objectDTOFromDb the object before the state change
   * @param dateChange      the date of de change
   * @return the objectDTO change, null if there is a problem
   */
  @Override
  public ObjectDTO setStateDate(ObjectDTO objectDTO, ObjectDTO objectDTOFromDb,
      LocalDate dateChange) {

    if (objectDTO == null || objectDTOFromDb == null || dateChange == null) {
      return null;
    }

    if (!objectDTO.getState().equals(objectDTOFromDb.getState())) {
      // Check if the object is already refused
      if (objectDTOFromDb.getState().equals("refusé")) {
        throw new UserException("L'objet est déjà refusé");
      }

      // Check if the object is already sold or withdrawn
      if (objectDTOFromDb.getState().equals("vendu") || objectDTOFromDb.getState()
          .equals("retiré")) {
        throw new UserException("L'objet est déjà vendu ou retiré");
      }

      // If the object is being sold, the previous state must be "on sale"
      if (objectDTO.getState().equals("vendu")) {
        if (!objectDTOFromDb.getState()
            .equals("en vente")) {
          throw new UserException("L'objet n'est pas en vente");
        }

        objectDTOFromDb.setSellingDate(dateChange);
      }

      // If the object is being withdrawn, the previous state must be "on sale", or "in shop"
      if (objectDTO.getState().equals("retiré")) {
        if (!objectDTOFromDb.getState().equals("en vente") && !objectDTOFromDb.getState()
            .equals("en magasin")) {
          throw new UserException("L'objet n'est pas en vente ou en magasin");
        }

        objectDTOFromDb.setWithdrawalDate(dateChange);
      }

      // If the object is being put on sale, the previous state must be "in shop"
      if (objectDTO.getState().equals("en vente")) {
        if (!objectDTOFromDb.getState().equals("en magasin")) {
          throw new UserException("L'objet n'est pas en magasin");
        }

        objectDTOFromDb.setOnSaleDate(dateChange);
      }

      // If the object is being put in the shop, the previous state must be "accepted", or "in workshop"
      if (objectDTO.getState().equals("en magasin")) {
        if (!objectDTOFromDb.getState().equals("accepté") && !objectDTOFromDb.getState()
            .equals("à l'atelier")) {
          throw new UserException("L'objet n'est pas accepté ou à l'atelier");
        }

        objectDTOFromDb.setDepositDate(dateChange);
      }

      // If the object is being put in the workshop, the previous state must be "accepted"
      if (objectDTO.getState().equals("à l'atelier")) {
        if (!objectDTOFromDb.getState().equals("accepté")) {
          throw new UserException("L'objet n'est pas accepté");
        }

        objectDTOFromDb.setWorkshopDate(dateChange);
      }
    }

    return objectDTO;
  }

}
