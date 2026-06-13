package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DInscripcion {

    public static final String[] HEADERS = {
        "ID", "FECHA", "ESTADO", "APROBADOS", "MONTO", "ESTUDIANTE", "CURSO", "PLAN_PAGO"
    };

    private final DBConeccion connection;

    public DInscripcion() {
        connection = new DBConeccion();
    }

    public void guardar(String fecha, String estado, float montoTotal,
                        int estudianteId, int planPagoId, int cursoId) throws SQLException {
        String query =
            "INSERT INTO Inscripcion(fecha_inscripcion, estado_inscripcion, aprobados, monto_total, " +
            "estudiante_id, plan_pago_id, curso_id) VALUES(?::DATE, ?, 0, ?, ?, ?, ?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, fecha);
        ps.setString(2, estado);
        ps.setFloat(3, montoTotal);
        ps.setInt(4, estudianteId);
        ps.setInt(5, planPagoId);
        ps.setInt(6, cursoId);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String estado, int aprobados) throws SQLException {
        String query =
            "UPDATE Inscripcion SET estado_inscripcion=?, aprobados=?, " +
            "actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, estado);
        ps.setInt(2, aprobados);
        ps.setInt(3, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    // Soft delete: referenciada por Pago y ControlCertificacion con ON DELETE RESTRICT
    public void eliminar(int id) throws SQLException {
        String query =
            "UPDATE Inscripcion SET estado_inscripcion='cancelada', " +
            "actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT i.id, i.fecha_inscripcion, i.estado_inscripcion, i.aprobados, i.monto_total, " +
            "u.nombre || ' ' || u.apellido AS estudiante, " +
            "c.id || ' (' || tc.nombre || ')' AS curso, " +
            "pp.nombre AS plan_pago " +
            "FROM Inscripcion i " +
            "JOIN Usuario u ON i.estudiante_id = u.id " +
            "JOIN Curso c ON i.curso_id = c.id " +
            "JOIN TipoCurso tc ON c.tipo_curso_id = tc.id " +
            "JOIN PlanPago pp ON i.plan_pago_id = pp.id " +
            "ORDER BY i.id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("fecha_inscripcion"),
                rs.getString("estado_inscripcion"),
                String.valueOf(rs.getInt("aprobados")),
                String.valueOf(rs.getFloat("monto_total")),
                rs.getString("estudiante"),
                rs.getString("curso"),
                rs.getString("plan_pago")
            });
        }
        return lista;
    }

    /**
     * Retorna los datos necesarios para generar el QR de pago.
     * [0]=monto_total [1]=nro_cuotas [2]=clientName [3]=nro_documento [4]=telefono [5]=correo [6]=estudiante_id
     * Retorna null si no existe la inscripción.
     */
    public String[] getDatosParaPago(int inscripcionId) throws SQLException {
        String query =
            "SELECT i.monto_total, pp.numero_cuotas, " +
            "u.nombre || ' ' || u.apellido AS cliente_nombre, " +
            "u.nro_documento, u.telefono, u.correo, u.id AS estudiante_id " +
            "FROM Inscripcion i " +
            "JOIN Usuario u   ON i.estudiante_id = u.id " +
            "JOIN PlanPago pp ON i.plan_pago_id  = pp.id " +
            "WHERE i.id = ?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, inscripcionId);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;
        return new String[]{
            String.valueOf(rs.getFloat("monto_total")),
            String.valueOf(rs.getInt("numero_cuotas")),
            rs.getString("cliente_nombre"),
            rs.getString("nro_documento"),
            rs.getString("telefono") != null ? rs.getString("telefono") : "",
            rs.getString("correo"),
            String.valueOf(rs.getInt("estudiante_id"))
        };
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
