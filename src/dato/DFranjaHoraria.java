package dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DFranjaHoraria {

    public static final String[] HEADERS = {"ID", "HORA_INICIO", "HORA_FIN"};

    private final DBConeccion connection;

    public DFranjaHoraria() {
        connection = new DBConeccion();
    }

    public void guardar(String horaInicio, String horaFin) throws SQLException {
        String query = "INSERT INTO FranjaHoraria(hora_inicio, hora_fin) VALUES(?::TIME, ?::TIME)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, horaInicio);
        ps.setString(2, horaFin);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void modificar(int id, String horaInicio, String horaFin) throws SQLException {
        String query = "UPDATE FranjaHoraria SET hora_inicio=?::TIME, hora_fin=?::TIME, actualizado_en=CURRENT_TIMESTAMP WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, horaInicio);
        ps.setString(2, horaFin);
        ps.setInt(3, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM FranjaHoraria WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) throw new SQLException();
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String query = "SELECT id, hora_inicio, hora_fin FROM FranjaHoraria ORDER BY hora_inicio";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("hora_inicio"),
                rs.getString("hora_fin")
            });
        }
        return lista;
    }

    public void desconectar() {
        if (connection != null) connection.closeConnection();
    }
}
