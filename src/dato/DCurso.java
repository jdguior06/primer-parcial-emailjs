package dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DCurso {

    public static final String[] HEADERS = {
        "ID", "FECHA_INI", "FECHA_FIN", "PRECIO", "ESTADO",
        "INSTRUCTOR", "VEHICULO", "TIPO_CURSO", "FRANJA"
    };

    private final DBConeccion connection;

    public DCurso() {
        connection = new DBConeccion();
    }

    public void guardar(String fechaInicio, String fechaFin, float precio,
                        String estado, int instructorId, int vehiculoId,
                        int tipoCursoId, int franjaId) throws SQLException {
        String query =
            "INSERT INTO Curso(fecha_inicio, fecha_fin, precio_final, estado_curso, " +
            "instructor_id, vehiculo_id, tipo_curso_id, franja_horaria_id) " +
            "VALUES(?::DATE, ?::DATE, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, fechaInicio);
        ps.setString(2, fechaFin);
        ps.setFloat(3, precio);
        ps.setString(4, estado);
        ps.setInt(5, instructorId);
        ps.setInt(6, vehiculoId);
        ps.setInt(7, tipoCursoId);
        ps.setInt(8, franjaId);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String fechaInicio, String fechaFin,
                          float precio, String estado) throws SQLException {
        String query =
            "UPDATE Curso SET fecha_inicio=?::DATE, fecha_fin=?::DATE, precio_final=?, " +
            "estado_curso=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, fechaInicio);
        ps.setString(2, fechaFin);
        ps.setFloat(3, precio);
        ps.setString(4, estado);
        ps.setInt(5, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    // Soft delete: Curso es referenciado por Inscripcion con ON DELETE RESTRICT
    public void eliminar(int id) throws SQLException {
        String query = "UPDATE Curso SET estado_curso='cancelado', actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT c.id, c.fecha_inicio, c.fecha_fin, c.precio_final, c.estado_curso, " +
            "u.nombre || ' ' || u.apellido AS instructor, " +
            "v.placa || ' ' || v.marca AS vehiculo, " +
            "tc.nombre AS tipo_curso, " +
            "fh.hora_inicio || '-' || fh.hora_fin AS franja " +
            "FROM Curso c " +
            "JOIN Usuario u ON c.instructor_id = u.id " +
            "JOIN Vehiculo v ON c.vehiculo_id = v.id " +
            "JOIN TipoCurso tc ON c.tipo_curso_id = tc.id " +
            "JOIN FranjaHoraria fh ON c.franja_horaria_id = fh.id " +
            "ORDER BY c.id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("fecha_inicio"),
                rs.getString("fecha_fin"),
                String.valueOf(rs.getFloat("precio_final")),
                rs.getString("estado_curso"),
                rs.getString("instructor"),
                rs.getString("vehiculo"),
                rs.getString("tipo_curso"),
                rs.getString("franja")
            });
        }
        return lista;
    }

    public float getPrecioFinal(int cursoId) throws SQLException {
        String query = "SELECT precio_final FROM Curso WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cursoId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getFloat("precio_final") : -1f;
    }

    /**
     * Asegura que el curso quede en estado 'reservado' para proceder con inscripción.
     * Si está disponible: lo reserva atómicamente (solo cambia estado).
     * Si ya está reservado: no hace nada.
     * Lanza excepción si está inscrito o cancelado.
     */
    public void asegurarReservado(int cursoId) throws SQLException {
        String queryUpdate =
            "UPDATE Curso SET estado_curso='reservado', " +
            "actualizado_en=CURRENT_TIMESTAMP WHERE id=? AND estado_curso='disponible'";
        PreparedStatement psUpdate = connection.connect().prepareStatement(queryUpdate);
        psUpdate.setInt(1, cursoId);
        if (psUpdate.executeUpdate() > 0) return;

        String queryEstado = "SELECT estado_curso FROM Curso WHERE id=?";
        PreparedStatement psEstado = connection.connect().prepareStatement(queryEstado);
        psEstado.setInt(1, cursoId);
        ResultSet rs = psEstado.executeQuery();
        if (!rs.next()) {
            throw new IllegalArgumentException(
                "No existe un curso con id=" + cursoId + ". Use LISCUR[\"*\"] para ver los cursos."
            );
        }
        String estado = rs.getString("estado_curso");
        if ("reservado".equals(estado)) return;
        throw new IllegalStateException(
            "El curso " + cursoId + " no está disponible para inscripción (estado actual: " +
            estado + ")."
        );
    }

    /** Marca el curso como inscrito (llamado por NInscripcion tras crear la inscripción). */
    public void marcarInscrito(int cursoId) throws SQLException {
        String query =
            "UPDATE Curso SET estado_curso='inscrito', actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cursoId);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    /** Libera el curso → disponible (llamado por NControlCert al emitir cert). */
    public void liberarPorInscripcion(int inscripcionId) throws SQLException {
        String query =
            "UPDATE Curso SET estado_curso='disponible', " +
            "actualizado_en=CURRENT_TIMESTAMP " +
            "WHERE id = (SELECT curso_id FROM Inscripcion WHERE id=?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, inscripcionId);
        ps.executeUpdate();
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
