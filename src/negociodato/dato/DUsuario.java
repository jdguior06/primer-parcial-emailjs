package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DUsuario {

    public static final String[] HEADERS = {"ID", "NOMBRE", "APELLIDO", "DOCUMENTO", "CORREO", "TELEFONO", "ESTADO", "ROL"};

    private final DBConeccion connection;

    public DUsuario() {
        connection = new DBConeccion();
    }

    public void guardar(String nombre, String apellido, String nroDocumento,
                        String correo, String telefono, String direccion,
                        String password, int rolId) throws SQLException {
        String query =
            "INSERT INTO Usuario(nombre, apellido, nro_documento, correo, telefono, direccion, password, estado_usuario, rol_id) " +
            "VALUES(?,?,?,?,?,?,?,'activo',?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setString(3, nroDocumento);
        ps.setString(4, correo);
        ps.setString(5, telefono);
        ps.setString(6, direccion);
        ps.setString(7, password);
        ps.setInt(8, rolId);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String nombre, String apellido, String nroDocumento,
                          String correo, String telefono, String direccion,
                          String estado) throws SQLException {
        String query =
            "UPDATE Usuario SET nombre=?, apellido=?, nro_documento=?, correo=?, " +
            "telefono=?, direccion=?, estado_usuario=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setString(3, nroDocumento);
        ps.setString(4, correo);
        ps.setString(5, telefono);
        ps.setString(6, direccion);
        ps.setString(7, estado);
        ps.setInt(8, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    // Soft delete: marca como inactivo para preservar FK con Curso/Pago
    public void eliminar(int id) throws SQLException {
        String query = "UPDATE Usuario SET estado_usuario='inactivo', actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT u.id, u.nombre, u.apellido, u.nro_documento, u.correo, " +
            "u.telefono, u.estado_usuario, r.nombre AS rol " +
            "FROM Usuario u JOIN Rol r ON u.rol_id = r.id ORDER BY u.id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("nro_documento"),
                rs.getString("correo"),
                rs.getString("telefono") != null ? rs.getString("telefono") : "",
                rs.getString("estado_usuario"),
                rs.getString("rol")
            });
        }
        return lista;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
