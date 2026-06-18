package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DControlCert {

    public static final String[] HEADERS = {
        "ID", "NOTA", "ESTADO", "FECHA_EMISION", "ESTUDIANTE", "CURSO"
    };

    private final DBConeccion connection;

    public DControlCert() {
        connection = new DBConeccion();
    }

    public int guardar(int inscripcionId, float nota,
                       String estado, String fechaEmision) throws SQLException {
        String query =
            "INSERT INTO ControlCertificacion(inscripcion_id, nota, estado_certificacion, fecha_emision) " +
            "VALUES(?, ?, ?, ?::DATE) RETURNING id";
        System.out.println("[DControlCert.guardar] ejecutando INSERT con inscripcionId=" + inscripcionId);
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, inscripcionId);
        ps.setFloat(2, nota);
        ps.setString(3, estado);
        ps.setString(4, fechaEmision);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            System.out.println("[DControlCert.guardar] INSERT OK, id=" + id);
            return id;
        }
        throw new SQLException("No se pudo obtener el ID del certificado insertado.");
    }

    public String[] obtenerDatosParaCertificado(int certId) throws SQLException {
        System.out.println("[DControlCert.obtenerDatos] buscando certId=" + certId);
        String query =
            "SELECT cc.id, " +
            "est.nombre || ' ' || est.apellido AS estudiante, est.nro_documento, " +
            "tc.nombre AS tipo_curso, " +
            "cc.nota, cc.estado_certificacion, " +
            "COALESCE(cc.fecha_emision::TEXT, '-') AS fecha_emision, " +
            "inst.nombre || ' ' || inst.apellido AS instructor, " +
            "c.fecha_inicio::TEXT, c.fecha_fin::TEXT " +
            "FROM ControlCertificacion cc " +
            "JOIN Inscripcion i ON cc.inscripcion_id = i.id " +
            "JOIN Usuario est ON i.estudiante_id = est.id " +
            "JOIN Curso c ON i.curso_id = c.id " +
            "JOIN TipoCurso tc ON c.tipo_curso_id = tc.id " +
            "JOIN Usuario inst ON c.instructor_id = inst.id " +
            "WHERE cc.id = ?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, certId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("estudiante"),
                rs.getString("nro_documento"),
                rs.getString("tipo_curso"),
                String.format("%.1f", rs.getFloat("nota")),
                rs.getString("estado_certificacion"),
                rs.getString("fecha_emision"),
                rs.getString("instructor"),
                rs.getString("fecha_inicio"),
                rs.getString("fecha_fin")
            };
        }
        return null;
    }

    public void modificar(int id, float nota, String estado, String fechaEmision) throws SQLException {
        String query =
            "UPDATE ControlCertificacion SET nota=?, estado_certificacion=?, " +
            "fecha_emision=?::DATE, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setFloat(1, nota);
        ps.setString(2, estado);
        ps.setString(3, fechaEmision);
        ps.setInt(4, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    // ControlCertificacion no es referenciada por otras tablas: delete físico
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM ControlCertificacion WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT cc.id, cc.nota, cc.estado_certificacion, " +
            "COALESCE(cc.fecha_emision::TEXT, '-') AS fecha_emision, " +
            "u.nombre || ' ' || u.apellido AS estudiante, " +
            "tc.nombre AS curso " +
            "FROM ControlCertificacion cc " +
            "JOIN Inscripcion i ON cc.inscripcion_id = i.id " +
            "JOIN Usuario u ON i.estudiante_id = u.id " +
            "JOIN Curso c ON i.curso_id = c.id " +
            "JOIN TipoCurso tc ON c.tipo_curso_id = tc.id " +
            "ORDER BY cc.id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getFloat("nota")),
                rs.getString("estado_certificacion"),
                rs.getString("fecha_emision"),
                rs.getString("estudiante"),
                rs.getString("curso")
            });
        }
        return lista;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
