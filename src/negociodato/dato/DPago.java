package negociodato.dato;

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
     * El transaction_id se actualiza luego con actualizarTransaccion().
     */
    public int guardarPendiente(String fecha, float monto, int usuarioId,
                                int metodoId, int inscripcionId,
                                int nroCuota) throws SQLException {
        String query =
            "INSERT INTO Pago(fecha, monto, usuario_id, metodo_id, inscripcion_id, " +
            "nro_cuota, estado_pago) " +
            "VALUES(?::DATE, ?, ?, ?, ?, ?, 'pendiente') RETURNING id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, fecha);
        ps.setFloat(2, monto);
        ps.setInt(3, usuarioId);
        ps.setInt(4, metodoId);
        ps.setInt(5, inscripcionId);
        ps.setInt(6, nroCuota);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) throw new SQLException("No se pudo insertar el pago.");
        return rs.getInt("id");
    }

    /** Asocia el transactionId de PagoFacil al pago ya insertado. */
    public void actualizarTransaccion(int id, String transactionId) throws SQLException {
        String query =
            "UPDATE Pago SET transaction_id=?, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, transactionId);
        ps.setInt(2, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    /** Cuenta los pagos existentes para una inscripción (para saber el número de cuota siguiente). */
    public int contarPorInscripcion(int inscripcionId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Pago WHERE inscripcion_id=?";
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
