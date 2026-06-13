package negociodato.utils;

import java.util.ArrayList;

public class Comandos {

    public static final String[] HEADERS = {"ID", "COMANDO", "DESCRIPCION", "PARAMETROS", "EJEMPLO"};

    private static final ArrayList<String[]> LISTA = new ArrayList<>();

    static {                                              
        LISTA.add(new String[]{"1", "LISROL", "Listar roles",          "*",                          "LISROL[\"*\"]"});
        LISTA.add(new String[]{"2", "INSROL", "Insertar rol",          "nombre, descripcion",        "INSROL[\"Instructor\",\"Docente de manejo\"]"});
        LISTA.add(new String[]{"3", "MODROL", "Modificar rol",         "id, nombre, descripcion",    "MODROL[\"1\",\"Instructor\",\"Docente de manejo\"]"});
        LISTA.add(new String[]{"4", "DELROL", "Eliminar rol",          "id",                         "DELROL[\"1\"]"});
        // MetodoPago
        LISTA.add(new String[]{"5", "LISMET", "Listar métodos de pago", "*",                         "LISMET[\"*\"]"});
        LISTA.add(new String[]{"6", "INSMET", "Insertar método pago",   "nombre, descripcion",       "INSMET[\"Efectivo\",\"Pago en caja\"]"});
        LISTA.add(new String[]{"7", "MODMET", "Modificar método pago",  "id, nombre, descripcion",   "MODMET[\"1\",\"Efectivo\",\"Pago en caja\"]"});
        LISTA.add(new String[]{"8", "DELMET", "Eliminar método pago",   "id",                        "DELMET[\"1\"]"});
        // PlanPago
        LISTA.add(new String[]{"9", "LISPLN", "Listar planes de pago",  "*",                         "LISPLN[\"*\"]"});
        LISTA.add(new String[]{"10", "INSPLN", "Insertar plan de pago",  "nombre, nro_cuotas, estado","INSPLN[\"Contado\",\"1\",\"activo\"]"});
        LISTA.add(new String[]{"11", "MODPLN", "Modificar plan de pago", "id, nombre, nro_cuotas, estado","MODPLN[\"1\",\"Contado\",\"1\",\"activo\"]"});
        LISTA.add(new String[]{"12", "DELPLN", "Eliminar plan de pago",  "id",                        "DELPLN[\"1\"]"});
        // FranjaHoraria
        LISTA.add(new String[]{"13", "LISFRA", "Listar franjas horarias","*",                         "LISFRA[\"*\"]"});
        LISTA.add(new String[]{"14", "INSFRA", "Insertar franja horaria","hora_inicio, hora_fin",     "INSFRA[\"08:00\",\"10:00\"]"});
        LISTA.add(new String[]{"15", "MODFRA", "Modificar franja horaria","id, hora_inicio, hora_fin","MODFRA[\"1\",\"08:00\",\"10:00\"]"});
        LISTA.add(new String[]{"16", "DELFRA", "Eliminar franja horaria","id",                        "DELFRA[\"1\"]"});
        // TipoVehiculo
        LISTA.add(new String[]{"17", "LISTYV", "Listar tipos de vehículo","*",                        "LISTYV[\"*\"]"});
        LISTA.add(new String[]{"18", "INSTYV", "Insertar tipo vehículo", "nombre, descripcion",       "INSTYV[\"Automovil\",\"Vehículo de 4 ruedas\"]"});
        LISTA.add(new String[]{"19", "MODTYV", "Modificar tipo vehículo","id, nombre, descripcion",   "MODTYV[\"1\",\"Automovil\",\"Vehículo de 4 ruedas\"]"});
        LISTA.add(new String[]{"20", "DELTYV", "Eliminar tipo vehículo", "id",                        "DELTYV[\"1\"]"});
        // TipoCurso
        LISTA.add(new String[]{"21", "LISTCU", "Listar tipos de curso",  "*",                         "LISTCU[\"*\"]"});
        LISTA.add(new String[]{"22", "INSTCU", "Insertar tipo de curso", "nombre, descripcion, precio, estado, duracion_horas, nombre_tipo_vehiculo","INSTCU[\"Basico\",\"Curso inicial\",\"500\",\"activo\",\"20\",\"Automovil\"]"});
        LISTA.add(new String[]{"23", "MODTCU", "Modificar tipo de curso","id, nombre, descripcion, precio, estado, duracion_horas","MODTCU[\"1\",\"Basico\",\"Curso inicial\",\"500\",\"activo\",\"20\"]"});
        LISTA.add(new String[]{"24", "DELTCU", "Eliminar tipo de curso", "id",                        "DELTCU[\"1\"]"});
        // Usuario
        LISTA.add(new String[]{"25", "LISUSU", "Listar usuarios",       "*",                                                                                        "LISUSU[\"*\"]"});
        LISTA.add(new String[]{"26", "INSUSU", "Registrar usuario",     "nombre, apellido, nro_doc, correo, telefono, direccion, pass, nombre_rol",                 "INSUSU[\"Juan\",\"Perez\",\"12345678\",\"juan@mail.com\",\"70000000\",\"Av. Principal\",\"pass123\",\"Estudiante\"]"});
        LISTA.add(new String[]{"27", "MODUSU", "Modificar usuario",     "id, nombre, apellido, nro_doc, correo, telefono, direccion, estado",                       "MODUSU[\"1\",\"Juan\",\"Perez\",\"12345678\",\"juan@mail.com\",\"70000000\",\"Av. Principal\",\"activo\"]"});
        LISTA.add(new String[]{"28", "DELUSU", "Desactivar usuario",    "id",                                                                                       "DELUSU[\"1\"]"});
        // Vehiculo
        LISTA.add(new String[]{"29", "LISVEH", "Listar vehículos",       "*",                                                                        "LISVEH[\"*\"]"});
        LISTA.add(new String[]{"30", "INSVEH", "Registrar vehículo",     "placa, marca, modelo, estado, fecha_mant (YYYY-MM-DD), nombre_tipo",       "INSVEH[\"ABC-123\",\"Toyota\",\"Corolla\",\"disponible\",\"2025-01-15\",\"Automovil\"]"});
        LISTA.add(new String[]{"31", "MODVEH", "Modificar vehículo",     "id, placa, marca, modelo, estado, fecha_mant (YYYY-MM-DD)",                "MODVEH[\"1\",\"ABC-123\",\"Toyota\",\"Corolla\",\"disponible\",\"2025-06-01\"]"});
        LISTA.add(new String[]{"32", "DELVEH", "Dar de baja vehículo",   "id",                                                                       "DELVEH[\"1\"]"});
        // Curso (instancia / reserva)
        LISTA.add(new String[]{"33", "LISCUR", "Listar cursos",          "*",                                                                                                        "LISCUR[\"*\"]"});
        LISTA.add(new String[]{"34", "INSCUR", "Crear curso",            "fecha_ini, fecha_fin, precio, estado, id_instructor, id_vehiculo, nombre_tipo_curso, id_franja",           "INSCUR[\"2025-07-01\",\"2025-07-31\",\"500\",\"abierto\",\"1\",\"1\",\"Basico\",\"1\"]"});
        LISTA.add(new String[]{"35", "MODCUR", "Modificar curso",        "id, fecha_ini, fecha_fin, precio, estado",                                                                 "MODCUR[\"1\",\"2025-07-01\",\"2025-07-31\",\"500\",\"cerrado\"]"});
        LISTA.add(new String[]{"36", "DELCUR", "Cancelar curso",         "id",                                                                                                       "DELCUR[\"1\"]"});
        // Inscripcion
        LISTA.add(new String[]{"37", "LISINC", "Listar inscripciones",   "*",                                                                          "LISINC[\"*\"]"});
        LISTA.add(new String[]{"38", "INSINC", "Inscribir estudiante",   "fecha (YYYY-MM-DD), estado, id_estudiante, id_curso, nombre_plan",           "INSINC[\"2025-07-01\",\"activa\",\"2\",\"1\",\"Contado\"]"});
        LISTA.add(new String[]{"39", "MODINC", "Actualizar inscripcion", "id, estado, aprobados (0/1)",                                               "MODINC[\"1\",\"completada\",\"1\"]"});
        LISTA.add(new String[]{"40", "DELINC", "Cancelar inscripcion",   "id",                                                                         "DELINC[\"1\"]"});
        // Control y Certificacion
        LISTA.add(new String[]{"41", "LISCRT", "Listar certificaciones",  "*",                                                                              "LISCRT[\"*\"]"});
        LISTA.add(new String[]{"42", "INSCRT", "Registrar certificacion", "id_inscripcion, nota, estado, fecha_emision (YYYY-MM-DD)",                       "INSCRT[\"1\",\"85\",\"aprobado\",\"2025-08-15\"]"});
        LISTA.add(new String[]{"43", "MODCRT", "Modificar certificacion", "id, nota, estado, fecha_emision (YYYY-MM-DD)",                                   "MODCRT[\"1\",\"90\",\"aprobado\",\"2025-08-20\"]"});
        LISTA.add(new String[]{"44", "DELCRT", "Eliminar certificacion",  "id",                                                                             "DELCRT[\"1\"]"});
        // Pago (simulado — QR API pendiente)
        LISTA.add(new String[]{"45", "LISPAG", "Listar pagos",         "*",                                                           "LISPAG[\"*\"]"});
        LISTA.add(new String[]{"46", "INSPAG", "Registrar pago",       "fecha (YYYY-MM-DD), monto, id_cajero, nombre_metodo, id_inscripcion", "INSPAG[\"2025-08-01\",\"500\",\"1\",\"Efectivo\",\"1\"]"});
        LISTA.add(new String[]{"47", "MODPAG", "Modificar pago",       "id, fecha (YYYY-MM-DD), monto",                               "MODPAG[\"1\",\"2025-08-02\",\"550\"]"});
        LISTA.add(new String[]{"48", "DELPAG", "Eliminar pago",        "id",                                                          "DELPAG[\"1\"]"});
        // Reportes y Estadísticas (solo lectura)
        LISTA.add(new String[]{"49", "REPUSU", "Reporte usuarios por rol/estado",         "*", "REPUSU[\"*\"]"});
        LISTA.add(new String[]{"50", "REPVEH", "Reporte vehículos por tipo/estado",       "*", "REPVEH[\"*\"]"});
        LISTA.add(new String[]{"51", "REPINS", "Reporte inscripciones por tipo de curso", "*", "REPINS[\"*\"]"});
        LISTA.add(new String[]{"52", "REPPAG", "Reporte pagos por método",                "*", "REPPAG[\"*\"]"});
        // Ayuda
        LISTA.add(new String[]{"53", "AYUDA",   "Ver lista de comandos", "-",                   "AYUDA"});
    }

    public static ArrayList<String[]> listar() {
        return new ArrayList<>(LISTA);
    }
}
