package dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DPlanPago {

    public static final String[] HEADERS = {"ID", "NOMBRE", "NRO_CUOTAS", "ESTADO"};

    private final DBConeccion connection;

    public DPlanPago() {
        connection = new DBConeccion();
    }

    public void guardar(String nombre, int numeroCuotas, String estado) throws SQLException {
        String query = "INSERT INTO PlanPago(nombre, numero_cuotas, estado) VALUES(?, ?, ?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setInt(2, numeroCuotas);
        ps.setString(3, estado);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String nombre, int numeroCuotas, String estado) throws SQLException {
        String query = "UPDATE PlanPago SET nombre=?, numero_cuotas=?, estado=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setInt(2, numeroCuotas);
        ps.setString(3, estado);
        ps.setInt(4, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM PlanPago WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query = "SELECT id, nombre, numero_cuotas, estado FROM PlanPago ORDER BY id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("nombre"),
                String.valueOf(rs.getInt("numero_cuotas")),
                rs.getString("estado")
            });
        }
        return lista;
    }

    public int getIdByNombre(String nombre) throws SQLException {
        String query = "SELECT id FROM PlanPago WHERE nombre=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("id") : -1;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
