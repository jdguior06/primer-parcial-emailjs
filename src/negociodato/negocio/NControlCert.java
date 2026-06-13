package negociodato.negocio;

import negociodato.dato.DControlCert;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NControlCert {

    private final DControlCert dControlCert;

    public NControlCert() {
        this.dControlCert = new DControlCert();
    }

    /**
     * INSCRT["id_inscripcion","nota","estado","fecha_emision"]
     * fecha_emision: YYYY-MM-DD
     * estado: p.ej. "aprobado", "reprobado", "pendiente"
     */
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 4, "id_inscripcion", "nota", "estado_certificacion",
                               "fecha_emision (YYYY-MM-DD)");

        int idInscripcion = parseEntero(parametros.get(0), "id_inscripcion");
        float nota        = parseDecimal(parametros.get(1), "nota");

        dControlCert.guardar(idInscripcion, nota, parametros.get(2), parametros.get(3));
        dControlCert.desconectar();
    }

    /**
     * MODCRT["id","nota","estado","fecha_emision"]
     */
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 4, "id", "nota", "estado_certificacion",
                               "fecha_emision (YYYY-MM-DD)");

        int id     = parseEntero(parametros.get(0), "id");
        float nota = parseDecimal(parametros.get(1), "nota");

        dControlCert.modificar(id, nota, parametros.get(2), parametros.get(3));
        dControlCert.desconectar();
    }

    // DELCRT["id"] — delete físico (no hay FK dependiente)
    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dControlCert.eliminar(id);
        dControlCert.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dControlCert.listar();
        dControlCert.desconectar();
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

    private static float parseDecimal(String valor, String campo) {
        try {
            return Float.parseFloat(valor.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "El campo '" + campo + "' debe ser un número decimal. Se recibió: \"" + valor + "\""
            );
        }
    }
}
