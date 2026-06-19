package dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DTipoVehiculo {

    public static final String[] HEADERS = {"ID", "NOMBRE", "DESCRIPCION"};

    private final DBConeccion connection;

    public DTipoVehiculo() {
        connection = new DBConeccion();
    }

    public void guardar(String nombre, String descripcion) throws SQLException {
        String query = "INSERT INTO TipoVehiculo(nombre, descripcion) VALUES(?, ?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String nombre, String descripcion) throws SQLException {
        String query = "UPDATE TipoVehiculo SET nombre=?, descripcion=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setInt(3, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM TipoVehiculo WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query = "SELECT id, nombre, descripcion FROM TipoVehiculo ORDER BY id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("nombre"),
                rs.getString("descripcion")
            });
        }
        return lista;
    }

    public int getIdByNombre(String nombre) throws SQLException {
        String query = "SELECT id FROM TipoVehiculo WHERE nombre=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("id") : -1;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
