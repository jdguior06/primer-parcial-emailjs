package negocio;

import dato.DCurso;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NCurso {

    private final DCurso dCurso;

    public NCurso() {
        this.dCurso = new DCurso();
    }

    // INSCUR["fecha_ini","fecha_fin","precio","estado","id_instructor","id_vehiculo","id_tipo_curso","id_franja"]
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 8,
            "fecha_inicio (YYYY-MM-DD)", "fecha_fin (YYYY-MM-DD)", "precio",
            "estado", "id_instructor", "id_vehiculo",
            "id_tipo_curso", "id_franja");

        float precio     = parseDecimal(parametros.get(2), "precio");
        int idInstructor = parseEntero(parametros.get(4), "id_instructor");
        int idVehiculo   = parseEntero(parametros.get(5), "id_vehiculo");
        int idTipoCurso  = parseEntero(parametros.get(6), "id_tipo_curso");
        int idFranja     = parseEntero(parametros.get(7), "id_franja");

        dCurso.guardar(
            parametros.get(0), parametros.get(1), precio,
            parametros.get(3), idInstructor, idVehiculo, idTipoCurso, idFranja
        );
        dCurso.desconectar();
    }

    /**
     * MODCUR["id","fecha_ini","fecha_fin","precio","estado"]
     */
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 5,
            "id", "fecha_inicio (YYYY-MM-DD)", "fecha_fin (YYYY-MM-DD)", "precio", "estado");

        int id       = parseEntero(parametros.get(0), "id");
        float precio = parseDecimal(parametros.get(3), "precio");

        dCurso.modificar(id, parametros.get(1), parametros.get(2), precio, parametros.get(4));
        dCurso.desconectar();
    }

    // DELCUR["id"] — soft delete (cancelado)
    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dCurso.eliminar(id);
        dCurso.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dCurso.listar();
        dCurso.desconectar();
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
