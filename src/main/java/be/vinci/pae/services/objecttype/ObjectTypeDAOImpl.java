package be.vinci.pae.services.objecttype;

import be.vinci.pae.services.DALServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ObjectTypeDAO class that implements ObjectTypeDAO interface Provide the different methods.
 */
public class ObjectTypeDAOImpl implements ObjectTypeDAO {

  @Inject
  private DALServices myDALServices;

  /**
   * Get the object type by the id.
   *
   * @param id of the object type
   * @return the object type String corresponding to the id
   */
  @Override
  public String getOneById(int id) {
    String request = "SELECT label FROM pae.object_types WHERE id_object_type = ?;";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getString("label");
        }
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return null;
  }

  /**
   * Return the id corresponding to the type label.
   *
   * @param type the type label
   * @return the id corresponding
   */
  public int getIdByString(String type) {
    String request = "SELECT id_object_type FROM pae.object_types WHERE label = ?;";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setString(1, type);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id_object_type");
        }
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return 0;
  }
}
