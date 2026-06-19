package dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DVehiculo {

    public static final String[] HEADERS = {"ID", "PLACA", "MARCA", "MODELO", "ESTADO", "FECHA_MANT", "TIPO_VEHICULO"};

    private final DBConeccion connection;

    public DVehiculo() {
        connection = new DBConeccion();
    }

    public void guardar(String placa, String marca, String modelo,
                        String estado, String fechaMantenimiento,
                        int tipoVehiculoId) throws SQLException {
        String query =
            "INSERT INTO Vehiculo(placa, marca, modelo, estado_vehiculo, fecha_mantenimiento, tipo_vehiculo_id) " +
            "VALUES(?,?,?,?,?::DATE,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, placa);
        ps.setString(2, marca);
        ps.setString(3, modelo);
        ps.setString(4, estado);
        ps.setString(5, fechaMantenimiento);
        ps.setInt(6, tipoVehiculoId);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String placa, String marca, String modelo,
                          String estado, String fechaMantenimiento) throws SQLException {
        String query =
            "UPDATE Vehiculo SET placa=?, marca=?, modelo=?, estado_vehiculo=?, " +
            "fecha_mantenimiento=?::DATE, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, placa);
        ps.setString(2, marca);
        ps.setString(3, modelo);
        ps.setString(4, estado);
        ps.setString(5, fechaMantenimiento);
        ps.setInt(6, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    // Soft delete: Vehiculo es referenciado por Curso con ON DELETE RESTRICT
    public void eliminar(int id) throws SQLException {
        String query = "UPDATE Vehiculo SET estado_vehiculo='baja', actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT v.id, v.placa, v.marca, v.modelo, v.estado_vehiculo, " +
            "COALESCE(v.fecha_mantenimiento::TEXT, '-') AS fecha_mant, tv.nombre AS tipo_vehiculo " +
            "FROM Vehiculo v JOIN TipoVehiculo tv ON v.tipo_vehiculo_id = tv.id ORDER BY v.id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("placa"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getString("estado_vehiculo"),
                rs.getString("fecha_mant"),
                rs.getString("tipo_vehiculo")
            });
        }
        return lista;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
