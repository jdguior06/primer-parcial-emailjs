package negocio;

import dato.DTipoCurso;
import dato.DTipoVehiculo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NTipoCurso {

    private final DTipoCurso     dTipoCurso;
    private final DTipoVehiculo  dTipoVehiculo;

    public NTipoCurso() {
        this.dTipoCurso    = new DTipoCurso();
        this.dTipoVehiculo = new DTipoVehiculo();
    }

    // INSTCU["nombre","descripcion","precio","estado","duracion_horas","nombre_tipo_vehiculo"]
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 6, "nombre", "descripcion", "precio", "estado", "duracion_horas", "nombre_tipo_vehiculo");

        float precio       = parseDecimal(parametros.get(2), "precio");
        int duracionHoras  = parseEntero(parametros.get(4), "duracion_horas");

        int idTipoVehiculo = dTipoVehiculo.getIdByNombre(parametros.get(5));
        if (idTipoVehiculo == -1) {
            throw new IllegalArgumentException(
                "No existe el tipo de vehículo \"" + parametros.get(5) + "\". Use LISTYV[\"*\"] para ver los tipos disponibles."
            );
        }

        dTipoCurso.guardar(parametros.get(0), parametros.get(1), precio, parametros.get(3), duracionHoras, idTipoVehiculo);
        dTipoCurso.desconectar();
        dTipoVehiculo.desconectar();
    }

    // MODTCU["id","nombre","descripcion","precio","estado","duracion_horas"]
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 6, "id", "nombre", "descripcion", "precio", "estado", "duracion_horas");

        int id            = parseEntero(parametros.get(0), "id");
        float precio      = parseDecimal(parametros.get(3), "precio");
        int duracionHoras = parseEntero(parametros.get(5), "duracion_horas");

        dTipoCurso.modificar(id, parametros.get(1), parametros.get(2), precio, parametros.get(4), duracionHoras);
        dTipoCurso.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dTipoCurso.eliminar(id);
        dTipoCurso.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dTipoCurso.listar();
        dTipoCurso.desconectar();
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
