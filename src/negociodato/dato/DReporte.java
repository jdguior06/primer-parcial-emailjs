package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DReporte {

    public static final String[] HEADERS_USUARIOS      = {"ROL", "ESTADO", "TOTAL"};
    public static final String[] HEADERS_VEHICULOS     = {"TIPO", "ESTADO", "TOTAL"};
    public static final String[] HEADERS_INSCRIPCIONES = {"TIPO_CURSO", "ESTADO", "TOTAL", "MONTO_TOTAL"};
    public static final String[] HEADERS_PAGOS         = {"METODO", "CANTIDAD", "TOTAL_BS"};

    private final DBConeccion connection;

    public DReporte() {
        connection = new DBConeccion();
    }

    /** Usuarios agrupados por rol y estado. */
    public List<String[]> reporteUsuarios() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT r.nombre AS rol, u.estado_usuario AS estado, COUNT(*) AS total " +
            "FROM Usuario u " +
            "JOIN Rol r ON u.rol_id = r.id " +
            "GROUP BY r.nombre, u.estado_usuario " +
            "ORDER BY r.nombre, u.estado_usuario";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                rs.getString("rol"),
                rs.getString("estado"),
                String.valueOf(rs.getInt("total"))
            });
        }
        return lista;
    }

    /** Vehículos agrupados por tipo y estado. */
    public List<String[]> reporteVehiculos() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT tv.nombre AS tipo, v.estado_vehiculo AS estado, COUNT(*) AS total " +
            "FROM Vehiculo v " +
            "JOIN TipoVehiculo tv ON v.tipo_vehiculo_id = tv.id " +
            "GROUP BY tv.nombre, v.estado_vehiculo " +
            "ORDER BY tv.nombre, v.estado_vehiculo";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                rs.getString("tipo"),
                rs.getString("estado"),
                String.valueOf(rs.getInt("total"))
            });
        }
        return lista;
    }

    /** Inscripciones agrupadas por tipo de curso y estado, con monto total. */
    public List<String[]> reporteInscripciones() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT tc.nombre AS tipo_curso, i.estado_inscripcion AS estado, " +
            "COUNT(*) AS total, COALESCE(SUM(i.monto_total), 0) AS monto_total " +
            "FROM Inscripcion i " +
            "JOIN Curso c ON i.curso_id = c.id " +
            "JOIN TipoCurso tc ON c.tipo_curso_id = tc.id " +
            "GROUP BY tc.nombre, i.estado_inscripcion " +
            "ORDER BY tc.nombre, i.estado_inscripcion";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                rs.getString("tipo_curso"),
                rs.getString("estado"),
                String.valueOf(rs.getInt("total")),
                String.format("%.2f", rs.getFloat("monto_total"))
            });
        }
        return lista;
    }

    /** Pagos agrupados por método, con cantidad y total recaudado. */
    public List<String[]> reportePagos() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT mp.nombre AS metodo, COUNT(*) AS cantidad, " +
            "COALESCE(SUM(p.monto), 0) AS total_bs " +
            "FROM Pago p " +
            "JOIN MetodoPago mp ON p.metodo_id = mp.id " +
            "GROUP BY mp.nombre " +
            "ORDER BY total_bs DESC";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                rs.getString("metodo"),
                String.valueOf(rs.getInt("cantidad")),
                String.format("%.2f", rs.getFloat("total_bs"))
            });
        }
        return lista;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
