package interpreter.analex.utils;

public class Token {

    private int name;
    private int attribute;

    // ── Tipos de token ────────────────────────────────────────────────────────
    public static final int CU       = 0;
    public static final int ACTION   = 1;
    public static final int PARAMS   = 2;
    public static final int END      = 3;
    public static final int ERROR    = 4;
    public static final int COMPOUND = 5; // token compuesto: lleva CU + acción (ej. INSCAT)

    // ── Casos de uso activos (100-199) ────────────────────────────────────────
    public static final int HELP         = 100;
    public static final int HELP_FLUJO   = 101;
    public static final int ROL          = 107;
    public static final int METODOPAGO   = 108;
    public static final int PLANPAGO     = 109;
    public static final int FRANJAH      = 110;
    public static final int TIPOVEHICULO = 111;
    public static final int TIPOCURSO    = 112;
    public static final int USUARIO      = 113;
    public static final int VEHICULO     = 114;
    public static final int CURSO        = 115;
    public static final int INSCRIPCION  = 116;
    public static final int CERTIFICACION = 117;
    public static final int PAGO          = 118;
    public static final int REPORTE       = 119;
    public static final int RESERVA       = 120;

    // ── Acciones activas (200-299) ────────────────────────────────────────────
    public static final int AGREGAR  = 207;
    public static final int ELIMINAR = 208;
    public static final int MODIFICAR= 209;
    public static final int LISTAR   = 210;
    public static final int GRAFICA  = 211;

    // ── Tokens compuestos (300-399) ───────────────────────────────────────────
    //    Convenio: x0=LISTAR, x1=AGREGAR, x2=MODIFICAR, x3=ELIMINAR
    //    330-339 = ROL
    public static final int LISROL = 330;
    public static final int INSROL = 331;
    public static final int MODROL = 332;
    public static final int DELROL = 333;

    //    340-349 = METODOPAGO
    public static final int LISMET = 340;
    public static final int INSMET = 341;
    public static final int MODMET = 342;
    public static final int DELMET = 343;

    //    350-359 = PLANPAGO
    public static final int LISPLN = 350;
    public static final int INSPLN = 351;
    public static final int MODPLN = 352;
    public static final int DELPLN = 353;

    //    360-369 = FRANJAH
    public static final int LISFRA = 360;
    public static final int INSFRA = 361;
    public static final int MODFRA = 362;
    public static final int DELFRA = 363;

    //    370-379 = TIPOVEHICULO
    public static final int LISTYV = 370;
    public static final int INSTYV = 371;
    public static final int MODTYV = 372;
    public static final int DELTYV = 373;

    //    380-389 = USUARIO
    public static final int LISUSU = 380;
    public static final int INSUSU = 381;
    public static final int MODUSU = 382;
    public static final int DELUSU = 383;

    //    430-439 = PAGO
    public static final int LISPAG = 430;
    public static final int INSPAG = 431;
    public static final int MODPAG = 432;
    public static final int DELPAG = 433;

    //    440-449 = CERTIFICACION
    public static final int LISCRT = 440;
    public static final int INSCRT = 441;
    public static final int MODCRT = 442;
    public static final int DELCRT = 443;

    //    450-459 = REPORTE (solo lectura; x0=usuarios, x1=vehículos, x2=inscripciones, x3=pagos)
    public static final int REPUSU = 450;
    public static final int REPVEH = 451;
    public static final int REPINS = 452;
    public static final int REPPAG = 453;

    //    460-469 = RESERVA
    public static final int LISRES = 460;
    public static final int INSRES = 461;
    public static final int DELRES = 463;

    //    420-429 = INSCRIPCION
    public static final int LISINC = 420;
    public static final int INSINC = 421;
    public static final int MODINC = 422;
    public static final int DELINC = 423;

    //    410-419 = CURSO (instancia)
    public static final int LISCUR = 410;
    public static final int INSCUR = 411;
    public static final int MODCUR = 412;
    public static final int DELCUR = 413;

    //    400-409 = VEHICULO
    public static final int LISVEH = 400;
    public static final int INSVEH = 401;
    public static final int MODVEH = 402;
    public static final int DELVEH = 403;

    //    390-399 = TIPOCURSO
    public static final int LISTCU = 390;
    public static final int INSTCU = 391;
    public static final int MODTCU = 392;
    public static final int DELTCU = 393;

    // ── Errores (500+) ────────────────────────────────────────────────────────
    public static final int ERROR_COMMAND   = 500;
    public static final int ERROR_CHARACTER = 501;

    // ── Lexemas de tipos ──────────────────────────────────────────────────────
    public static final String LEXEME_CU     = "caso de uso";
    public static final String LEXEME_ACTION = "action";
    public static final String LEXEME_PARAMS = "params";
    public static final String LEXEME_END    = "end";
    public static final String LEXEME_ERROR  = "error";

    // ── Lexemas de casos de uso ───────────────────────────────────────────────
    public static final String LEXEME_HELP         = "ayuda";
    public static final String LEXEME_HELP_FLUJO   = "ayuda_administrativo";
    public static final String LEXEME_ROL          = "rol";
    public static final String LEXEME_METODOPAGO   = "metodopago";
    public static final String LEXEME_PLANPAGO     = "planpago";
    public static final String LEXEME_FRANJAH      = "franjah";
    public static final String LEXEME_TIPOVEHICULO = "tipovehiculo";
    public static final String LEXEME_TIPOCURSO    = "tipocurso";
    public static final String LEXEME_USUARIO      = "usuario";
    public static final String LEXEME_VEHICULO     = "vehiculo";
    public static final String LEXEME_CURSO        = "curso";
    public static final String LEXEME_INSCRIPCION  = "inscripcion";
    public static final String LEXEME_CERTIFICACION = "certificacion";
    public static final String LEXEME_PAGO          = "pago";
    public static final String LEXEME_REPORTE       = "reporte";
    public static final String LEXEME_RESERVA       = "reserva";

    // ── Lexemas de acciones ───────────────────────────────────────────────────
    public static final String LEXEME_AGREGAR   = "agregar";
    public static final String LEXEME_ELIMINAR  = "eliminar";
    public static final String LEXEME_MODIFICAR = "modificar";
    public static final String LEXEME_LISTAR    = "listar";
    public static final String LEXEME_GRAFICA   = "grafica";

    // ── Lexemas de tokens compuestos ──────────────────────────────────────────
    public static final String LEXEME_LISROL = "lisrol";
    public static final String LEXEME_INSROL = "insrol";
    public static final String LEXEME_MODROL = "modrol";
    public static final String LEXEME_DELROL = "delrol";

    public static final String LEXEME_LISMET = "lismet";
    public static final String LEXEME_INSMET = "insmet";
    public static final String LEXEME_MODMET = "modmet";
    public static final String LEXEME_DELMET = "delmet";

    public static final String LEXEME_LISPLN = "lispln";
    public static final String LEXEME_INSPLN = "inspln";
    public static final String LEXEME_MODPLN = "modpln";
    public static final String LEXEME_DELPLN = "delpln";

    public static final String LEXEME_LISFRA = "lisfra";
    public static final String LEXEME_INSFRA = "insfra";
    public static final String LEXEME_MODFRA = "modfra";
    public static final String LEXEME_DELFRA = "delfra";

    public static final String LEXEME_LISTYV = "listyv";
    public static final String LEXEME_INSTYV = "instyv";
    public static final String LEXEME_MODTYV = "modtyv";
    public static final String LEXEME_DELTYV = "deltyv";

    public static final String LEXEME_LISTCU = "listcu";
    public static final String LEXEME_INSTCU = "instcu";
    public static final String LEXEME_MODTCU = "modtcu";
    public static final String LEXEME_DELTCU = "deltcu";

    public static final String LEXEME_LISUSU = "lisusu";
    public static final String LEXEME_INSUSU = "insusu";
    public static final String LEXEME_MODUSU = "modusu";
    public static final String LEXEME_DELUSU = "delusu";

    public static final String LEXEME_LISVEH = "lisveh";
    public static final String LEXEME_INSVEH = "insveh";
    public static final String LEXEME_MODVEH = "modveh";
    public static final String LEXEME_DELVEH = "delveh";

    public static final String LEXEME_LISCUR = "liscur";
    public static final String LEXEME_INSCUR = "inscur";
    public static final String LEXEME_MODCUR = "modcur";
    public static final String LEXEME_DELCUR = "delcur";

    public static final String LEXEME_LISINC = "lisinc";
    public static final String LEXEME_INSINC = "insinc";
    public static final String LEXEME_MODINC = "modinc";
    public static final String LEXEME_DELINC = "delinc";

    public static final String LEXEME_LISCRT = "liscrt";
    public static final String LEXEME_INSCRT = "inscrt";
    public static final String LEXEME_MODCRT = "modcrt";
    public static final String LEXEME_DELCRT = "delcrt";

    public static final String LEXEME_LISPAG = "lispag";
    public static final String LEXEME_INSPAG = "inspag";
    public static final String LEXEME_MODPAG = "modpag";
    public static final String LEXEME_DELPAG = "delpag";

    public static final String LEXEME_REPUSU = "repusu";
    public static final String LEXEME_REPVEH = "repveh";
    public static final String LEXEME_REPINS = "repins";
    public static final String LEXEME_REPPAG = "reppag";

    public static final String LEXEME_LISRES = "lisres";
    public static final String LEXEME_INSRES = "insres";
    public static final String LEXEME_DELRES = "delres";

    public static final String LEXEME_ERROR_COMMAND   = "UNKNOWN COMMAND";
    public static final String LEXEME_ERROR_CHARACTER = "UNKNOWN CHARACTER";

    // ── Constructores ─────────────────────────────────────────────────────────
    public Token() {}

    public Token(String token) {
        int id = findByLexeme(token);
        if (id != -1) {
            if (100 <= id && id < 200) {
                this.name = CU;
                this.attribute = id;
            } else if (200 <= id && id < 300) {
                this.name = ACTION;
                this.attribute = id;
            } else if (300 <= id && id < 500) {
                this.name = COMPOUND;
                this.attribute = id;
            }
        } else {
            this.name = ERROR;
            this.attribute = ERROR_COMMAND;
            System.err.println("Token desconocido: " + token);
        }
    }

    public Token(int name) {
        this.name = name;
    }

    public Token(int name, int attribute) {
        this.name = name;
        this.attribute = attribute;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public int getName()            { return name; }
    public void setName(int name)   { this.name = name; }
    public int getAttribute()       { return attribute; }
    public void setAttribute(int a) { this.attribute = a; }

    // ── Helpers para tokens compuestos ────────────────────────────────────────

    /** Devuelve el CU (entidad) del token compuesto. */
    public static int getCompoundCU(int compound) {
        if (compound >= 330 && compound < 340) return ROL;
        if (compound >= 340 && compound < 350) return METODOPAGO;
        if (compound >= 350 && compound < 360) return PLANPAGO;
        if (compound >= 360 && compound < 370) return FRANJAH;
        if (compound >= 370 && compound < 380) return TIPOVEHICULO;
        if (compound >= 380 && compound < 390) return USUARIO;
        if (compound >= 390 && compound < 400) return TIPOCURSO;
        if (compound >= 400 && compound < 410) return VEHICULO;
        if (compound >= 410 && compound < 420) return CURSO;
        if (compound >= 420 && compound < 430) return INSCRIPCION;
        if (compound >= 430 && compound < 440) return PAGO;
        if (compound >= 440 && compound < 450) return CERTIFICACION;
        if (compound >= 450 && compound < 460) return REPORTE;
        if (compound >= 460 && compound < 470) return RESERVA;
        return -1;
    }

    /** Devuelve la acción del token compuesto.
     *  Convenio: x0=LISTAR, x1=AGREGAR, x2=MODIFICAR, x3=ELIMINAR */
    public static int getCompoundAction(int compound) {
        switch (compound % 10) {
            case 0:  return LISTAR;
            case 1:  return AGREGAR;
            case 2:  return MODIFICAR;
            case 3:  return ELIMINAR;
            default: return -1;
        }
    }

    // ── toString ──────────────────────────────────────────────────────────────
    @Override
    public String toString() {
        if (name == COMPOUND)
            return "< COMPOUND , " + getStringToken(attribute) + " >";
        if (0 <= name && name <= 1)
            return "< " + getStringToken(name) + " , " + getStringToken(attribute) + ">";
        if (name == 2)
            return "< " + getStringToken(name) + " , " + attribute + ">";
        if (name == 3)
            return "< " + getStringToken(name) + " , _______ >";
        if (name == 4)
            return "< " + getStringToken(name) + " , " + getStringToken(attribute) + ">";
        return "< TOKEN , DESCONOCIDO>";
    }

    public String getStringToken(int token) {
        switch (token) {
            case CU:       return LEXEME_CU;
            case ACTION:   return LEXEME_ACTION;
            case PARAMS:   return LEXEME_PARAMS;
            case END:      return LEXEME_END;
            case ERROR:    return LEXEME_ERROR;

            case HELP:        return LEXEME_HELP;
            case HELP_FLUJO:  return LEXEME_HELP_FLUJO;

            case AGREGAR:   return LEXEME_AGREGAR;
            case ELIMINAR:  return LEXEME_ELIMINAR;
            case MODIFICAR: return LEXEME_MODIFICAR;
            case LISTAR:    return LEXEME_LISTAR;
            case GRAFICA:   return LEXEME_GRAFICA;

            case ROL:          return LEXEME_ROL;
            case METODOPAGO:   return LEXEME_METODOPAGO;
            case PLANPAGO:     return LEXEME_PLANPAGO;
            case FRANJAH:      return LEXEME_FRANJAH;
            case TIPOVEHICULO: return LEXEME_TIPOVEHICULO;
            case TIPOCURSO:    return LEXEME_TIPOCURSO;
            case USUARIO:      return LEXEME_USUARIO;
            case VEHICULO:     return LEXEME_VEHICULO;
            case CURSO:        return LEXEME_CURSO;
            case INSCRIPCION:   return LEXEME_INSCRIPCION;
            case CERTIFICACION: return LEXEME_CERTIFICACION;
            case PAGO:          return LEXEME_PAGO;
            case REPORTE:       return LEXEME_REPORTE;
            case RESERVA:       return LEXEME_RESERVA;

            case LISROL: return LEXEME_LISROL;
            case INSROL: return LEXEME_INSROL;
            case MODROL: return LEXEME_MODROL;
            case DELROL: return LEXEME_DELROL;
            case LISMET: return LEXEME_LISMET;
            case INSMET: return LEXEME_INSMET;
            case MODMET: return LEXEME_MODMET;
            case DELMET: return LEXEME_DELMET;
            case LISPLN: return LEXEME_LISPLN;
            case INSPLN: return LEXEME_INSPLN;
            case MODPLN: return LEXEME_MODPLN;
            case DELPLN: return LEXEME_DELPLN;
            case LISFRA: return LEXEME_LISFRA;
            case INSFRA: return LEXEME_INSFRA;
            case MODFRA: return LEXEME_MODFRA;
            case DELFRA: return LEXEME_DELFRA;
            case LISTYV: return LEXEME_LISTYV;
            case INSTYV: return LEXEME_INSTYV;
            case MODTYV: return LEXEME_MODTYV;
            case DELTYV: return LEXEME_DELTYV;
            case LISTCU: return LEXEME_LISTCU;
            case INSTCU: return LEXEME_INSTCU;
            case MODTCU: return LEXEME_MODTCU;
            case DELTCU: return LEXEME_DELTCU;
            case LISUSU: return LEXEME_LISUSU;
            case INSUSU: return LEXEME_INSUSU;
            case MODUSU: return LEXEME_MODUSU;
            case DELUSU: return LEXEME_DELUSU;
            case LISVEH: return LEXEME_LISVEH;
            case INSVEH: return LEXEME_INSVEH;
            case MODVEH: return LEXEME_MODVEH;
            case DELVEH: return LEXEME_DELVEH;
            case LISCUR: return LEXEME_LISCUR;
            case INSCUR: return LEXEME_INSCUR;
            case MODCUR: return LEXEME_MODCUR;
            case DELCUR: return LEXEME_DELCUR;
            case LISINC: return LEXEME_LISINC;
            case INSINC: return LEXEME_INSINC;
            case MODINC: return LEXEME_MODINC;
            case DELINC: return LEXEME_DELINC;
            case LISCRT: return LEXEME_LISCRT;
            case INSCRT: return LEXEME_INSCRT;
            case MODCRT: return LEXEME_MODCRT;
            case DELCRT: return LEXEME_DELCRT;
            case LISPAG: return LEXEME_LISPAG;
            case INSPAG: return LEXEME_INSPAG;
            case MODPAG: return LEXEME_MODPAG;
            case DELPAG: return LEXEME_DELPAG;
            case REPUSU: return LEXEME_REPUSU;
            case REPVEH: return LEXEME_REPVEH;
            case REPINS: return LEXEME_REPINS;
            case REPPAG: return LEXEME_REPPAG;
            case LISRES: return LEXEME_LISRES;
            case INSRES: return LEXEME_INSRES;
            case DELRES: return LEXEME_DELRES;

            case ERROR_COMMAND:   return LEXEME_ERROR_COMMAND;
            case ERROR_CHARACTER: return LEXEME_ERROR_CHARACTER;
            default: return "N: " + token;
        }
    }

    private int findByLexeme(String lexeme) {
        switch (lexeme) {
            case LEXEME_CU:     return CU;
            case LEXEME_ACTION: return ACTION;
            case LEXEME_PARAMS: return PARAMS;
            case LEXEME_END:    return END;
            case LEXEME_ERROR:  return ERROR;

            case LEXEME_HELP:        return HELP;
            case LEXEME_HELP_FLUJO:  return HELP_FLUJO;

            case LEXEME_AGREGAR:   return AGREGAR;
            case LEXEME_ELIMINAR:  return ELIMINAR;
            case LEXEME_MODIFICAR: return MODIFICAR;
            case LEXEME_LISTAR:    return LISTAR;
            case LEXEME_GRAFICA:   return GRAFICA;

            case LEXEME_ROL:          return ROL;
            case LEXEME_METODOPAGO:   return METODOPAGO;
            case LEXEME_PLANPAGO:     return PLANPAGO;
            case LEXEME_FRANJAH:      return FRANJAH;
            case LEXEME_TIPOVEHICULO: return TIPOVEHICULO;
            case LEXEME_TIPOCURSO:    return TIPOCURSO;

            case LEXEME_LISROL: return LISROL;
            case LEXEME_INSROL: return INSROL;
            case LEXEME_MODROL: return MODROL;
            case LEXEME_DELROL: return DELROL;
            case LEXEME_LISMET: return LISMET;
            case LEXEME_INSMET: return INSMET;
            case LEXEME_MODMET: return MODMET;
            case LEXEME_DELMET: return DELMET;
            case LEXEME_LISPLN: return LISPLN;
            case LEXEME_INSPLN: return INSPLN;
            case LEXEME_MODPLN: return MODPLN;
            case LEXEME_DELPLN: return DELPLN;
            case LEXEME_LISFRA: return LISFRA;
            case LEXEME_INSFRA: return INSFRA;
            case LEXEME_MODFRA: return MODFRA;
            case LEXEME_DELFRA: return DELFRA;
            case LEXEME_LISTYV: return LISTYV;
            case LEXEME_INSTYV: return INSTYV;
            case LEXEME_MODTYV: return MODTYV;
            case LEXEME_DELTYV: return DELTYV;
            case LEXEME_LISTCU: return LISTCU;
            case LEXEME_INSTCU: return INSTCU;
            case LEXEME_MODTCU: return MODTCU;
            case LEXEME_DELTCU: return DELTCU;
            case LEXEME_LISUSU:   return LISUSU;
            case LEXEME_INSUSU:   return INSUSU;
            case LEXEME_MODUSU:   return MODUSU;
            case LEXEME_DELUSU:   return DELUSU;
            case LEXEME_USUARIO:  return USUARIO;
            case LEXEME_LISVEH:   return LISVEH;
            case LEXEME_INSVEH:   return INSVEH;
            case LEXEME_MODVEH:   return MODVEH;
            case LEXEME_DELVEH:   return DELVEH;
            case LEXEME_VEHICULO: return VEHICULO;
            case LEXEME_LISCUR:      return LISCUR;
            case LEXEME_INSCUR:      return INSCUR;
            case LEXEME_MODCUR:      return MODCUR;
            case LEXEME_DELCUR:      return DELCUR;
            case LEXEME_CURSO:       return CURSO;
            case LEXEME_LISINC:       return LISINC;
            case LEXEME_INSINC:       return INSINC;
            case LEXEME_MODINC:       return MODINC;
            case LEXEME_DELINC:       return DELINC;
            case LEXEME_INSCRIPCION:  return INSCRIPCION;
            case LEXEME_LISCRT:        return LISCRT;
            case LEXEME_INSCRT:        return INSCRT;
            case LEXEME_MODCRT:        return MODCRT;
            case LEXEME_DELCRT:        return DELCRT;
            case LEXEME_CERTIFICACION: return CERTIFICACION;
            case LEXEME_LISPAG:        return LISPAG;
            case LEXEME_INSPAG:        return INSPAG;
            case LEXEME_MODPAG:        return MODPAG;
            case LEXEME_DELPAG:        return DELPAG;
            case LEXEME_PAGO:          return PAGO;
            case LEXEME_REPUSU:        return REPUSU;
            case LEXEME_REPVEH:        return REPVEH;
            case LEXEME_REPINS:        return REPINS;
            case LEXEME_REPPAG:        return REPPAG;
            case LEXEME_REPORTE:       return REPORTE;
            case LEXEME_LISRES:        return LISRES;
            case LEXEME_INSRES:        return INSRES;
            case LEXEME_DELRES:        return DELRES;
            case LEXEME_RESERVA:       return RESERVA;

            default: return -1;
        }
    }
}
