package negociodato.negocio;

import negociodato.dato.DCurso;
import negociodato.dato.DTipoCurso;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NCurso {

    private final DCurso     dCurso;
    private final DTipoCurso dTipoCurso;

    public NCurso() {
        this.dCurso     = new DCurso();
        this.dTipoCurso = new DTipoCurso();
    }

    /**
     * INSCUR["fecha_ini","fecha_fin","precio","estado","id_instructor","id_vehiculo","nombre_tipo_curso","id_franja"]
     * fecha_ini / fecha_fin: YYYY-MM-DD
     * id_instructor, id_vehiculo, id_franja: enteros (obtenidos con LISUSU, LISVEH, LISFRA)
     * nombre_tipo_curso: resuelto por nombre (usar LISTCU para ver disponibles)
     */
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 8,
            "fecha_inicio (YYYY-MM-DD)", "fecha_fin (YYYY-MM-DD)", "precio",
            "estado", "id_instructor", "id_vehiculo",
            "nombre_tipo_curso", "id_franja");

        float precio       = parseDecimal(parametros.get(2), "precio");
        int idInstructor   = parseEntero(parametros.get(4), "id_instructor");
        int idVehiculo     = parseEntero(parametros.get(5), "id_vehiculo");
        int idFranja       = parseEntero(parametros.get(7), "id_franja");

        int idTipoCurso = dTipoCurso.getIdByNombre(parametros.get(6));
        if (idTipoCurso == -1) {
            throw new IllegalArgumentException(
                "No existe el tipo de curso \"" + parametros.get(6) + "\". Use LISTCU[\"*\"] para ver los tipos disponibles."
            );
        }

        dCurso.guardar(
            parametros.get(0), parametros.get(1), precio,
            parametros.get(3), idInstructor, idVehiculo, idTipoCurso, idFranja
        );
        dCurso.desconectar();
        dTipoCurso.desconectar();
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
