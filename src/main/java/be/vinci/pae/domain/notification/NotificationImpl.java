package be.vinci.pae.domain.notification;

/**
 * NotificationImpl class that implements the NotificationDTO interface Contains all the attribute
 * of a notification.
 */
public class NotificationImpl implements Notification {

  private int id;
  private String notificationText;
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
   * Get the ID of the object.
   *
   * @return The ID of the object
   */
  @Override
  public int getIDObject() {
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
