-- =====================================================
-- SCRIPT DE BASE DE DATOS PARA POSTGRESQL
-- Sistema de Gestión por Email - ParcialEmail
-- Base de datos: db_grupo13sa
-- =====================================================

-- =====================================================
-- ELIMINAR TABLAS SI EXISTEN (OPCIONAL - SOLO PARA REINICIAR)
-- =====================================================
DROP TABLE IF EXISTS Rol CASCADE;
DROP TABLE IF EXISTS MetodoPago CASCADE;
DROP TABLE IF EXISTS PlanPago CASCADE;
DROP TABLE IF EXISTS FranjaHoraria CASCADE;
DROP TABLE IF EXISTS TipoVehiculo CASCADE;
DROP TABLE IF EXISTS Usuario CASCADE;
DROP TABLE IF EXISTS TipoCurso CASCADE;
DROP TABLE IF EXISTS Vehiculo CASCADE;
DROP TABLE IF EXISTS Curso CASCADE;
DROP TABLE IF EXISTS Inscripcion CASCADE;
DROP TABLE IF EXISTS Pago CASCADE;
DROP TABLE IF EXISTS ControlCertificacion CASCADE;

CREATE TABLE Rol (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(150),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: (MetodoPago)
CREATE TABLE MetodoPago (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(150),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: (PlanPago)
CREATE TABLE PlanPago (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    numero_cuotas INT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: (FranjaHoraria)
CREATE TABLE FranjaHoraria (
    id SERIAL PRIMARY KEY,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: (TipoVehiculo)
CREATE TABLE TipoVehiculo (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(150),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: (Usuario)
CREATE TABLE Usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(1),
    nro_documento VARCHAR(20) UNIQUE NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    estado_usuario VARCHAR(20) DEFAULT 'activo',
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rol_id INTEGER NOT NULL REFERENCES Rol (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Tabla: (TipoCurso)
CREATE TABLE TipoCurso (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio FLOAT NOT NULL,
    estado_curso VARCHAR(20) NOT NULL,
    duracion_horas INT NOT NULL,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_vehiculo_id INTEGER NOT NULL REFERENCES TipoVehiculo (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Tabla: (Vehiculo)
CREATE TABLE Vehiculo (
    id SERIAL PRIMARY KEY,
    placa VARCHAR(20) UNIQUE NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    estado_vehiculo VARCHAR(20) NOT NULL,
    fecha_mantenimiento DATE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_vehiculo_id INTEGER NOT NULL REFERENCES TipoVehiculo (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Tabla: (Curso)
-- estado_curso: 'disponible' | 'reservado' | 'inscrito' | 'cancelado'
-- Ciclo: disponible → reservado → inscrito → disponible (al emitir certificado)
CREATE TABLE Curso (
    id SERIAL PRIMARY KEY,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    precio_final FLOAT NOT NULL,
    estado_curso VARCHAR(20) NOT NULL DEFAULT 'disponible',
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    instructor_id INTEGER NOT NULL REFERENCES Usuario (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    vehiculo_id INTEGER NOT NULL REFERENCES Vehiculo (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    tipo_curso_id INTEGER NOT NULL REFERENCES TipoCurso (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    franja_horaria_id INTEGER NOT NULL REFERENCES FranjaHoraria (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    reservado_por INTEGER REFERENCES Usuario (id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Tabla: (Inscripcion)
CREATE TABLE Inscripcion (
    id SERIAL PRIMARY KEY,
    fecha_inscripcion DATE NOT NULL,
    estado_inscripcion VARCHAR(20) NOT NULL,
    monto_total FLOAT NOT NULL,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estudiante_id INTEGER NOT NULL REFERENCES Usuario (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    plan_pago_id INTEGER NOT NULL REFERENCES PlanPago (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    curso_id INTEGER NOT NULL REFERENCES Curso (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Tabla: (Pago)
CREATE TABLE Pago (
    id SERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    monto FLOAT NOT NULL,
    nro_cuota INTEGER NOT NULL DEFAULT 1,
    id_transaccion VARCHAR(100),
    nro_pedido VARCHAR(50),
    estado_pago VARCHAR(20) NOT NULL DEFAULT 'pendiente',
    correo_notificacion VARCHAR(150),
    notificado BOOLEAN DEFAULT FALSE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INTEGER NOT NULL REFERENCES Usuario (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    metodo_id INTEGER NOT NULL REFERENCES MetodoPago (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    inscripcion_id INTEGER NOT NULL REFERENCES Inscripcion (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Tabla: (ControlCertificacion)
CREATE TABLE ControlCertificacion (
    id SERIAL PRIMARY KEY,
    nota FLOAT NOT NULL,
    estado_certificacion VARCHAR(50) NOT NULL,
    fecha_emision DATE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    inscripcion_id INTEGER NOT NULL REFERENCES Inscripcion (id) ON DELETE RESTRICT ON UPDATE CASCADE
); 


-- =====================================================
-- INSERTAR DATOS DE EJEMPLO - COMANDOS
-- =====================================================
-- Verificar que las tablas se crearon correctamente
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;
