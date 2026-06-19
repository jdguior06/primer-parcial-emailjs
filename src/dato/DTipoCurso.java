package dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DTipoCurso {

    public static final String[] HEADERS = {"ID", "NOMBRE", "DESCRIPCION", "PRECIO", "ESTADO", "DURACION_HORAS", "TIPO_VEHICULO"};

    private final DBConeccion connection;

    public DTipoCurso() {
        connection = new DBConeccion();
    }

    public void guardar(String nombre, String descripcion, float precio,
                        String estado, int duracionHoras, int tipoVehiculoId) throws SQLException {
        String query = "INSERT INTO TipoCurso(nombre, descripcion, precio, estado_curso, duracion_horas, tipo_vehiculo_id) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setFloat(3, precio);
        ps.setString(4, estado);
        ps.setInt(5, duracionHoras);
        ps.setInt(6, tipoVehiculoId);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String nombre, String descripcion, float precio,
                          String estado, int duracionHoras) throws SQLException {
        String query = "UPDATE TipoCurso SET nombre=?, descripcion=?, precio=?, estado_curso=?, duracion_horas=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setFloat(3, precio);
        ps.setString(4, estado);
        ps.setInt(5, duracionHoras);
        ps.setInt(6, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM TipoCurso WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT tc.id, tc.nombre, tc.descripcion, tc.precio, tc.estado_curso, tc.duracion_horas, tv.nombre AS tipo_vehiculo " +
            "FROM TipoCurso tc JOIN TipoVehiculo tv ON tc.tipo_vehiculo_id = tv.id ORDER BY tc.id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                String.valueOf(rs.getFloat("precio")),
                rs.getString("estado_curso"),
                String.valueOf(rs.getInt("duracion_horas")),
                rs.getString("tipo_vehiculo")
            });
        }
        return lista;
    }

    public int getIdByNombre(String nombre) throws SQLException {
        String query = "SELECT id FROM TipoCurso WHERE nombre=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("id") : -1;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
