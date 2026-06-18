package coneccion.pagofacil;

import database.Constantes;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class PagoFacilService {

    private static final String URL_BASE = "https://masterqr.pagofacil.com.bo/api/services/v2";

    public static class QRResult {
        public final String transactionId;
        public final String qrBase64;

        public QRResult(String transactionId, String qrBase64) {
            this.transactionId = transactionId;
            this.qrBase64      = qrBase64;
        }
    }

    // ── Paso 1: autenticar y obtener accessToken ──────────────────────────────

    private String autenticar() throws IOException {
        HttpURLConnection conn = abrirConexion(URL_BASE + "/login", "POST");
        conn.setRequestProperty("tcTokenService", Constantes.PAGOFACIL_TOKEN_SERVICE);
        conn.setRequestProperty("tcTokenSecret",  Constantes.PAGOFACIL_TOKEN_SECRET);
        conn.setRequestProperty("Response-Language", "es");
        conn.setDoOutput(false);

        String resp = leerRespuesta(conn);
        validarRespuesta(resp, "autenticación");
        return extraerString(resp, "accessToken");
    }

    private static final int PAYMENT_METHOD_QR = 34;

    // ── Paso 2: generar QR ────────────────────────────────────────────────────

    public QRResult generarQR(String paymentNumber, float montoApi, String clientName,
                              String documentId, String correoSender,
                              String clientCode, String tipoCursoNombre) throws IOException {
        String token = autenticar();

        String monto = String.format(java.util.Locale.US, "%.2f", montoApi);
        String body  = "{"
            + "\"paymentMethod\":"  + PAYMENT_METHOD_QR                  + ","
            + "\"clientName\":\""   + escapar(clientName)                + "\","
            + "\"documentType\":1,"
            + "\"documentId\":\""   + escapar(documentId)                + "\","
            + "\"phoneNumber\":\"69087992\","
            + "\"email\":\""        + escapar(correoSender)              + "\","
            + "\"paymentNumber\":\"" + escapar(paymentNumber)            + "\","
            + "\"amount\":"         + monto                              + ","
            + "\"currency\":2,"
            + "\"clientCode\":\""   + escapar(clientCode)               + "\","
            + "\"callbackUrl\":\""  + Constantes.PAGOFACIL_CALLBACK_URL  + "\","
            + "\"orderDetail\":[{"
            +   "\"serial\":1,"
            +   "\"product\":\""    + escapar(tipoCursoNombre)           + "\","
            +   "\"quantity\":1,"
            +   "\"price\":"        + monto                              + ","
            +   "\"discount\":0,"
            +   "\"total\":"        + monto
            + "}]"
            + "}";

        System.out.println("=== [PagoFacil] BODY generate-qr ===\n" + body);

        HttpURLConnection conn = abrirConexion(URL_BASE + "/generate-qr", "POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Response-Language", "es");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        String resp = leerRespuesta(conn);
        System.out.println("=== [PagoFacil] RESP generate-qr ===\n" + resp);
        validarRespuesta(resp, "generar QR");

        return new QRResult(
            extraerNumero(resp, "transactionId"),
            extraerString(resp, "qrBase64")
        );
    }

    // ── Utilidades HTTP ───────────────────────────────────────────────────────

    private HttpURLConnection abrirConexion(String urlStr, String metodo) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) URI.create(urlStr).toURL().openConnection();
        conn.setRequestMethod(metodo);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(15_000);
        return conn;
    }

    private String leerRespuesta(HttpURLConnection conn) throws IOException {
        int code = conn.getResponseCode();
        InputStream is = (code < 400) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        return sb.toString();
    }

    private void validarRespuesta(String json, String operacion) {
        String errorVal = extraerNumero(json, "error");
        if (!"0".equals(errorVal)) {
            String msg = json.contains("\"message\"") ? extraerString(json, "message") : json;
            throw new IllegalStateException("PagoFacil (" + operacion + "): " + msg);
        }
    }

    // ── Extractor JSON mínimo (sin dependencias externas) ────────────────────

    private String extraerString(String json, String campo) {
        String key = "\"" + campo + "\":\"";
        int idx = json.indexOf(key);
        if (idx == -1) throw new IllegalStateException("Campo '" + campo + "' no encontrado en respuesta PagoFacil.");
        int start = idx + key.length();
        int end   = json.indexOf('"', start);
        return json.substring(start, end);
    }

    private String extraerNumero(String json, String campo) {
        String key = "\"" + campo + "\":";
        int idx = json.indexOf(key);
        if (idx == -1) throw new IllegalStateException("Campo '" + campo + "' no encontrado en respuesta PagoFacil.");
        int start = idx + key.length();
        while (start < json.length() && json.charAt(start) == ' ') start++;
        int end = start;
        while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') end++;
        return json.substring(start, end).trim();
    }

    private String escapar(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
