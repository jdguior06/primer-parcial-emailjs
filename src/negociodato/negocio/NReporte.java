package negociodato.negocio;

import negociodato.dato.DReporte;
import java.sql.SQLException;
import java.util.ArrayList;

public class NReporte {

    private final DReporte dReporte;

    public NReporte() {
        this.dReporte = new DReporte();
    }

    public ArrayList<String[]> reporteUsuarios() throws SQLException {
        ArrayList<String[]> lista = new ArrayList<>(dReporte.reporteUsuarios());
        dReporte.desconectar();
        return lista;
    }

    public ArrayList<String[]> reporteVehiculos() throws SQLException {
        ArrayList<String[]> lista = new ArrayList<>(dReporte.reporteVehiculos());
        dReporte.desconectar();
        return lista;
    }

    public ArrayList<String[]> reporteInscripciones() throws SQLException {
        ArrayList<String[]> lista = new ArrayList<>(dReporte.reporteInscripciones());
        dReporte.desconectar();
        return lista;
    }

    public ArrayList<String[]> reportePagos() throws SQLException {
        ArrayList<String[]> lista = new ArrayList<>(dReporte.reportePagos());
        dReporte.desconectar();
        return lista;
    }
}
