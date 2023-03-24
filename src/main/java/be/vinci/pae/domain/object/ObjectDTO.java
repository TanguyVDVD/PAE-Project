package be.vinci.pae.domain.object;

import be.vinci.pae.domain.user.UserDTO;

/**
 * ObjectDTO interface. Representing a data transfer object (DTO) for a object in the domain The
 * interface only contains getter and setter.
 */
public interface ObjectDTO {

  /**
   * Return the id of an object.
   *
   * @return an int corresponding to the id of an object
   */
  int getId();

  /**
   * set the id of an object.
   *
   * @param id the id of an object
   */
  void setId(int id);

  /**
   * Return the description of an object.
   *
   * @return a String corresponding to the description of an object
   */
  String getDescription();

  /**
   * set the description of an object.
   *
   * @param description the description of an object
   */
  void setDescription(String description);

  /**
   * Return the path of the photo of an object.
   *
   * @return a String corresponding to the path of the photo of an object
   */
  boolean getPhoto();

  /**
   * set the description of an object.
   *
   * @param photo the path of the photo of an object
   */
  void setPhoto(boolean photo);

  /**
   * Return the visibility of an object.
   *
   * @return true if the object is visible, else false
   */
  Boolean getisVisible();

  /**
   * set the visibility of an object.
   *
   * @param isVisible the visibility of an object
   */
  void setIsVisible(boolean isVisible);

  /**
   * Return the price of an object.
   *
   * @return a double corresponding to the price of an object
   */
  double getPrice();

  /**
   * set the price of an object.
   *
   * @param price the price of an object
   */
  void setPrice(double price);

  /**
   * Return the state of an object.
   *
   * @return a String corresponding to the state of an object
   */
  String getState();

  /**
   * set the state of an object.
   *
   * @param state the password of an object
   */
  void setState(String state);

  /**
   * Return the offer date of an object.
   *
   * @return a String corresponding to the offer date of the object
   */
  String getOfferDate();

  /**
   * set the acceptance date of an object.
   *
   * @param offerDate the acceptance date of an object
   */
  void setOfferDate(String offerDate);

  /**
   * Return the acceptance date of an object.
   *
   * @return a String corresponding to the acceptance date of the object
   */
  String getAcceptanceDate();

  /**
   * set the acceptance date of an object.
   *
   * @param acceptanceDate the acceptance date of an object
   */
  void setAcceptanceDate(String acceptanceDate);

  /**
   * Return the refusal date of an object.
   *
   * @return a String corresponding to the refusal date of the object
   */
  String getRefusalDate();

  /**
   * set the refusal date of an object.
   *
   * @param refusalDate the refusal date of an object
   */
  void setRefusalDate(String refusalDate);

  /**
   * Return the deposit date of an object.
   *
   * @return a String corresponding to the deposit date of the object
   */
  String getDepositDate();

  /**
   * set the deposit date of an object.
   *
   * @param depositDate the deposit date of an object
   */
  void setDepositDate(String depositDate);

  /**
   * Return the selling date of an object.
   *
   * @return a String corresponding to the selling date of an object
   */
  String getSellingDate();

  /**
   * set the selling date of an object.
   *
   * @param sellingDate the selling date of an object
   */
  void setSellingDate(String sellingDate);

  /**
   * Return the withdrawal date of an object.
   *
   * @return a String corresponding to the withdrawal date of an object
   */
  String getWithdrawalDate();

  /**
   * set the withdrawal date of an object.
   *
   * @param withdrawalDate the withdrawal date of an object
   */
  void setWithdrawalDate(String withdrawalDate);

  /**
   * Return the time slot of an object.
   *
   * @return a String corresponding to the time slot of an object
   */
  String getTimeSlot();

  /**
   * set the time slot of an object.
   *
   * @param timeSlot the time slot of an object
   */
  void setTimeSlot(String timeSlot);

  /**
   * Return the status of an object.
   *
   * @return a String corresponding to the status of an object
   */
  String getStatus();

  /**
   * set the status of an object.
   *
   * @param status the status of the object
   */
  void setStatus(String status);

  /**
   * Return the reason for refusal of an object.
   *
   * @return a String corresponding to the reason for refusal of an object
   */
  String getReasonForRefusal();

  /**
   * set the reason for refusal of an object.
   *
   * @param reasonForRefusal the reason for refusal of an object
   */
  void setReasonForRefusal(String reasonForRefusal);

  /**
   * Return the phone number of an object.
   *
   * @return a String corresponding to the phone number of an object
   */
  String getPhoneNumber();

  /**
   * set the phone number of an object.
   *
   * @param phoneNumber the phone number of an object
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Return the pickup date of an object.
   *
   * @return a String corresponding to the pickup date of an object
   */
  String getPickupDate();

  /**
   * set the pickup date of an object.
   *
   * @param pickupDate the pickup date of an object
   */
  void setPickupDate(String pickupDate);

  /**
   * Return the user of an object.
   *
   * @return a UserDTO corresponding to the user of an object
   */
  UserDTO getUser();

  /**
   * set the user of an object.
   *
   * @param user the user of an object
   */
  void setUser(UserDTO user);

  /**
   * Return the type of the object.
   *
   * @return a String corresponding to the type of the object
   */
  String getObjectType();

  /**
   * set the type of the object.
   *
   * @param objectType the type of the object
   */
  void setObjectType(String objectType);

  /**
   * Return the date an object was dropped in the workshop.
   *
   * @return a String corresponding to the workshop deposit date
   */
  String getWorkshopDate();

  /**
   * Set the date an object was dropped in the workshop.
   *
   * @param workshopDate the date to set
   */
  void setWorkshopDate(String workshopDate);

}
