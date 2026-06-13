package main;

import java.util.List;

public class HtmlBuilder {

    // ── Identidad visual ──────────────────────────────────────────────────────
    private static final String NOMBRE_SISTEMA = "Autom&#243;vil Club Boliviano";
    private static final String SUBTITULO      = "Santa Cruz &mdash; Sistema de Gesti&#243;n Acad&#233;mico y Administrativo";

    // ── Paleta (verde institucional + blanco) ─────────────────────────────────
    private static final String C_PRIMARY    = "#1a5c2a";  // verde oscuro — header, th
    private static final String C_PRIMARY_DK = "#13421e";  // verde muy oscuro — footer
    private static final String C_ACCENT     = "#4caf50";  // verde medio — bordes, hover
    private static final String C_ROW_ALT    = "#eef7f1";  // verde muy claro — fila par
    private static final String C_ERROR      = "#cc0000";
    private static final String C_BG         = "#f5f7f5";  // fondo general

    // ── Esqueleto HTML ────────────────────────────────────────────────────────
    private static final String HTML_OPEN  = "<!DOCTYPE html><html lang=\"es\">";
    private static final String HTML_CLOSE = "</html>";
    private static final String HEAD_OPEN  =
            "<head>"
            + "<meta charset=\"UTF-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
            + "<style type=\"text/css\">";
    private static final String HEAD_CLOSE = "</style></head>";
    private static final String BODY_OPEN  = "<body>";
    private static final String BODY_CLOSE = "</body>";

    // ── CSS global ────────────────────────────────────────────────────────────

    private static String cssBase() {
        return "* { box-sizing: border-box; margin: 0; padding: 0; }\n"
             + "body { background: " + C_BG + "; color: #1a1a1a;"
             + " font-family: Arial, Helvetica, sans-serif; }\n"
             // Header banner
             + ".acb-header { background: " + C_PRIMARY + "; color: #fff;"
             + " text-align: center; padding: 22px 20px 16px;"
             + " border-bottom: 4px solid " + C_ACCENT + "; }\n"
             + ".acb-header h1 { font-size: 26px; letter-spacing: 1px; margin: 0; }\n"
             + ".acb-header .subtitle { font-size: 12px; opacity: .85; margin-top: 5px; }\n"
             // Wrapper central
             + ".container { padding: 20px; }\n"
             // Footer
             + "footer { background: " + C_PRIMARY_DK + "; color: #fff;"
             + " text-align: center; padding: 14px 20px; margin-top: 30px;"
             + " font-size: 12px; }\n"
             // Flexbox helpers (usados por footer con múltiples columnas)
             + ".footer-container { display: flex; justify-content: center;"
             + " align-items: center; flex-wrap: wrap; }\n"
             + ".item { flex: 1; padding: 20px; text-align: center; }\n"
             + ".item h5 { margin-bottom: 10px; }\n"
             + ".item img { max-width: 200px; }\n"
             + ".item a img { max-width: 400px; }\n"
             + ".column { display: flex; flex-direction: column; align-items: center; }\n";
    }

    private static String cssTable() {
        return "table { margin: 30px auto; border-collapse: collapse; min-width: 580px; }\n"
             + "caption { padding-bottom: 10px; font-size: 22px; font-weight: bold;"
             + " color: " + C_PRIMARY + "; caption-side: top; }\n"
             + "th { background: " + C_PRIMARY + "; color: #fff;"
             + " padding: 10px 20px; text-align: left; }\n"
             + "td { padding: 9px 20px; border: 1px solid #c8dfd0; }\n"
             + "th { border: 1px solid " + C_PRIMARY_DK + "; }\n"
             + "tbody tr:nth-child(even) { background: " + C_ROW_ALT + "; }\n"
             + "tbody tr:hover { background: #d4edda; }\n";
    }

    // ── Banner de cabecera ─────────────────────────────────────────────────────

    public static String brandHeader() {
        return "<div class=\"acb-header\">"
             + "<h1>" + NOMBRE_SISTEMA + "</h1>"
             + "<p class=\"subtitle\">" + SUBTITULO + "</p>"
             + "</div>";
    }

    /** Mantiene compatibilidad con el código existente que llama a BalnearioHTML(). */
    public static String BalnearioHTML(String contenido) {
        return brandHeader()
             + "<div class=\"container\">"
             + contenido
             + "</div>";
    }

    // ── Footer ─────────────────────────────────────────────────────────────────

    private static String FooterHTML() {
        return "<footer>"
             + "<p><strong>" + NOMBRE_SISTEMA + " &mdash; Santa Cruz</strong></p>"
             + "<p style=\"margin-top:4px;opacity:.8;\">Desarrollado con Java &bull; HTML &bull; CSS &bull; PostgreSQL</p>"
             + "</footer>";
    }

    // ── Inserción en plantilla HTML ────────────────────────────────────────────

    private static String insertInHtml(String body) {
        return HTML_OPEN
             + HEAD_OPEN + cssBase() + HEAD_CLOSE
             + BODY_OPEN + BalnearioHTML(body) + FooterHTML() + BODY_CLOSE
             + HTML_CLOSE;
    }

    private static String insertInHtml(String extraCss, String body) {
        return HTML_OPEN
             + HEAD_OPEN + cssBase() + extraCss + HEAD_CLOSE
             + BODY_OPEN + body + FooterHTML() + BODY_CLOSE
             + HTML_CLOSE;
    }

    // ── Generadores públicos ───────────────────────────────────────────────────

    public static String generateTable(String title, String[] headers, List<String[]> data) {
        StringBuilder th = new StringBuilder("<tr>");
        for (String h : headers) th.append("<th>").append(h).append("</th>");
        th.append("</tr>");

        StringBuilder rows = new StringBuilder();
        for (String[] row : data) {
            rows.append("<tr>");
            for (String val : row) rows.append("<td>").append(val).append("</td>");
            rows.append("</tr>");
        }

        String table = "<table>"
                + "<caption>" + title + "</caption>"
                + "<thead>" + th + "</thead>"
                + "<tbody>" + rows + "</tbody>"
                + "</table>";

        String body = BalnearioHTML(table);
        return insertInHtml(cssTable(), body);
    }

    public static String generateText(String[] args) {
        StringBuilder html = new StringBuilder();
        html.append("<div style=\"text-align:center; padding: 30px 20px;\">");
        html.append("<h2 style=\"color:").append(C_PRIMARY).append("; margin-bottom:12px;\">")
            .append(args[0]).append("</h2>");
        for (int i = 1; i < args.length; i++) {
            html.append("<h3 style=\"font-weight:normal; color:#333; margin-top:8px;\">")
                .append(args[i]).append("</h3>");
        }
        html.append("</div>");
        return insertInHtml(html.toString());
    }

    public static String generateErrorWithHelp(String[] messages, String tableTitle,
                                               String[] headers, List<String[]> data) {
        StringBuilder errorHtml = new StringBuilder();
        errorHtml.append("<div style=\"text-align:center; padding: 20px 20px 0;\">")
                 .append("<h2 style=\"color:").append(C_ERROR).append(";\">")
                 .append(messages[0]).append("</h2>");
        for (int i = 1; i < messages.length; i++) {
            errorHtml.append("<h3 style=\"font-weight:normal; color:#333; margin-top:8px;\">")
                     .append(messages[i]).append("</h3>");
        }
        errorHtml.append("</div>");

        StringBuilder th = new StringBuilder("<tr>");
        for (String h : headers) th.append("<th>").append(h).append("</th>");
        th.append("</tr>");

        StringBuilder rows = new StringBuilder();
        for (String[] row : data) {
            rows.append("<tr>");
            for (String val : row) rows.append("<td>").append(val).append("</td>");
            rows.append("</tr>");
        }

        String table = "<table>"
                + "<caption>" + tableTitle + "</caption>"
                + "<thead>" + th + "</thead>"
                + "<tbody>" + rows + "</tbody>"
                + "</table>";

        String body = BalnearioHTML(errorHtml.toString() + table);
        return insertInHtml(cssTable(), body);
    }

    public static String generateTableForSimpleData(String title, String[] headers, String[] data) {
        StringBuilder rows = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            rows.append("<tr>")
                .append("<td><strong>").append(headers[i]).append("</strong></td>")
                .append("<td>").append(data[i]).append("</td>")
                .append("</tr>");
        }
        String table = "<table style=\"min-width:300px;\">"
                + "<caption>" + title + "</caption>"
                + "<tbody>" + rows + "</tbody>"
                + "</table>";
        String body = BalnearioHTML(table);
        return insertInHtml(cssTable(), body);
    }

    /**
     * Genera un correo con el código QR embebido como imagen base64 y los datos del cobro.
     * qrBase64     : string base64 devuelto por PagoFacil
     * monto        : "250.00"
     * nroCuota     : "1"
     * totalCuotas  : "3"
     * transactionId: ID de la transacción en PagoFacil
     */
    public static String generateQR(String qrBase64, String monto,
                                    String nroCuota, String totalCuotas,
                                    String transactionId) {
        String cssQr = ".qr-box { text-align:center; padding: 24px 20px; }\n"
            + ".qr-box h2 { color:" + C_PRIMARY + "; margin-bottom:6px; }\n"
            + ".qr-box .detalle { color:#444; font-size:15px; margin-bottom:18px; }\n"
            + ".qr-box img { max-width:280px; border:3px solid " + C_ACCENT + ";"
            + " border-radius:8px; padding:6px; background:#fff; }\n"
            + ".qr-box .aviso { margin-top:16px; font-size:13px; color:#666; }\n"
            + ".qr-box .tid { margin-top:8px; font-size:11px; color:#999; }\n";

        String body = "<div class=\"qr-box\">"
            + "<h2>Código QR de Pago</h2>"
            + "<p class=\"detalle\">Cuota <strong>" + nroCuota + "</strong> de <strong>"
            + totalCuotas + "</strong> &mdash; Monto: <strong>Bs. " + monto + "</strong></p>"
            + "<img src=\"data:image/png;base64," + qrBase64 + "\" alt=\"QR PagoFacil\">"
            + "<p class=\"aviso\">Escanee el código con su aplicación bancaria para completar el pago.</p>"
            + "<p class=\"tid\">Ref. transacción: " + transactionId + "</p>"
            + "</div>";

        return insertInHtml(cssQr, BalnearioHTML(body));
    }

    // ── Gráficas (Google Chart) ───────────────────────────────────────────────

    public static String generateGrafica(String title, List<String[]> data) {
        StringBuilder valores = new StringBuilder();
        StringBuilder enc1    = new StringBuilder();
        StringBuilder enc2    = new StringBuilder();
        for (String[] e : data) {
            valores.append(e[1]).append(",");
            enc1.append(e[0]).append("|");
            enc2.append(e[0]).append("%28").append(e[1]).append("+veces%29|");
        }
        trim(valores); trim(enc1); trim(enc2);
        String base = "http://chart.apis.google.com/chart?";
        String graficas = "<h2 style=\"text-align:center;color:" + C_PRIMARY + ";margin:20px 0 10px;\">" + title + "</h2>"
                + "<div style=\"text-align:center;\">"
                + "<img src=\"" + base + "chs=600x200&cht=p&chd=t:" + valores + "&chl=" + enc1 + "\" width=\"600\" height=\"200\">"
                + "</div>"
                + "<div style=\"text-align:center;margin-top:10px;\">"
                + "<img src=\"" + base + "chs=600x200&cht=bhg&chco=1a5c2a|4caf50|eef7f1&chd=t:" + valores + "&chdl=" + enc2 + "\" width=\"600\" height=\"200\">"
                + "</div>";
        return insertInHtml(graficas);
    }

    public static String generateGrafica2(String title, List<String[]> data) {
        StringBuilder valores = new StringBuilder();
        StringBuilder enc2    = new StringBuilder();
        for (String[] e : data) {
            valores.append(e[1]).append(",");
            enc2.append(e[0]).append(" %28").append(e[1]).append("+Bs%29|");
        }
        trim(valores); trim(enc2);
        String graficas = "<h2 style=\"text-align:center;color:" + C_PRIMARY + ";margin:20px 0 10px;\">" + title + "</h2>"
                + "<div style=\"text-align:center;\">"
                + "<img src=\"https://chart.apis.google.com/chart?chs=700x400&cht=p&chd=t:" + valores + "&chdl=" + enc2 + "\" width=\"700\" height=\"400\">"
                + "</div>";
        return insertInHtml(graficas);
    }

    public static String generateGrafica3(String title, List<String[]> data) {
        StringBuilder valores = new StringBuilder();
        StringBuilder enc1    = new StringBuilder();
        StringBuilder enc2    = new StringBuilder();
        for (String[] e : data) {
            valores.append(e[1]).append(",");
            enc1.append(e[0]).append(" %28").append(e[1]).append("+Bs%29|");
            enc2.append(e[0]).append(" %28").append(e[2]).append("+veces%29|");
        }
        trim(valores); trim(enc1); trim(enc2);
        String base = "http://chart.apis.google.com/chart?";
        String graficas = "<h2 style=\"text-align:center;color:" + C_PRIMARY + ";margin:20px 0 10px;\">" + title + "</h2>"
                + "<div style=\"text-align:center;\">"
                + "<img src=\"" + base + "chs=600x200&cht=p&chd=t:" + valores + "&chl=" + enc1 + "\" width=\"600\" height=\"200\">"
                + "</div>"
                + "<div style=\"text-align:center;margin-top:10px;\">"
                + "<img src=\"" + base + "chs=600x200&cht=bhg&chco=1a5c2a|4caf50|eef7f1&chd=t:" + valores + "&chdl=" + enc2 + "\" width=\"600\" height=\"200\">"
                + "</div>";
        return insertInHtml(graficas);
    }

    private static void trim(StringBuilder sb) {
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
    }
}
