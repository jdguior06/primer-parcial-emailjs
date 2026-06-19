package dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DPago {

    public static final String[] HEADERS = {
        "ID", "FECHA", "MONTO", "CUOTA", "ESTADO_PAGO", "CAJERO", "METODO", "INSCRIPCION"
    };

    private final DBConeccion connection;

    public DPago() {
        connection = new DBConeccion();
    }

    /**
     * Inserta el pago en estado 'pendiente' y retorna el id generado.
     * El id_transaccion se actualiza luego con actualizarTransaccion().
     */
    public int guardarPendiente(float monto, int usuarioId,
                                int metodoId, int inscripcionId,
                                int nroCuota, String correoNotificacion) throws SQLException {
        String query =
            "INSERT INTO Pago(fecha, monto, usuario_id, metodo_id, inscripcion_id, " +
            "nro_cuota, estado_pago, correo_notificacion) " +
            "VALUES(CURRENT_DATE, ?, ?, ?, ?, ?, 'pendiente', ?) RETURNING id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setFloat(1, monto);
        ps.setInt(2, usuarioId);
        ps.setInt(3, metodoId);
        ps.setInt(4, inscripcionId);
        ps.setInt(5, nroCuota);
        ps.setString(6, correoNotificacion);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) throw new SQLException("No se pudo insertar el pago.");
        return rs.getInt("id");
    }

    /**
     * Retorna los pagos aprobados que aún no han sido notificados por correo.
     * Cada String[]: [0]=id [1]=correo_notificacion [2]=monto [3]=nro_cuota
     *                [4]=estudiante [5]=tipo_curso [6]=total_cuotas
     */
    public List<String[]> obtenerPagosParaNotificar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT p.id, p.correo_notificacion, p.monto, p.nro_cuota, " +
            "u.nombre || ' ' || u.apellido AS estudiante, " +
            "tc.nombre AS tipo_curso, pp.numero_cuotas AS total_cuotas " +
            "FROM Pago p " +
            "JOIN Inscripcion i ON p.inscripcion_id = i.id " +
            "JOIN Usuario u     ON i.estudiante_id  = u.id " +
            "JOIN Curso c       ON i.curso_id        = c.id " +
            "JOIN TipoCurso tc  ON c.tipo_curso_id   = tc.id " +
            "JOIN PlanPago pp   ON i.plan_pago_id    = pp.id " +
            "WHERE p.estado_pago = 'pagado' " +
            "  AND p.notificado = false " +
            "  AND p.correo_notificacion IS NOT NULL";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("correo_notificacion"),
                String.valueOf(rs.getFloat("monto")),
                String.valueOf(rs.getInt("nro_cuota")),
                rs.getString("estudiante"),
                rs.getString("tipo_curso"),
                String.valueOf(rs.getInt("total_cuotas"))
            });
        }
        return lista;
    }

    public void marcarNotificado(int id) throws SQLException {
        String query = "UPDATE Pago SET notificado=true, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    /** Inserta pago en efectivo directamente como 'pagado'. */
    public void guardarEfectivo(float monto, int usuarioId,
                                int metodoId, int inscripcionId,
                                int nroCuota) throws SQLException {
        String query =
            "INSERT INTO Pago(fecha, monto, usuario_id, metodo_id, inscripcion_id, " +
            "nro_cuota, estado_pago, notificado) " +
            "VALUES(CURRENT_DATE, ?, ?, ?, ?, ?, 'pagado', true)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setFloat(1, monto);
        ps.setInt(2, usuarioId);
        ps.setInt(3, metodoId);
        ps.setInt(4, inscripcionId);
        ps.setInt(5, nroCuota);
        if (ps.executeUpdate() == 0) throw new SQLException("No se pudo insertar el pago en efectivo.");
    }

    /** Asocia el transactionId y paymentNumber de PagoFacil al pago ya insertado. */
    public void actualizarTransaccion(int id, String transactionId, String paymentNumber) throws SQLException {
        String query =
            "UPDATE Pago SET id_transaccion=?, nro_pedido=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, transactionId);
        ps.setString(2, paymentNumber);
        ps.setInt(3, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    /** Cuenta los pagos existentes para una inscripción (para saber el número de cuota siguiente). */
    public int contarPorInscripcion(int inscripcionId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Pago WHERE inscripcion_id=? AND estado_pago='pagado'";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, inscripcionId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    public void modificar(int id, String fecha, float monto) throws SQLException {
        String query =
            "UPDATE Pago SET fecha=?::DATE, monto=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, fecha);
        ps.setFloat(2, monto);
        ps.setInt(3, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM Pago WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query =
            "SELECT p.id, p.fecha, p.monto, p.nro_cuota, p.estado_pago, " +
            "u.nombre || ' ' || u.apellido AS cajero, " +
            "mp.nombre AS metodo, p.inscripcion_id " +
            "FROM Pago p " +
            "JOIN Usuario u     ON p.usuario_id = u.id " +
            "JOIN MetodoPago mp ON p.metodo_id  = mp.id " +
            "ORDER BY p.id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("fecha"),
                String.valueOf(rs.getFloat("monto")),
                String.valueOf(rs.getInt("nro_cuota")),
                rs.getString("estado_pago"),
                rs.getString("cajero"),
                rs.getString("metodo"),
                String.valueOf(rs.getInt("inscripcion_id"))
            });
        }
        return lista;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
