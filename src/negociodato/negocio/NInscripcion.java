package negociodato.negocio;

import negociodato.dato.DInscripcion;
import negociodato.dato.DPlanPago;
import negociodato.dato.DCurso;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NInscripcion {

    private final DInscripcion dInscripcion;
    private final DPlanPago    dPlanPago;
    private final DCurso       dCurso;

    public NInscripcion() {
        this.dInscripcion = new DInscripcion();
        this.dPlanPago    = new DPlanPago();
        this.dCurso       = new DCurso();
    }

    /**
     * INSINC["fecha","estado","id_estudiante","id_curso","nombre_plan"]
     * fecha: YYYY-MM-DD
     * monto_total: se calcula automáticamente desde Curso.precio_final
     */
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 5, "fecha (YYYY-MM-DD)", "estado", "id_estudiante", "id_curso", "nombre_plan");

        int idEstudiante = parseEntero(parametros.get(2), "id_estudiante");
        int idCurso      = parseEntero(parametros.get(3), "id_curso");

        float montoTotal = dCurso.getPrecioFinal(idCurso);
        if (montoTotal < 0) {
            throw new IllegalArgumentException(
                "No existe el curso con id=\"" + parametros.get(3) + "\". Use LISCUR[\"*\"] para ver los cursos disponibles."
            );
        }

        int idPlanPago = dPlanPago.getIdByNombre(parametros.get(4));
        if (idPlanPago == -1) {
            throw new IllegalArgumentException(
                "No existe el plan de pago \"" + parametros.get(4) + "\". Use LISPLN[\"*\"] para ver los planes disponibles."
            );
        }

        dInscripcion.guardar(parametros.get(0), parametros.get(1), montoTotal,
                             idEstudiante, idPlanPago, idCurso);
        dInscripcion.desconectar();
        dPlanPago.desconectar();
        dCurso.desconectar();
    }

    /**
     * MODINC["id","estado","aprobados"]
     * aprobados: 0=pendiente, 1=aprobado
     */
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 3, "id", "estado", "aprobados (0/1)");
        int id        = parseEntero(parametros.get(0), "id");
        int aprobados = parseEntero(parametros.get(2), "aprobados");
        dInscripcion.modificar(id, parametros.get(1), aprobados);
        dInscripcion.desconectar();
    }

    // DELINC["id"] — soft delete (cancelada)
    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dInscripcion.eliminar(id);
        dInscripcion.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dInscripcion.listar();
        dInscripcion.desconectar();
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
