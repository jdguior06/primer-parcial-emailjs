package negociodato.negocio;

import negociodato.dato.DUsuario;
import negociodato.dato.DRol;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NUsuario {

    private final DUsuario dUsuario;
    private final DRol     dRol;

    public NUsuario() {
        this.dUsuario = new DUsuario();
        this.dRol     = new DRol();
    }

    // INSUSU["nombre","apellido","nro_doc","correo","telefono","direccion","pass","nombre_rol"]
    public void guardar(List<String> parametros) throws SQLException {
        validar(parametros, 8, "nombre", "apellido", "nro_documento", "correo",
                               "telefono", "direccion", "password", "nombre_rol");

        int idRol = dRol.getIdByNombre(parametros.get(7));
        if (idRol == -1) {
            throw new IllegalArgumentException(
                "No existe el rol \"" + parametros.get(7) + "\". Use LISROL[\"*\"] para ver los roles disponibles."
            );
        }

        dUsuario.guardar(
            parametros.get(0), parametros.get(1), parametros.get(2),
            parametros.get(3), parametros.get(4), parametros.get(5),
            parametros.get(6), idRol
        );
        dUsuario.desconectar();
        dRol.desconectar();
    }

    // MODUSU["id","nombre","apellido","nro_doc","correo","telefono","direccion","estado"]
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 8, "id", "nombre", "apellido", "nro_documento",
                               "correo", "telefono", "direccion", "estado");

        int id = parseEntero(parametros.get(0), "id");
        dUsuario.modificar(
            id,
            parametros.get(1), parametros.get(2), parametros.get(3),
            parametros.get(4), parametros.get(5), parametros.get(6),
            parametros.get(7)
        );
        dUsuario.desconectar();
    }

    // DELUSU["id"] — soft delete (inactivo)
    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dUsuario.eliminar(id);
        dUsuario.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dUsuario.listar();
        dUsuario.desconectar();
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
