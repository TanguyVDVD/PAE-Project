package be.vinci.pae.services.objecttype;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.objecttype.ObjectTypeDTO;
import be.vinci.pae.services.DalBackendServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * ObjectTypeDAO class that implements ObjectTypeDAO interface Provide the different methods.
 */
public class ObjectTypeDAOImpl implements ObjectTypeDAO {

  @Inject
  private DalBackendServices dalBackendServices;

  @Inject
  private DomainFactory myDomainFactory;


  /**
   * Map a ResultSet to a ObjectTypeDTO.
   *
   * @param resultSet the ResultSet
   * @return the ObjectTypeDTO
   */
  public ObjectTypeDTO dtoFromRS(ResultSet resultSet) {
    ObjectTypeDTO objectType = myDomainFactory.getObjectType();

    try {
      objectType.setId(resultSet.getInt("id_object_type"));
      objectType.setLabel(resultSet.getString("label"));
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return objectType;
  }

  /**
   * Get all object types.
   *
   * @return the list of object types
   */
  @Override
  public Object getAll() {
    String request = "SELECT * FROM pae.object_types ORDER BY id_object_type";
    ArrayList<ObjectTypeDTO> objectTypes = new ArrayList<>();

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objectTypes.add(dtoFromRS(rs));
        }
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }

    return objectTypes;
  }
  
  /**
   * Get the object type by the id.
   *
   * @param id of the object type
   * @return the object type String corresponding to the id
   */
  @Override
  public String getOneById(int id) {
    String request = "SELECT label FROM pae.object_types WHERE id_object_type = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
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
   * @param label the type label
   * @return the id corresponding
   */
  public int getIdByLabel(String label) {
    String request = "SELECT id_object_type FROM pae.object_types WHERE label = ?;";

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(request)) {
      ps.setString(1, label);

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
