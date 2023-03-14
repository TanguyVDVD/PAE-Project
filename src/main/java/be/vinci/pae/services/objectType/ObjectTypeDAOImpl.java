package be.vinci.pae.services.objectType;

import be.vinci.pae.services.DALServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    String request = "SELECT label FROM pae.object_types WHERE id_object_type = ?";

    try (PreparedStatement ps = myDALServices.getPreparedStatement(request)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          rs.getString("label");
        }
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return null;
  }
}
