package be.vinci.pae.domain.notification;

/**
 * NotificationImpl class that implements the NotificationDTO interface Contains all the attribute
 * of a notification.
 */
public class NotificationImpl implements Notification {

  private int id;
  private String notificationText;
  private Boolean isRead;
  private int userId;
  private int objectId;


  /**
   * Return the id of a notification.
   *
   * @return an int corresponding to the id of the notification.
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * set the id of a notification.
   *
   * @param id the id of a notification
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Return the text of the notification.
   *
   * @return a String corresponding to the text of the notification.
   */
  @Override
  public String getNotificationText() {
    return notificationText;
  }

  /**
   * Set the notification text.
   *
   * @param notificationText The notification text to set
   */
  @Override
  public void setNotificationText(String notificationText) {
    this.notificationText = notificationText;
  }

  /**
   * Returns a boolean value indicating whether the notification has been read.
   *
   * @return true if the notification has been read, false otherwise.
   */
  @Override
  public Boolean getRead() {
    return isRead;
  }

  /**
   * Sets the read status of the notification.
   *
   * @param isRead the boolean value to set the read status of the notification
   */
  @Override
  public void setRead(Boolean isRead) {
    this.isRead = isRead;
  }

  /**
   * Returns the ID of the user who receive the notification.
   *
   * @return the ID of the user who receive the notification
   */
  @Override
  public int getIdUser() {
    return userId;
  }

  /**
   * Sets the ID of the user who receive the notification.
   *
   * @param idUser the ID of the user who reveive the notification
   */
  @Override
  public void setIdUser(int idUser) {
    this.userId = idUser;
  }

  /**
   * Get the ID of the object.
   *
   * @return The ID of the object
   */
  @Override
  public int getIdObject() {
    return objectId;
  }

  /**
   * Set the ID of the object.
   *
   * @param id The ID of the object to set
   */
  @Override
  public void setIdObject(int id) {
    this.objectId = id;
  }

}
