package negociodato.utils;

import java.util.ArrayList;

public class Comandos {

    public static final String[] HEADERS = {"ID", "COMANDO", "DESCRIPCION", "PARAMETROS", "EJEMPLO"};

    private static final ArrayList<String[]> LISTA = new ArrayList<>();

    static {                                              
        LISTA.add(new String[]{"1", "LISROL", "Listar roles",          "*",                          "LISROL[\"*\"]"});
        // LISTA.add(new String[]{"2", "INSROL", "Insertar rol",          "nombre, descripcion",        "INSROL[\"Instructor\",\"Docente de manejo\"]"});
        // LISTA.add(new String[]{"3", "MODROL", "Modificar rol",         "id, nombre, descripcion",    "MODROL[\"1\",\"Instructor\",\"Docente de manejo\"]"});
        // LISTA.add(new String[]{"4", "DELROL", "Eliminar rol",          "id",                         "DELROL[\"1\"]"});
        // MetodoPago
        LISTA.add(new String[]{"2", "LISMET", "Listar métodos de pago", "*",                         "LISMET[\"*\"]"});
        // LISTA.add(new String[]{"6", "INSMET", "Insertar método pago",   "nombre, descripcion",       "INSMET[\"Efectivo\",\"Pago en caja\"]"});
        // LISTA.add(new String[]{"7", "MODMET", "Modificar método pago",  "id, nombre, descripcion",   "MODMET[\"1\",\"Efectivo\",\"Pago en caja\"]"});
        // LISTA.add(new String[]{"8", "DELMET", "Eliminar método pago",   "id",                        "DELMET[\"1\"]"});
        // PlanPago
        LISTA.add(new String[]{"3", "LISPLN", "Listar planes de pago",  "*",                         "LISPLN[\"*\"]"});
        //LISTA.add(new String[]{"10", "INSPLN", "Insertar plan de pago",  "nombre, nro_cuotas, estado","INSPLN[\"Contado\",\"1\",\"activo\"]"});
        //LISTA.add(new String[]{"11", "MODPLN", "Modificar plan de pago", "id, nombre, nro_cuotas, estado","MODPLN[\"1\",\"Contado\",\"1\",\"activo\"]"});
        //LISTA.add(new String[]{"12", "DELPLN", "Eliminar plan de pago",  "id",                        "DELPLN[\"1\"]"});
        // FranjaHoraria
        LISTA.add(new String[]{"4", "LISFRA", "Listar franjas horarias","*",                         "LISFRA[\"*\"]"});
        //LISTA.add(new String[]{"5", "INSFRA", "Insertar franja horaria","hora_inicio, hora_fin",     "INSFRA[\"08:00\",\"10:00\"]"});
        //LISTA.add(new String[]{"6", "MODFRA", "Modificar franja horaria","id, hora_inicio, hora_fin","MODFRA[\"1\",\"08:00\",\"10:00\"]"});
        //LISTA.add(new String[]{"7", "DELFRA", "Eliminar franja horaria","id",                        "DELFRA[\"1\"]"});
        // TipoVehiculo
        LISTA.add(new String[]{"5", "LISTYV", "Listar tipos de vehículo","*",                        "LISTYV[\"*\"]"});
        //LISTA.add(new String[]{"9", "INSTYV", "Insertar tipo vehículo", "nombre, descripcion",       "INSTYV[\"Automovil\",\"Vehículo de 4 ruedas\"]"});
        //LISTA.add(new String[]{"10", "MODTYV", "Modificar tipo vehículo","id, nombre, descripcion",   "MODTYV[\"1\",\"Automovil\",\"Vehículo de 4 ruedas\"]"});
        //LISTA.add(new String[]{"11", "DELTYV", "Eliminar tipo vehículo", "id",                        "DELTYV[\"1\"]"});
        // TipoCurso
        LISTA.add(new String[]{"6", "LISTCU", "Listar tipos de curso",  "*",                         "LISTCU[\"*\"]"});
        //LISTA.add(new String[]{"13", "INSTCU", "Insertar tipo de curso", "nombre, descripcion, precio, estado, duracion_horas, nombre_tipo_vehiculo","INSTCU[\"Basico\",\"Curso inicial\",\"500\",\"activo\",\"20\",\"Automovil\"]"});
        //LISTA.add(new String[]{"14", "MODTCU", "Modificar tipo de curso","id, nombre, descripcion, precio, estado, duracion_horas","MODTCU[\"1\",\"Basico\",\"Curso inicial\",\"500\",\"activo\",\"20\"]"});
        //LISTA.add(new String[]{"15", "DELTCU", "Eliminar tipo de curso", "id",                        "DELTCU[\"1\"]"});
        // Usuario
        LISTA.add(new String[]{"7", "LISUSU", "Listar usuarios",       "* — muestra: id, nombre, apellido, fecha_nac, genero, documento, correo, telefono, estado, rol", "LISUSU[\"*\"]"});
        LISTA.add(new String[]{"8", "INSUSU", "Registrar usuario",     "nombre, apellido, fecha_nac (YYYY-MM-DD), genero (M/F), nro_doc, correo, telefono, direccion, pass, rol_id", "INSUSU[\"Juan\",\"Perez\",\"2000-05-15\",\"M\",\"12345678\",\"juan@mail.com\",\"70000000\",\"Av. Principal\",\"pass123\",\"4\"]"});
        LISTA.add(new String[]{"9", "MODUSU", "Modificar usuario",     "id, nombre, apellido, fecha_nac (YYYY-MM-DD), genero (M/F), nro_doc, correo, telefono, direccion, estado", "MODUSU[\"1\",\"Juan\",\"Perez\",\"2000-05-15\",\"M\",\"12345678\",\"juan@mail.com\",\"70000000\",\"Av. Principal\",\"activo\"]"});
        LISTA.add(new String[]{"10", "DELUSU", "Desactivar usuario",    "id",                                                                                       "DELUSU[\"1\"]"});
        // Vehiculo
        LISTA.add(new String[]{"11", "LISVEH", "Listar vehículos",       "*",                                                                        "LISVEH[\"*\"]"});
        LISTA.add(new String[]{"12", "INSVEH", "Registrar vehículo",     "placa, marca, modelo, estado, fecha_mant (YYYY-MM-DD), nombre_tipo",       "INSVEH[\"ABC-123\",\"Toyota\",\"Corolla\",\"disponible\",\"2025-01-15\",\"Automovil\"]"});
        LISTA.add(new String[]{"13", "MODVEH", "Modificar vehículo",     "id, placa, marca, modelo, estado, fecha_mant (YYYY-MM-DD)",                "MODVEH[\"1\",\"ABC-123\",\"Toyota\",\"Corolla\",\"disponible\",\"2025-06-01\"]"});
        LISTA.add(new String[]{"14", "DELVEH", "Dar de baja vehículo",   "id",                                                                       "DELVEH[\"1\"]"});
        // Curso (instancia / reserva)
        LISTA.add(new String[]{"15", "LISCUR", "Listar cursos",          "*",                                                                                                        "LISCUR[\"*\"]"});
        LISTA.add(new String[]{"16", "INSCUR", "Crear curso",            "fecha_ini, fecha_fin, precio, estado, id_instructor, id_vehiculo, id_tipo_curso, id_franja",              "INSCUR[\"2025-07-01\",\"2025-07-31\",\"500\",\"abierto\",\"1\",\"1\",\"1\",\"1\"]"});
        LISTA.add(new String[]{"17", "MODCUR", "Modificar curso",        "id, fecha_ini, fecha_fin, precio, estado",                                                                 "MODCUR[\"1\",\"2025-07-01\",\"2025-07-31\",\"500\",\"cerrado\"]"});
        LISTA.add(new String[]{"18", "DELCUR", "Cancelar curso",         "id",                                                                                                       "DELCUR[\"1\"]"});
        // Inscripcion
        LISTA.add(new String[]{"19", "LISINC", "Listar inscripciones",   "*",                                                                          "LISINC[\"*\"]"});
        LISTA.add(new String[]{"20", "INSINC", "Inscribir estudiante",   "fecha (YYYY-MM-DD), estado, id_estudiante, id_curso, id_plan_pago",          "INSINC[\"2025-07-01\",\"activa\",\"2\",\"1\",\"1\"]"});
        LISTA.add(new String[]{"21", "MODINC", "Actualizar inscripcion", "id, estado, aprobados (0/1)",                                               "MODINC[\"1\",\"completada\",\"1\"]"});
        LISTA.add(new String[]{"22", "DELINC", "Cancelar inscripcion",   "id",                                                                         "DELINC[\"1\"]"});
        // Control y Certificacion
        LISTA.add(new String[]{"23", "LISCRT", "Listar certificaciones",  "*",                                                                              "LISCRT[\"*\"]"});
        LISTA.add(new String[]{"24", "INSCRT", "Registrar certificacion", "id_inscripcion, nota, estado, fecha_emision (YYYY-MM-DD)",                       "INSCRT[\"1\",\"85\",\"aprobado\",\"2025-08-15\"]"});
        LISTA.add(new String[]{"25", "MODCRT", "Modificar certificacion", "id, nota, estado, fecha_emision (YYYY-MM-DD)",                                   "MODCRT[\"1\",\"90\",\"aprobado\",\"2025-08-20\"]"});
        LISTA.add(new String[]{"26", "DELCRT", "Eliminar certificacion",  "id",                                                                             "DELCRT[\"1\"]"});
        // Pago (simulado — QR API pendiente)
        LISTA.add(new String[]{"27", "LISPAG", "Listar pagos",         "*",                                                           "LISPAG[\"*\"]"});
        LISTA.add(new String[]{"28", "INSPAG", "Registrar pago",       "fecha (YYYY-MM-DD), id_usuario, id_inscripcion, id_metodo (1=Efectivo, 2=QR)", "INSPAG[\"2026-06-14\",\"1\",\"1\",\"2\"]"});
        //LISTA.add(new String[]{"32", "MODPAG", "Modificar pago",       "id, fecha (YYYY-MM-DD), monto",                               "MODPAG[\"1\",\"2025-08-02\",\"550\"]"});
        //LISTA.add(new String[]{"33", "DELPAG", "Eliminar pago",        "id",                                                          "DELPAG[\"1\"]"});
        // Reportes y Estadísticas (solo lectura)
        LISTA.add(new String[]{"29", "REPUSU", "Reporte usuarios por rol/estado",         "*", "REPUSU[\"*\"]"});
        LISTA.add(new String[]{"30", "REPVEH", "Reporte vehículos por tipo/estado",       "*", "REPVEH[\"*\"]"});
        LISTA.add(new String[]{"31", "REPINS", "Reporte inscripciones por tipo de curso", "*", "REPINS[\"*\"]"});
        LISTA.add(new String[]{"32", "REPPAG", "Reporte pagos por método",                "*", "REPPAG[\"*\"]"});
        // Ayuda
        LISTA.add(new String[]{"33", "AYUDA",   "Ver lista de comandos",         "-", "AYUDA"});
        LISTA.add(new String[]{"34", "AYUDA1",  "Ver flujo principal de negocio", "-", "AYUDA1"});
    }

    public static ArrayList<String[]> listar() {
        return new ArrayList<>(LISTA);
    }

    // ── Flujo principal para exposición ──────────────────────────────────────

    public static final String[] HEADERS_FLUJO = {"PASO", "COMANDO", "QUÉ HACE", "EJEMPLO"};

    private static final ArrayList<String[]> FLUJO = new ArrayList<>();

    static {
        FLUJO.add(new String[]{"1",  "AYUDA",
            "Ver todos los comandos disponibles",
            "AYUDA"});
        FLUJO.add(new String[]{"2",  "INSUSU",
            "Registrar al nuevo estudiante",
            "INSUSU[\"Maria\",\"Gonzalez\",\"2000-03-20\",\"F\",\"87654321\",\"estudiante@mail.com\",\"78000001\",\"Av. Beni 123\",\"pass123\",\"4\"]"});
        FLUJO.add(new String[]{"3",  "LISUSU",
            "Verificar que el estudiante fue registrado",
            "LISUSU[\"*\"]"});
        FLUJO.add(new String[]{"4",  "LISTCU",
            "Ver tipos de curso disponibles",
            "LISTCU[\"*\"]"});
        FLUJO.add(new String[]{"5",  "LISFRA",
            "Ver franjas horarias disponibles",
            "LISFRA[\"*\"]"});
        FLUJO.add(new String[]{"6",  "LISCUR",
            "Ver cursos abiertos (con instructor y vehículo)",
            "LISCUR[\"*\"]"});
        FLUJO.add(new String[]{"7",  "LISPLN",
            "Ver planes de pago (contado, 2 cuotas, 3 cuotas...)",
            "LISPLN[\"*\"]"});
        FLUJO.add(new String[]{"8",  "INSINC",
            "Inscribir al estudiante en el curso elegido",
            "INSINC[\"2026-06-17\",\"activa\",\"5\",\"2\",\"1\"]"});
        FLUJO.add(new String[]{"9",  "INSPAG",
            "Registrar pago en EFECTIVO (id_metodo=1)",
            "INSPAG[\"2026-06-17\",\"5\",\"1\",\"1\"]"});
        FLUJO.add(new String[]{"10", "INSPAG",
            "Registrar pago con QR PagoFacil (id_metodo=2) — recibe imagen QR por correo",
            "INSPAG[\"2026-06-17\",\"5\",\"1\",\"2\"]"});
        FLUJO.add(new String[]{"11", "LISPAG",
            "Listar pagos — verificar cuotas registradas",
            "LISPAG[\"*\"]"});
        FLUJO.add(new String[]{"12", "INSCRT",
            "Registrar nota/estado → sistema genera certificado PDF con membrete y lo envía por correo",
            "INSCRT[\"1\",\"85\",\"aprobado\",\"2026-06-17\"]"});
        FLUJO.add(new String[]{"13", "LISCRT",
            "Listar certificaciones emitidas",
            "LISCRT[\"*\"]"});
    }

    public static ArrayList<String[]> listarFlujo() {
        return new ArrayList<>(FLUJO);
    }
}
