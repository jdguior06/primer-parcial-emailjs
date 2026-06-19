package main;

import coneccion.comunicacion.MailVerificationThread;
import coneccion.comunicacion.PagoNotificacionThread;
import coneccion.comunicacion.SendEmailThread;
import coneccion.interfaces.IEmailEventListener;
import coneccion.utils.Email;
import interpreter.analex.Interpreter;
import interpreter.analex.interfaces.ITokenEventListener;
import interpreter.analex.utils.Token;
import interpreter.events.TokenEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import dato.DMetodoPago;
import dato.DPlanPago;
import dato.DRol;
import dato.DTipoCurso;
import dato.DTipoVehiculo;
import dato.DFranjaHoraria;
import negocio.NComando;
import negocio.NFranjaHoraria;
import negocio.NMetodoPago;
import negocio.NPlanPago;
import negocio.NRol;
import negocio.NTipoCurso;
import negocio.NTipoVehiculo;
import negocio.NUsuario;
import negocio.NVehiculo;
import negocio.NCurso;
import negocio.NInscripcion;
import negocio.NControlCert;
import negocio.NPago;
import negocio.NReporte;
import negocio.NReserva;
import dato.DUsuario;
import dato.DVehiculo;
import dato.DCurso;
import dato.DInscripcion;
import dato.DControlCert;
import dato.DPago;
import dato.DReporte;
import dato.DReserva;
import utils.Comandos;

public class Aplication implements IEmailEventListener, ITokenEventListener {

    private static final Logger LOG = Logger.getLogger(Aplication.class.getName());

    private static final int CONSTRAINTS_ERROR        = -2;
    private static final int INDEX_OUT_OF_BOUND_ERROR = -4;
    private static final int PARSE_ERROR              = -5;

    private MailVerificationThread mailVerificationThread;

    private NComando      nComando;
    private NRol          nRol;
    private NMetodoPago   nMetodoPago;
    private NPlanPago     nPlanPago;
    private NFranjaHoraria nFranjaHoraria;
    private NTipoVehiculo nTipoVehiculo;
    private NTipoCurso    nTipoCurso;
    private NUsuario      nUsuario;
    private NVehiculo     nVehiculo;
    private NCurso        nCurso;
    private NInscripcion  nInscripcion;
    private NControlCert  nControlCert;
    private NPago         nPago;
    private NReporte      nReporte;
    private NReserva      nReserva;

    public Aplication() {
        mailVerificationThread = new MailVerificationThread();
        mailVerificationThread.setEmailEventListener(Aplication.this);

        nComando       = new NComando();
        nRol           = new NRol();
        nMetodoPago    = new NMetodoPago();
        nPlanPago      = new NPlanPago();
        nFranjaHoraria = new NFranjaHoraria();
        nTipoVehiculo  = new NTipoVehiculo();
        nTipoCurso     = new NTipoCurso();
        nUsuario       = new NUsuario();
        nVehiculo      = new NVehiculo();
        nCurso         = new NCurso();
        nInscripcion   = new NInscripcion();
        nControlCert   = new NControlCert();
        nPago          = new NPago();
        nReporte       = new NReporte();
        nReserva       = new NReserva();
    }

    public void start() {
        Thread thread = new Thread(mailVerificationThread);
        thread.setName("Mail Verification Thread");
        thread.start();

        Thread pagoNotif = new Thread(new PagoNotificacionThread());
        pagoNotif.setName("Pago Notificacion Thread");
        pagoNotif.setDaemon(true);
        pagoNotif.start();
    }

    @Override
    public void onReceiveEmailEvent(List<Email> emails) {
        for (Email email : emails) {
            LOG.info("[CORREO RECIBIDO] de: " + email.getFrom() + " | subject: " + email.getSubject());
            String subject = email.getSubject() + " ";
            Interpreter interpreter = new Interpreter(subject, email.getFrom());
            interpreter.setListener(Aplication.this);
            Thread thread = new Thread(interpreter);
            thread.setName("Interpreter Thread");
            thread.start();
        }
    }

    // ── Handlers de comandos ──────────────────────────────────────────────────

    @Override
    public void help(TokenEvent event) {
        tableNotifySuccess(event.getSender(),
            "Flujo Principal",
            Comandos.HEADERS_FLUJO,
            Comandos.listarFlujo());
    }

    @Override
    public void helpFlujo(TokenEvent event) {
        tableNotifySuccess(event.getSender(), "Lista de Comandos", Comandos.HEADERS, nComando.listar());
    }

    @Override
    public void rol(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nRol.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Rol guardado correctamente.");
                    break;
                case Token.MODIFICAR:
                    nRol.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Rol modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nRol.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Rol eliminado correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Roles", DRol.HEADERS, nRol.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void metodoPago(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nMetodoPago.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Método de pago guardado correctamente.");
                    break;
                case Token.MODIFICAR:
                    nMetodoPago.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Método de pago modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nMetodoPago.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Método de pago eliminado correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Métodos de Pago", DMetodoPago.HEADERS, nMetodoPago.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void planPago(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nPlanPago.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Plan de pago guardado correctamente.");
                    break;
                case Token.MODIFICAR:
                    nPlanPago.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Plan de pago modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nPlanPago.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Plan de pago eliminado correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Planes de Pago", DPlanPago.HEADERS, nPlanPago.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void franjaHoraria(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nFranjaHoraria.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Franja horaria guardada correctamente.");
                    break;
                case Token.MODIFICAR:
                    nFranjaHoraria.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Franja horaria modificada correctamente.");
                    break;
                case Token.ELIMINAR:
                    nFranjaHoraria.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Franja horaria eliminada correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Franjas Horarias", DFranjaHoraria.HEADERS, nFranjaHoraria.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void tipoVehiculo(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nTipoVehiculo.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Tipo de vehículo guardado correctamente.");
                    break;
                case Token.MODIFICAR:
                    nTipoVehiculo.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Tipo de vehículo modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nTipoVehiculo.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Tipo de vehículo eliminado correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Tipos de Vehículo", DTipoVehiculo.HEADERS, nTipoVehiculo.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void usuario(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    String[] datosUsuario = nUsuario.guardar(event.getParams());
                    ArrayList<String[]> filaUsuario = new ArrayList<>();
                    filaUsuario.add(datosUsuario);
                    tableNotifySuccess(event.getSender(), "Usuario registrado correctamente", DUsuario.HEADERS, filaUsuario);
                    break;
                case Token.MODIFICAR:
                    nUsuario.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Usuario modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nUsuario.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Usuario desactivado correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Usuarios", DUsuario.HEADERS, nUsuario.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void certificacion(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    String[] datosCert = nControlCert.guardar(event.getParams());
                    if (datosCert != null) {
                        try {
                            byte[] pdf = CertificadoPdfBuilder.generar(datosCert);
                            sendEmail(Email.conPdf(event.getSender(), Email.SUBJECT,
                                HtmlBuilder.generateCertificadoConfirmacion(datosCert), pdf));
                        } catch (Exception pdfEx) {
                            simpleNotifySuccess(event.getSender(), "Certificación registrada correctamente.");
                        }
                    } else {
                        simpleNotifySuccess(event.getSender(), "Certificación registrada correctamente.");
                    }
                    break;
                case Token.MODIFICAR:
                    nControlCert.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Certificación actualizada correctamente.");
                    break;
                case Token.ELIMINAR:
                    nControlCert.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Certificación eliminada correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Certificaciones", DControlCert.HEADERS, nControlCert.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL][CERT] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS][CERT] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void inscripcion(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    String[] datosInsc = nInscripcion.guardar(event.getParams());
                    ArrayList<String[]> filaInsc = new ArrayList<>();
                    filaInsc.add(datosInsc);
                    tableNotifySuccess(event.getSender(), "Inscripción registrada correctamente", DInscripcion.HEADERS, filaInsc);
                    break;
                case Token.MODIFICAR:
                    nInscripcion.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Inscripción actualizada correctamente.");
                    break;
                case Token.ELIMINAR:
                    nInscripcion.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Inscripción cancelada correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Inscripciones", DInscripcion.HEADERS, nInscripcion.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void curso(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nCurso.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Curso registrado correctamente.");
                    break;
                case Token.MODIFICAR:
                    nCurso.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Curso modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nCurso.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Curso cancelado correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Cursos", DCurso.HEADERS, nCurso.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void vehiculo(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nVehiculo.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Vehículo registrado correctamente.");
                    break;
                case Token.MODIFICAR:
                    nVehiculo.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Vehículo modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nVehiculo.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Vehículo dado de baja correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Vehículos", DVehiculo.HEADERS, nVehiculo.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void tipoCurso(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nTipoCurso.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Tipo de curso guardado correctamente.");
                    break;
                case Token.MODIFICAR:
                    nTipoCurso.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Tipo de curso modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nTipoCurso.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Tipo de curso eliminado correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Tipos de Curso", DTipoCurso.HEADERS, nTipoCurso.listar());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            LOG.warning("[VALIDACION] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void pago(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    String[] result = nPago.guardar(event.getParams(), event.getSender());
                    if ("efectivo".equals(result[0])) {
                        // result: [0]="efectivo" [1]=monto [2]=nroCuota [3]=totalCuotas
                        simpleNotifySuccess(event.getSender(),
                            "Pago en efectivo registrado. Cuota " + result[2] +
                            " de " + result[3] + " — Bs. " + result[1] + ".");
                    } else {
                        // result: [0]="qr" [1]=qrBase64 [2]=transactionId [3]=monto [4]=nroCuota [5]=totalCuotas
                        byte[] qrBytes = java.util.Base64.getDecoder().decode(result[1].replace("\\/", "/"));
                        sendEmail(new Email(event.getSender(), Email.SUBJECT,
                            HtmlBuilder.generateQR(result[3], result[4], result[5], result[2]),
                            qrBytes));
                    }
                    break;
                case Token.MODIFICAR:
                    nPago.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Pago modificado correctamente.");
                    break;
                case Token.ELIMINAR:
                    nPago.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Pago eliminado correctamente.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Pagos", DPago.HEADERS, nPago.listar());
                    break;
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            LOG.warning("[VALIDACION][PAGO] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "[IO][PAGO] sender: " + event.getSender() + " | Error PagoFacil", ex);
            validationError(event.getSender(), "Error al conectar con PagoFacil: " + ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL][PAGO] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS][PAGO] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void reporte(TokenEvent event) {
        try {
            // El "action" actúa como sub-tipo: LISTAR=usuarios, AGREGAR=vehículos,
            // MODIFICAR=inscripciones, ELIMINAR=pagos
            switch (event.getAction()) {
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(),
                        "Reporte de Usuarios por Rol y Estado",
                        DReporte.HEADERS_USUARIOS,
                        nReporte.reporteUsuarios());
                    break;
                case Token.AGREGAR:
                    tableNotifySuccess(event.getSender(),
                        "Reporte de Vehículos por Tipo y Estado",
                        DReporte.HEADERS_VEHICULOS,
                        nReporte.reporteVehiculos());
                    break;
                case Token.MODIFICAR:
                    tableNotifySuccess(event.getSender(),
                        "Reporte de Inscripciones por Tipo de Curso",
                        DReporte.HEADERS_INSCRIPCIONES,
                        nReporte.reporteInscripciones());
                    break;
                case Token.ELIMINAR:
                    tableNotifySuccess(event.getSender(),
                        "Reporte de Pagos por Método",
                        DReporte.HEADERS_PAGOS,
                        nReporte.reportePagos());
                    break;
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL][REPORTE] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void reserva(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    String[] datosCurso = nReserva.reservar(event.getParams());
                    ArrayList<String[]> filaCurso = new ArrayList<>();
                    filaCurso.add(datosCurso);
                    tableNotifySuccess(event.getSender(), "Curso reservado exitosamente", DReserva.HEADERS, filaCurso);
                    break;
                case Token.ELIMINAR:
                    nReserva.cancelar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Reserva cancelada. El curso vuelve a estar disponible.");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Cursos Disponibles para Reservar", DReserva.HEADERS, nReserva.listar());
                    break;
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            LOG.warning("[VALIDACION][RESERVA] sender: " + event.getSender() + " | " + ex.getMessage());
            validationError(event.getSender(), ex.getMessage());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "[SQL][RESERVA] sender: " + event.getSender(), ex);
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warning("[PARAMS][RESERVA] sender: " + event.getSender() + " | Parametros insuficientes");
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void error(TokenEvent event) {
        this.handleError(event.getAction(), event.getSender(), event.getParams());
    }

    // ── Manejo de errores ─────────────────────────────────────────────────────

    private void handleError(int type, String email, List<String> args) {
        Email emailObject;
        switch (type) {
            case Token.ERROR_CHARACTER:
                emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[]{
                        "Caracter desconocido",
                        "No se pudo ejecutar: [" + args.get(0).trim() + "]",
                        "El caracter \"" + args.get(1) + "\" no es válido."
                    }));
                break;
            case Token.ERROR_COMMAND:
                emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateErrorWithHelp(
                        new String[]{
                            "Comando desconocido",
                            "\"" + args.get(0).trim() + "\" no es un comando válido.",
                            "A continuación se muestran los comandos disponibles:"
                        },
                        "Comandos disponibles",
                        Comandos.HEADERS,
                        Comandos.listar()
                    ));
                break;
            case CONSTRAINTS_ERROR:
                emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[]{
                        "Error en la base de datos",
                        "Referencia a información inexistente o restricción de integridad."
                    }));
                break;
            case INDEX_OUT_OF_BOUND_ERROR:
                emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[]{
                        "Cantidad de parámetros incorrecta",
                        "Faltan parámetros para ejecutar la acción solicitada."
                    }));
                break;
            case PARSE_ERROR:
                emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[]{
                        "Error de formato",
                        "Uno de los valores introducidos tiene un formato incorrecto."
                    }));
                break;
            default:
                emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[]{"Error desconocido"}));
        }
        this.sendEmail(emailObject);
    }

    /** Envía un correo de error de validación con el mensaje descriptivo del campo. */
    private void validationError(String email, String message) {
        sendEmail(new Email(email, Email.SUBJECT,
            HtmlBuilder.generateText(new String[]{"Error de validación", message})));
    }

    // ── Notificaciones ────────────────────────────────────────────────────────

    private void simpleNotifySuccess(String email, String message) {
        sendEmail(new Email(email, Email.SUBJECT,
            HtmlBuilder.generateText(new String[]{"Operación exitosa", message})));
    }

    private void tableNotifySuccess(String email, String title, String[] headers, ArrayList<String[]> data) {
        sendEmail(new Email(email, Email.SUBJECT,
            HtmlBuilder.generateTable(title, headers, data)));
    }

    private void tableGraficaSuccess3(String email, String title, ArrayList<String[]> data) {
        sendEmail(new Email(email, Email.SUBJECT,
            HtmlBuilder.generateGrafica3(title, data)));
    }

    private void sendEmail(Email email) {
        SendEmailThread sendEmail = new SendEmailThread(email);
        Thread thread = new Thread(sendEmail);
        thread.setName("Send Email Thread");
        thread.start();
    }
}
