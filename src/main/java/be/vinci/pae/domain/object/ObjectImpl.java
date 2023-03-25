package be.vinci.pae.domain.object;

import be.vinci.pae.domain.user.UserDTO;
import java.sql.Date;
import java.time.LocalDate;

/**
 * ObjectImpl class that implements the ObjectDTO interface Contains all the attribute of an
 * object.
 */
public class ObjectImpl implements Object {

  private int id;
  private String description;
  private boolean photo;
  private boolean isVisible;
  private double price;
  private String state;
  private String offerDate;
  private String acceptanceDate;
  private String refusalDate;
  private String workshopDate;
  private String depositDate;
  private String sellingDate;
  private String withdrawalDate;
  private String onSaleDate;
  private String timeSlot;
  private String status;
  private String reasonForRefusal;
  private String phoneNumber;
  private String receiptDate;
  private UserDTO user;
  private String objectType;

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
   * Return the path of the photo of an object.
   *
   * @return a String corresponding to the path of the photo of an object
   */
  @Override
  public boolean getPhoto() {
    return photo;
  }

  /**
   * set the description of an object.
   *
   * @param photo the path of the photo of an object
   */
  @Override
  public void setPhoto(boolean photo) {
    this.photo = photo;
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
  public String getOfferDate() {
    return offerDate;
  }

  /**
   * set the acceptance date of an object.
   *
   * @param offerDate the acceptance date of an object
   */
  @Override
  public void setOfferDate(String offerDate) {
    this.offerDate = offerDate;
  }

  /**
   * Return the acceptance date of an object.
   *
   * @return a String corresponding to the acceptance date of the object
   */
  @Override
  public String getAcceptanceDate() {
    return acceptanceDate;
  }

  /**
   * set the acceptance date of an object.
   *
   * @param acceptanceDate the acceptance date of an object
   */
  @Override
  public void setAcceptanceDate(String acceptanceDate) {
    this.acceptanceDate = acceptanceDate;
  }

  /**
   * Return the refusal date of an object.
   *
   * @return a String corresponding to the refusal date of the object
   */
  @Override
  public String getRefusalDate() {
    return refusalDate;
  }

  /**
   * set the refusal date of an object.
   *
   * @param refusalDate the refusal date of an object
   */
  @Override
  public void setRefusalDate(String refusalDate) {
    this.refusalDate = refusalDate;
  }

  /**
   * Return the date an object was dropped in the workshop.
   *
   * @return a String corresponding to the workshop deposit date
   */
  @Override
  public String getWorkshopDate() {
    return workshopDate;
  }

  /**
   * Set the date an object was dropped in the workshop.
   *
   * @param workshopDate the date to set
   */
  @Override
  public void setWorkshopDate(String workshopDate) {
    this.workshopDate = workshopDate;
  }

  /**
   * Return the deposit date of an object.
   *
   * @return a String corresponding to the deposit date of the object
   */
  @Override
  public String getDepositDate() {
    return depositDate;
  }

  /**
   * set the deposit date of an object.
   *
   * @param depositDate the deposit date of an object
   */
  @Override
  public void setDepositDate(String depositDate) {
    this.depositDate = depositDate;
  }

  /**
   * Return the selling date of an object.
   *
   * @return a String corresponding to the selling date of an object
   */
  @Override
  public String getSellingDate() {
    return sellingDate;
  }

  /**
   * set the selling date of an object.
   *
   * @param sellingDate the selling date of an object
   */
  @Override
  public void setSellingDate(String sellingDate) {
    this.sellingDate = sellingDate;
  }

  /**
   * Return the withdrawal date of an object.
   *
   * @return a String corresponding to the withdrawal date of an object
   */
  @Override
  public String getWithdrawalDate() {
    return withdrawalDate;
  }

  /**
   * set the withdrawal date of an object.
   *
   * @param withdrawalDate the withdrawal date of an object
   */
  @Override
  public void setWithdrawalDate(String withdrawalDate) {
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
  public String getReceiptDate() {
    return receiptDate;
  }

  /**
   * set the receipt date of an object.
   *
   * @param receiptDate the receipt date of an object
   */
  @Override
  public void setReceiptDate(String receiptDate) {
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
  public String getOnSaleDate() {
    return onSaleDate;
  }

  @Override
  public void setOnSaleDate(String date) {
    this.onSaleDate = date;
  }

  /**
   * Give the current date.
   *
   * @return today's date (sql)
   */
  @Override
  public Date getCurrentDate() {
    return Date.valueOf(LocalDate.now());
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
}
