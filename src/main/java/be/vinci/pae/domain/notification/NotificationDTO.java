package be.vinci.pae.domain.notification;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * NotificationDTO interface. Representing a data transfer object (DTO) for a notification in the
 * domain The interface only contains getter and setter.
 */
@JsonDeserialize(as = NotificationImpl.class)
public interface NotificationDTO {

  /**
   * Return the id of a notification.
   *
   * @return an int corresponding to the id of the notification.
   */
  int getId();

  /**
   * set the id of a notification.
   *
   * @param id the id of a notification
   */
  void setId(int id);

  /**
   * Return the text of the notification.
   *
   * @return a String corresponding to the text of the notification.
   */
  String getNotificationText();

  /**
   * Set the notification text.
   *
   * @param notificationText The notification text to set
   */
  void setNotificationText(String notificationText);

  /**
   * Returns a boolean value indicating whether the notification has been read.
   *
   * @return true if the notification has been read, false otherwise.
   */
  Boolean getRead();

  /**
   * Sets the read status of the notification.
   *
   * @param isRead the boolean value to set the read status of the notification
   */
  void setRead(Boolean isRead);

  /**
   * Returns the ID of the user who receive the notification.
   *
   * @return the ID of the user who receive the notification
   */
  int getIdUser();

  /**
   * Sets the ID of the user who receive the notification.
   *
   * @param idUser the ID of the user who reveive the notification
   */
  void setIdUser(int idUser);

  /**
   * Get the ID of the object.
   *
   * @return The ID of the object
   */
  int getIdObject();

  /**
   * Set the ID of the object.
   *
   * @param id The ID of the object to set
   */
  void setIdObject(int id);

}
