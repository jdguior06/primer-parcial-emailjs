package negocio;

import dato.DUsuario;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NUsuario {

    private final DUsuario dUsuario;

    public NUsuario() {
        this.dUsuario = new DUsuario();
    }

    // INSUSU["nombre","apellido","fecha_nacimiento","genero","nro_doc","correo","telefono","direccion","pass","rol_id"]
    public String[] guardar(List<String> parametros) throws SQLException {
        validar(parametros, 10, "nombre", "apellido", "fecha_nacimiento", "genero",
                                "nro_documento", "correo", "telefono", "direccion",
                                "password", "rol_id");

        Date fechaNacimiento;
        try {
            fechaNacimiento = Date.valueOf(parametros.get(2).trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "El campo 'fecha_nacimiento' debe tener formato YYYY-MM-DD. Se recibió: \"" + parametros.get(2) + "\""
            );
        }
        if (!fechaNacimiento.before(new Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser una fecha futura o el día de hoy.");
        }

        String genero = parametros.get(3).toUpperCase().trim();
        if (!genero.equals("M") && !genero.equals("F")) {
            throw new IllegalArgumentException(
                "El campo 'genero' debe ser 'M' o 'F'. Se recibió: \"" + parametros.get(3) + "\""
            );
        }

        int rolId = parseEntero(parametros.get(9), "rol_id");

        String[] datos = dUsuario.guardar(
            parametros.get(0), parametros.get(1), fechaNacimiento, genero,
            parametros.get(4), parametros.get(5), parametros.get(6),
            parametros.get(7), parametros.get(8), rolId
        );
        dUsuario.desconectar();
        return datos;
    }

    // MODUSU["id","nombre","apellido","fecha_nacimiento","genero","nro_doc","correo","telefono","direccion","estado"]
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 10, "id", "nombre", "apellido", "fecha_nacimiento", "genero",
                                "nro_documento", "correo", "telefono", "direccion", "estado");

        int id = parseEntero(parametros.get(0), "id");

        Date fechaNacimiento;
        try {
            fechaNacimiento = Date.valueOf(parametros.get(3).trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "El campo 'fecha_nacimiento' debe tener formato YYYY-MM-DD. Se recibió: \"" + parametros.get(3) + "\""
            );
        }
        if (!fechaNacimiento.before(new Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser una fecha futura o el día de hoy.");
        }

        String genero = parametros.get(4).toUpperCase().trim();
        if (!genero.equals("M") && !genero.equals("F")) {
            throw new IllegalArgumentException(
                "El campo 'genero' debe ser 'M' o 'F'. Se recibió: \"" + parametros.get(4) + "\""
            );
        }

        dUsuario.modificar(
            id,
            parametros.get(1), parametros.get(2), fechaNacimiento, genero,
            parametros.get(5), parametros.get(6), parametros.get(7),
            parametros.get(8), parametros.get(9)
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
