package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DUsuario {

    public static final String[] HEADERS = {"ID", "NOMBRE", "APELLIDO", "FECHA_NAC", "GENERO", "DOCUMENTO", "CORREO", "TELEFONO", "ESTADO", "ROL"};

    private final DBConeccion connection;

    public DUsuario() {
        connection = new DBConeccion();
    }

    public void guardar(String nombre, String apellido, java.sql.Date fechaNacimiento,
                        String genero, String nroDocumento, String correo,
                        String telefono, String direccion, String password,
                        int rolId) throws SQLException {
        String query =
            "INSERT INTO Usuario(nombre, apellido, fecha_nacimiento, genero, nro_documento, correo, telefono, direccion, password, estado_usuario, rol_id) " +
            "VALUES(?,?,?,?,?,?,?,?,?,'activo',?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setDate(3, fechaNacimiento);
        ps.setString(4, genero);
        ps.setString(5, nroDocumento);
        ps.setString(6, correo);
        ps.setString(7, telefono);
        ps.setString(8, direccion);
        ps.setString(9, password);
        ps.setInt(10, rolId);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String nombre, String apellido, java.sql.Date fechaNacimiento,
                          String genero, String nroDocumento, String correo,
                          String telefono, String direccion, String estado) throws SQLException {
        String query =
            "UPDATE Usuario SET nombre=?, apellido=?, fecha_nacimiento=?, genero=?, nro_documento=?, correo=?, " +
            "telefono=?, direccion=?, estado_usuario=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setDate(3, fechaNacimiento);
        ps.setString(4, genero);
        ps.setString(5, nroDocumento);
        ps.setString(6, correo);
        ps.setString(7, telefono);
        ps.setString(8, direccion);
        ps.setString(9, estado);
        ps.setInt(10, id);
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
            "SELECT u.id, u.nombre, u.apellido, u.fecha_nacimiento, u.genero, u.nro_documento, u.correo, " +
            "u.telefono, u.estado_usuario, r.nombre AS rol " +
            "FROM Usuario u JOIN Rol r ON u.rol_id = r.id ORDER BY u.id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getDate("fecha_nacimiento") != null ? rs.getDate("fecha_nacimiento").toString() : "",
                rs.getString("genero") != null ? rs.getString("genero") : "",
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
