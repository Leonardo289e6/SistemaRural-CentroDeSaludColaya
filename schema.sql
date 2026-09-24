-- ============================================================
-- SistemaRural-PE - Esquema de base de datos
-- Centro de Salud Rural Santa Rosa
-- ============================================================

CREATE TABLE IF NOT EXISTS pacientes (
    dni               VARCHAR(8) PRIMARY KEY,
    nombres           VARCHAR(100) NOT NULL,
    apellidos         VARCHAR(100) NOT NULL,
    fecha_nacimiento  DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS historias_clinicas (
    id               BIGSERIAL PRIMARY KEY,
    paciente_dni     VARCHAR(8) NOT NULL REFERENCES pacientes(dni),
    fecha_atencion   DATE NOT NULL,
    diagnostico      VARCHAR(255) NOT NULL,
    tratamiento      VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS personal_medico (
    id             BIGSERIAL PRIMARY KEY,
    usuario        VARCHAR(50) UNIQUE NOT NULL,
    password_hash  VARCHAR(64) NOT NULL,   -- SHA-256 en hexadecimal (64 caracteres)
    nombres        VARCHAR(100) NOT NULL,
    rol            VARCHAR(50) NOT NULL
);

-- Usuario de prueba: usuario "mquispe" / contraseña "santarosa2026"
-- El hash se genera con SHA-256 (ver security.GestorSeguridad.generarHash)
INSERT INTO personal_medico (usuario, password_hash, nombres, rol)
VALUES ('mquispe', '4c7f5716652cd23c2f703018ebfffa92d9e632101f664c5d191da3369de2c9fd', 'Marisol Quispe', 'Licenciada en Enfermería')
ON CONFLICT (usuario) DO NOTHING;
