package negocio;

import dato.DControlCert;
import dato.DCurso;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NControlCert {

    private final DControlCert dControlCert;
    private final DCurso       dCurso;

    public NControlCert() {
        this.dControlCert = new DControlCert();
        this.dCurso       = new DCurso();
    }

    /**
     * INSCRT["id_inscripcion","nota","estado","fecha_emision"]
     * fecha_emision: YYYY-MM-DD
     * estado: p.ej. "aprobado", "reprobado", "pendiente"
     * Retorna String[] con datos del certificado para generar el PDF.
     */
    public String[] guardar(List<String> parametros) throws SQLException {
        validar(parametros, 4, "id_inscripcion", "nota", "estado_certificacion",
                               "fecha_emision (YYYY-MM-DD)");

        int idInscripcion = parseEntero(parametros.get(0), "id_inscripcion");
        float nota        = parseDecimal(parametros.get(1), "nota");

        if (nota < 70) {
            dCurso.liberarPorInscripcion(idInscripcion);
            dCurso.desconectar();
            throw new IllegalStateException(
                "Nota insuficiente: " + nota + " (mínimo requerido: 70). " +
                "No se emite certificado. El curso ha sido liberado y está disponible nuevamente."
            );
        }

        System.out.println("[INSCRT] params: id_inscripcion=" + idInscripcion
            + " nota=" + nota
            + " estado=" + parametros.get(2)
            + " fecha=" + parametros.get(3));

        int certId = dControlCert.guardar(idInscripcion, nota, parametros.get(2), parametros.get(3));
        System.out.println("[INSCRT] INSERT OK — certId=" + certId);

        String[] datos = dControlCert.obtenerDatosParaCertificado(certId);
        System.out.println("[INSCRT] datos cert: " + (datos != null ? java.util.Arrays.toString(datos) : "NULL"));

        // Libera el curso → disponible para el próximo estudiante
        dCurso.liberarPorInscripcion(idInscripcion);
        dControlCert.desconectar();
        dCurso.desconectar();
        return datos;
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
