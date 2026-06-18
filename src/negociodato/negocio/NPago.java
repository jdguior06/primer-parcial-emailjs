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
     * INSPAG["fecha","id_usuario","id_inscripcion","id_metodo"]
     *   fecha         : YYYY-MM-DD
     *   id_usuario    : ID del usuario que registra el cobro
     *   id_inscripcion: ID de la inscripción a abonar
     *   id_metodo     : 1=Efectivo, 2=PagoFacil QR
     *
     * Retorna String[] cuyo primer elemento indica el tipo:
     *   ["efectivo", montoStr, nroCuotaStr, totalCuotasStr]
     *   ["qr", qrBase64, transactionId, montoStr, nroCuotaStr, totalCuotasStr]
     */
    public String[] guardar(List<String> parametros, String correoSender) throws SQLException, IOException {
        validar(parametros, 4, "fecha (YYYY-MM-DD)", "id_usuario", "id_inscripcion", "id_metodo (1=Efectivo, 2=QR)");

        int idUsuario     = parseEntero(parametros.get(1), "id_usuario");
        int idInscripcion = parseEntero(parametros.get(2), "id_inscripcion");
        int idMetodo      = parseEntero(parametros.get(3), "id_metodo");

        if (idMetodo != 1 && idMetodo != 2) {
            throw new IllegalArgumentException(
                "id_metodo inválido: use 1 (Efectivo) o 2 (PagoFacil QR)."
            );
        }

        // ── Obtener datos de la inscripción ───────────────────────────────────
        String[] datos = dInscripcion.getDatosParaPago(idInscripcion);
        if (datos == null) {
            throw new IllegalArgumentException(
                "No existe la inscripción con id=\"" + idInscripcion +
                "\". Use LISINC[\"*\"] para ver las inscripciones disponibles."
            );
        }

        float montoTotal       = Float.parseFloat(datos[0]);
        int   totalCuotas      = Integer.parseInt(datos[1]);
        String clientName      = datos[2];
        String documentId      = datos[3];
        String tipoCursoNombre = datos[7];

        // ── Calcular cuota ────────────────────────────────────────────────────
        int cuotasPagadas = dPago.contarPorInscripcion(idInscripcion);
        if (cuotasPagadas >= totalCuotas) {
            throw new IllegalArgumentException(
                "La inscripción ya tiene todas sus cuotas pagadas (" + totalCuotas + "/" + totalCuotas + ")."
            );
        }

        int   nroCuota   = cuotasPagadas + 1;
        float montoCuota = montoTotal / totalCuotas;

        // ── Rama efectivo ─────────────────────────────────────────────────────
        if (idMetodo == 1) {
            dPago.guardarEfectivo(
                parametros.get(0), montoCuota, idUsuario, idMetodo, idInscripcion, nroCuota
            );
            dPago.desconectar();
            dInscripcion.desconectar();
            return new String[]{
                "efectivo",
                String.format("%.2f", montoCuota),
                String.valueOf(nroCuota),
                String.valueOf(totalCuotas)
            };
        }

        // ── Rama QR (id_metodo == 2) ──────────────────────────────────────────
        // Se divide entre 1000 para pruebas: curso 500 Bs → 0.50 Bs al API
        float montoApi = montoCuota / 1000f;

        int pagoId = dPago.guardarPendiente(
            parametros.get(0), montoCuota, idUsuario, idMetodo, idInscripcion, nroCuota, correoSender
        );

        String paymentNumber = System.currentTimeMillis() + String.format("%04d", pagoId);

        PagoFacilService.QRResult qr;
        try {
            PagoFacilService servicio = new PagoFacilService();
            qr = servicio.generarQR(
                paymentNumber, montoApi, clientName, documentId, correoSender, "11001", tipoCursoNombre
            );
        } catch (IOException | IllegalStateException ex) {
            // La API falló — revertir el pago pendiente para no dejar registros huérfanos
            try { dPago.eliminar(pagoId); } catch (SQLException ignored) {}
            throw ex;
        }

        dPago.actualizarTransaccion(pagoId, qr.transactionId, paymentNumber);

        dPago.desconectar();
        dMetodoPago.desconectar();
        dInscripcion.desconectar();

        return new String[]{
            "qr",
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
