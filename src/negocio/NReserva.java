package negocio;

import dato.DReserva;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NReserva {

    private final DReserva dReserva;

    public NReserva() {
        this.dReserva = new DReserva();
    }

    // INSRES["id_curso","id_estudiante"]
    public String[] reservar(List<String> parametros) throws SQLException {
        validar(parametros, 2, "id_curso", "id_estudiante");
        int idCurso      = parseEntero(parametros.get(0), "id_curso");
        int idEstudiante = parseEntero(parametros.get(1), "id_estudiante");
        String[] datos = dReserva.reservar(idCurso, idEstudiante);
        dReserva.desconectar();
        return datos;
    }

    // DELRES["id_curso","id_estudiante"]
    public void cancelar(List<String> parametros) throws SQLException {
        validar(parametros, 2, "id_curso", "id_estudiante");
        int idCurso      = parseEntero(parametros.get(0), "id_curso");
        int idEstudiante = parseEntero(parametros.get(1), "id_estudiante");
        dReserva.cancelar(idCurso, idEstudiante);
        dReserva.desconectar();
    }

    // LISRES["*"]
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = new ArrayList<>(dReserva.listarDisponibles());
        dReserva.desconectar();
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
