package negociodato.negocio;

import negociodato.dato.DTipoVehiculo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NTipoVehiculo {

    private final DTipoVehiculo dTipoVehiculo;

    public NTipoVehiculo() {
        this.dTipoVehiculo = new DTipoVehiculo();
    }

    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 2, "nombre", "descripcion");
        dTipoVehiculo.guardar(parametros.get(0), parametros.get(1));
        dTipoVehiculo.desconectar();
    }

    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 3, "id", "nombre", "descripcion");
        int id = parseEntero(parametros.get(0), "id");
        dTipoVehiculo.modificar(id, parametros.get(1), parametros.get(2));
        dTipoVehiculo.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dTipoVehiculo.eliminar(id);
        dTipoVehiculo.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dTipoVehiculo.listar();
        dTipoVehiculo.desconectar();
        return lista;
    }

    private static void validar(List<String> params, int esperados, String... nombres) {
        if (params.size() < esperados) {
            throw new IllegalArgumentException(
                "Faltan parámetros: se esperan " + esperados +
                " [" + String.join(", ", nombres) + "]" +
                " pero se recibieron " + params.size() + "."
            );
        }
    }

    private static int parseEntero(String valor, String campo) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "El campo '" + campo + "' debe ser un número entero. Se recibió: \"" + valor + "\""
            );
        }
    }
}
