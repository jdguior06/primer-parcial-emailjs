package negocio;

import dato.DRol;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NRol {

    private final DRol dRol;

    public NRol() {
        this.dRol = new DRol();
    }

    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 2, "nombre", "descripcion");
        dRol.guardar(parametros.get(0), parametros.get(1));
        dRol.desconectar();
    }

    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 3, "id", "nombre", "descripcion");
        int id = parseEntero(parametros.get(0), "id");
        dRol.modificar(id, parametros.get(1), parametros.get(2));
        dRol.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dRol.eliminar(id);
        dRol.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dRol.listar();
        dRol.desconectar();
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
