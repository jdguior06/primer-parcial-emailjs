package negocio;

import dato.DInscripcion;
import dato.DCurso;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NInscripcion {

    private final DInscripcion dInscripcion;
    private final DCurso       dCurso;

    public NInscripcion() {
        this.dInscripcion = new DInscripcion();
        this.dCurso       = new DCurso();
    }

    // INSINC["id_estudiante","id_curso","id_plan_pago"]
    // fecha = CURRENT_DATE, estado = 'programada', monto_total calculado desde Curso.precio_final
    public String[] guardar(List<String> parametros) throws SQLException {
        validar(parametros, 3, "id_estudiante", "id_curso", "id_plan_pago");

        int idEstudiante = parseEntero(parametros.get(0), "id_estudiante");
        int idCurso      = parseEntero(parametros.get(1), "id_curso");
        int idPlanPago   = parseEntero(parametros.get(2), "id_plan_pago");

        float montoTotal = dCurso.getPrecioFinal(idCurso);
        if (montoTotal < 0) {
            throw new IllegalArgumentException(
                "No existe el curso con id=\"" + parametros.get(1) + "\". Use LISCUR[\"*\"] para ver los cursos disponibles."
            );
        }

        dCurso.asegurarReservado(idCurso, idEstudiante);

        String[] datos = dInscripcion.guardar(montoTotal, idEstudiante, idPlanPago, idCurso);
        dCurso.marcarInscrito(idCurso);
        dInscripcion.desconectar();
        dCurso.desconectar();
        return datos;
    }

    // MODINC["id","estado"]
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 2, "id", "estado");
        int id = parseEntero(parametros.get(0), "id");
        dInscripcion.modificar(id, parametros.get(1));
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
