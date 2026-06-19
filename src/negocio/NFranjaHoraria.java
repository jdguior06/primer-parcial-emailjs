package negocio;

import dato.DFranjaHoraria;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NFranjaHoraria {

    private final DFranjaHoraria dFranjaHoraria;

    public NFranjaHoraria() {
        this.dFranjaHoraria = new DFranjaHoraria();
    }

    // INSFRA["hora_inicio","hora_fin"]  — formato HH:MM p.ej. "08:00","10:00"
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 2, "hora_inicio (HH:MM)", "hora_fin (HH:MM)");
        dFranjaHoraria.guardar(parametros.get(0).trim(), parametros.get(1).trim());
        dFranjaHoraria.desconectar();
    }

    // MODFRA["id","hora_inicio","hora_fin"]
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 3, "id", "hora_inicio (HH:MM)", "hora_fin (HH:MM)");
        int id = parseEntero(parametros.get(0), "id");
        dFranjaHoraria.modificar(id, parametros.get(1).trim(), parametros.get(2).trim());
        dFranjaHoraria.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dFranjaHoraria.eliminar(id);
        dFranjaHoraria.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dFranjaHoraria.listar();
        dFranjaHoraria.desconectar();
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
