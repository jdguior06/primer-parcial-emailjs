package negocio;

import dato.DPlanPago;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NPlanPago {

    private final DPlanPago dPlanPago;

    public NPlanPago() {
        this.dPlanPago = new DPlanPago();
    }

    // INSPLN["nombre","numero_cuotas","estado"]
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 3, "nombre", "numero_cuotas", "estado");
        int numeroCuotas = parseEntero(parametros.get(1), "numero_cuotas");
        dPlanPago.guardar(parametros.get(0), numeroCuotas, parametros.get(2));
        dPlanPago.desconectar();
    }

    // MODPLN["id","nombre","numero_cuotas","estado"]
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 4, "id", "nombre", "numero_cuotas", "estado");
        int id = parseEntero(parametros.get(0), "id");
        int numeroCuotas = parseEntero(parametros.get(2), "numero_cuotas");
        dPlanPago.modificar(id, parametros.get(1), numeroCuotas, parametros.get(3));
        dPlanPago.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dPlanPago.eliminar(id);
        dPlanPago.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dPlanPago.listar();
        dPlanPago.desconectar();
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
