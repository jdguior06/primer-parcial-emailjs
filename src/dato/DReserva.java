package dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DReserva {

    public static final String[] HEADERS = {
        "ID", "FECHA_INI", "FECHA_FIN", "PRECIO", "ESTADO",
        "INSTRUCTOR", "VEHICULO", "TIPO_CURSO", "FRANJA"
    };

    private final DBConeccion connection;

    public DReserva() {
        connection = new DBConeccion();
    }

    /**
     * Reserva un curso (cambia estado a 'reservado').
     * Solo aplica si el curso está en estado 'disponible'.
     * Retorna los datos del curso reservado para mostrar al estudiante.
     */
    public String[] reservar(int cursoId, int estudianteId) throws SQLException {
        // Actualizar estado: solo si el curso está disponible (manejo de concurrencia)
        String queryUpdate =
            "UPDATE Curso SET estado_curso='reservado', " +
            "actualizado_en=CURRENT_TIMESTAMP " +
            "WHERE id=? AND estado_curso='disponible'";
        PreparedStatement psUpdate = connection.connect().prepareStatement(queryUpdate);
        psUpdate.setInt(1, cursoId);
        if (psUpdate.executeUpdate() == 0) {
            // Revisar por qué falló
            String queryEstado = "SELECT estado_curso FROM Curso WHERE id=?";
            PreparedStatement psEstado = connection.connect().prepareStatement(queryEstado);
            psEstado.setInt(1, cursoId);
            ResultSet rsEstado = psEstado.executeQuery();
            if (!rsEstado.next()) {
                throw new IllegalArgumentException(
                    "No existe un curso con id=" + cursoId + ". Use LISCUR[\"*\"] para ver los cursos."
                );
            }
            String estado = rsEstado.getString("estado_curso");
            throw new IllegalStateException(
                "El curso " + cursoId + " no está disponible para reservar (estado actual: " + estado + ")."
            );
        }

        // Retornar datos del curso recién reservado
        String querySelect =
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
            "WHERE c.id=?";
        PreparedStatement psSelect = connection.connect().prepareStatement(querySelect);
        psSelect.setInt(1, cursoId);
        ResultSet rs = psSelect.executeQuery();
        if (rs.next()) {
            return new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("fecha_inicio"),
                rs.getString("fecha_fin"),
                String.valueOf(rs.getFloat("precio_final")),
                rs.getString("estado_curso"),
                rs.getString("instructor"),
                rs.getString("vehiculo"),
                rs.getString("tipo_curso"),
                rs.getString("franja")
            };
        }
        throw new SQLException("No se pudo recuperar el curso tras la reserva.");
    }

    /**
     * Cancela la reserva del estudiante sobre el curso.
     * Valida que el curso esté en estado 'reservado' y que sea el mismo estudiante.
     */
    public void cancelar(int cursoId, int estudianteId) throws SQLException {
        String query =
            "UPDATE Curso SET estado_curso='disponible', " +
            "actualizado_en=CURRENT_TIMESTAMP " +
            "WHERE id=? AND estado_curso='reservado'";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cursoId);
        if (ps.executeUpdate() == 0) {
            throw new IllegalArgumentException(
                "El curso " + cursoId + " no tiene una reserva activa. Verifique con LISCUR[\"*\"]."
            );
        }
    }

    /** Lista todos los cursos con estado='disponible'. */
    public List<String[]> listarDisponibles() throws SQLException {
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
            "WHERE c.estado_curso = 'disponible' " +
            "ORDER BY c.fecha_inicio";
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

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
