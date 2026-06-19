package negocio;

import dato.DVehiculo;
import dato.DTipoVehiculo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NVehiculo {

    private final DVehiculo     dVehiculo;
    private final DTipoVehiculo dTipoVehiculo;

    public NVehiculo() {
        this.dVehiculo     = new DVehiculo();
        this.dTipoVehiculo = new DTipoVehiculo();
    }

    // INSVEH["placa","marca","modelo","estado","fecha_mant","nombre_tipo_vehiculo"]
    // fecha_mant formato YYYY-MM-DD, p.ej. "2025-01-15"
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 6, "placa", "marca", "modelo", "estado_vehiculo",
                               "fecha_mantenimiento (YYYY-MM-DD)", "nombre_tipo_vehiculo");

        int idTipo = dTipoVehiculo.getIdByNombre(parametros.get(5));
        if (idTipo == -1) {
            throw new IllegalArgumentException(
                "No existe el tipo de vehículo \"" + parametros.get(5) + "\". Use LISTYV[\"*\"] para ver los tipos disponibles."
            );
        }

        dVehiculo.guardar(
            parametros.get(0), parametros.get(1), parametros.get(2),
            parametros.get(3), parametros.get(4), idTipo
        );
        dVehiculo.desconectar();
        dTipoVehiculo.desconectar();
    }

    // MODVEH["id","placa","marca","modelo","estado","fecha_mant"]
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 6, "id", "placa", "marca", "modelo",
                               "estado_vehiculo", "fecha_mantenimiento (YYYY-MM-DD)");

        int id = parseEntero(parametros.get(0), "id");
        dVehiculo.modificar(
            id,
            parametros.get(1), parametros.get(2), parametros.get(3),
            parametros.get(4), parametros.get(5)
        );
        dVehiculo.desconectar();
    }

    // DELVEH["id"] — soft delete (baja)
    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dVehiculo.eliminar(id);
        dVehiculo.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dVehiculo.listar();
        dVehiculo.desconectar();
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
