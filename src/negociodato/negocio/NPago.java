package negociodato.negocio;

import coneccion.pagofacil.PagoFacilService;
import negociodato.dato.DPago;
import negociodato.dato.DMetodoPago;
import negociodato.dato.DInscripcion;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NPago {

    private final DPago        dPago;
    private final DMetodoPago  dMetodoPago;
    private final DInscripcion dInscripcion;

    public NPago() {
        this.dPago        = new DPago();
        this.dMetodoPago  = new DMetodoPago();
        this.dInscripcion = new DInscripcion();
    }

    /**
     * INSPAG["fecha","id_cajero","id_inscripcion"]
     *   fecha         : YYYY-MM-DD
     *   id_cajero     : ID del usuario Secretaria/Administrador que registra el cobro
     *   id_inscripcion: ID de la inscripción a abonar
     *
     * Retorna String[] { qrBase64, transactionId, montoStr, nroCuotaStr, totalCuotasStr }
     * para que Aplication construya el correo con la imagen QR.
     */
    public String[] guardar(List<String> parametros) throws SQLException, IOException {
        validar(parametros, 3, "fecha (YYYY-MM-DD)", "id_cajero", "id_inscripcion");

        int idCajero      = parseEntero(parametros.get(1), "id_cajero");
        int idInscripcion = parseEntero(parametros.get(2), "id_inscripcion");

        // ── Obtener datos de la inscripción y del estudiante ──────────────────
        String[] datos = dInscripcion.getDatosParaPago(idInscripcion);
        if (datos == null) {
            throw new IllegalArgumentException(
                "No existe la inscripción con id=\"" + idInscripcion +
                "\". Use LISINC[\"*\"] para ver las inscripciones disponibles."
            );
        }

        float montoTotal    = Float.parseFloat(datos[0]);
        int   totalCuotas   = Integer.parseInt(datos[1]);
        String clientName   = datos[2];
        String documentId   = datos[3];
        String telefono     = datos[4];
        String correo       = datos[5];
        int    estudianteId = Integer.parseInt(datos[6]);

        // ── Calcular número de cuota y monto ─────────────────────────────────
        int cuotasPagadas = dPago.contarPorInscripcion(idInscripcion);
        if (cuotasPagadas >= totalCuotas) {
            throw new IllegalArgumentException(
                "La inscripción ya tiene todas sus cuotas pagadas (" + totalCuotas + "/" + totalCuotas + ")."
            );
        }

        int   nroCuota   = cuotasPagadas + 1;
        float montoCuota = montoTotal / totalCuotas;
        // Los pagos son reales — se divide entre 100 para manejar centavos en pruebas
        float montoApi   = montoCuota / 100f;

        // ── Obtener o validar el método de pago "PagoFacil QR" ───────────────
        int idMetodo = dMetodoPago.getIdByNombre("PagoFacil QR");
        if (idMetodo == -1) {
            throw new IllegalArgumentException(
                "El método de pago \"PagoFacil QR\" no existe en la BD. " +
                "Insértelo primero con INSMET[\"PagoFacil QR\",\"Pago mediante código QR PagoFacil\"]."
            );
        }

        // ── Registrar pago en estado pendiente para obtener el ID ────────────
        int pagoId = dPago.guardarPendiente(
            parametros.get(0), montoCuota, idCajero, idMetodo, idInscripcion, nroCuota
        );

        // ── Llamar a la API de PagoFacil ─────────────────────────────────────
        PagoFacilService servicio = new PagoFacilService();
        PagoFacilService.QRResult qr = servicio.generarQR(
            pagoId, montoApi, clientName, documentId, telefono, correo, estudianteId
        );

        // ── Asociar transactionId al pago ─────────────────────────────────────
        dPago.actualizarTransaccion(pagoId, qr.transactionId);

        dPago.desconectar();
        dMetodoPago.desconectar();
        dInscripcion.desconectar();

        return new String[]{
            qr.qrBase64,
            qr.transactionId,
            String.format("%.2f", montoCuota),
            String.valueOf(nroCuota),
            String.valueOf(totalCuotas)
        };
    }

    /** MODPAG["id","fecha","monto"] */
    public void modificar(List<String> parametros) throws SQLException {
        validar(parametros, 3, "id", "fecha (YYYY-MM-DD)", "monto");
        int id      = parseEntero(parametros.get(0), "id");
        float monto = parseDecimal(parametros.get(2), "monto");
        dPago.modificar(id, parametros.get(1), monto);
        dPago.desconectar();
    }

    /** DELPAG["id"] — delete físico */
    public void eliminar(List<String> parametros) throws SQLException {
        validar(parametros, 1, "id");
        int id = parseEntero(parametros.get(0), "id");
        dPago.eliminar(id);
        dPago.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> lista = (ArrayList<String[]>) dPago.listar();
        dPago.desconectar();
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
