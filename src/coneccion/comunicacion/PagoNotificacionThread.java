package coneccion.comunicacion;

import coneccion.utils.Email;
import main.HtmlBuilder;
import negociodato.dato.DPago;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PagoNotificacionThread implements Runnable {

    private static final long INTERVALO_MS = 8 * 1000L; // 10 segundos

    @Override
    public void run() {
        System.out.println("[PagoNotificacion] Hilo iniciado — revisando cada 8 segundos.");
        while (true) {
            try {
                verificarYNotificar();
            } catch (Exception e) {
                Logger.getLogger(PagoNotificacionThread.class.getName())
                      .log(Level.WARNING, "[PagoNotificacion] Error en ciclo, reintentando en 8s...", e);
            }
            try {
                Thread.sleep(INTERVALO_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void verificarYNotificar() {
        DPago dPago = new DPago();
        try {
            List<String[]> pagos = dPago.obtenerPagosParaNotificar();
            for (String[] p : pagos) {
                // p: [0]=id [1]=correo [2]=monto [3]=nroCuota [4]=estudiante [5]=tipoCurso [6]=totalCuotas
                String correo = p[1];
                String monto  = String.format(java.util.Locale.US, "%.2f", Float.parseFloat(p[2]));

                String[] datos = { p[0], monto, p[3], p[4], p[5], p[6] };
                String html = HtmlBuilder.generatePagoConfirmacion(datos);

                SendEmailThread sender = new SendEmailThread(
                    new Email(correo, Email.SUBJECT, html)
                );
                new Thread(sender).start();

                dPago.marcarNotificado(Integer.parseInt(p[0]));
                System.out.println("[PagoNotificacion] Notificado pago #" + p[0] + " → " + correo);
            }
        } catch (SQLException e) {
            Logger.getLogger(PagoNotificacionThread.class.getName()).log(Level.SEVERE, "Error al notificar pagos", e);
        } finally {
            dPago.desconectar();
        }
    }
}
