-- imcDb.sql
-- Manejador: MySQL 8.0 (MySQL Workbench 8.0)
--     mysql -u root -p < imcDb.sql

SET NAMES utf8mb4;

-- 1. DDL: base de datos, tablas y vista
DROP DATABASE IF EXISTS imcDb;
CREATE DATABASE imcDb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE imcDb;

-- Catálogo de clasificación del IMC (OMS, adultos)
CREATE TABLE clasificacionImc (
    idClasificacion TINYINT       NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(30)   NOT NULL,
    imcMinimo       DECIMAL(5,2)  NOT NULL,
    imcMaximo       DECIMAL(5,2)  NOT NULL,
    CONSTRAINT pkClasificacionImc PRIMARY KEY (idClasificacion),
    CONSTRAINT ukClasificacionImcNombre UNIQUE (nombre),
    CONSTRAINT ckClasificacionImcRango CHECK (imcMinimo < imcMaximo)
) ENGINE = InnoDB;

-- Usuarios registrados (RF_0001 a RF_0004)
CREATE TABLE usuario (
    idUsuario       INT           NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(50)   NOT NULL,
    apellidoPaterno VARCHAR(50)   NOT NULL,
    apellidoMaterno VARCHAR(50)   NULL,
    nombreUsuario   VARCHAR(30)   NOT NULL,
    contrasenaHash  VARCHAR(255)  NOT NULL,
    edad            TINYINT       NOT NULL,
    sexo            CHAR(1)       NOT NULL,
    estatura        DECIMAL(3,2)  NOT NULL,
    fechaRegistro   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pkUsuario PRIMARY KEY (idUsuario),
    CONSTRAINT ukUsuarioNombreUsuario UNIQUE (nombreUsuario),
    CONSTRAINT ckUsuarioEdad CHECK (edad BETWEEN 15 AND 120),
    CONSTRAINT ckUsuarioSexo CHECK (sexo IN ('M', 'F')),
    CONSTRAINT ckUsuarioEstatura CHECK (estatura BETWEEN 1.00 AND 2.50)
) ENGINE = InnoDB;

-- Mediciones de peso e IMC con su fecha (RF_0007 a RF_0009)
CREATE TABLE medicion (
    idMedicion      INT           NOT NULL AUTO_INCREMENT,
    idUsuario       INT           NOT NULL,
    peso            DECIMAL(5,2)  NOT NULL,
    imc             DECIMAL(5,2)  NOT NULL,
    fechaMedicion   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pkMedicion PRIMARY KEY (idMedicion),
    CONSTRAINT fkMedicionUsuario FOREIGN KEY (idUsuario)
        REFERENCES usuario (idUsuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT ckMedicionPeso CHECK (peso > 0),
    CONSTRAINT ckMedicionImc CHECK (imc > 0),
    INDEX idxMedicionUsuarioFecha (idUsuario, fechaMedicion)
) ENGINE = InnoDB;

-- Vista de apoyo para consultar el historial con su clasificación
CREATE OR REPLACE VIEW vistaHistorialImc AS
SELECT m.idMedicion,
       m.idUsuario,
       u.nombreUsuario,
       m.fechaMedicion,
       m.peso,
       m.imc,
       c.nombre AS clasificacion
FROM medicion AS m
INNER JOIN usuario AS u
        ON u.idUsuario = m.idUsuario
INNER JOIN clasificacionImc AS c
        ON m.imc BETWEEN c.imcMinimo AND c.imcMaximo;

-- 2. DML: catálogo y datos de prueba
INSERT INTO clasificacionImc (nombre, imcMinimo, imcMaximo) VALUES
    ('Bajo peso',          0.00,  18.49),
    ('Peso normal',       18.50,  24.99),
    ('Sobrepeso',         25.00,  29.99),
    ('Obesidad grado I',  30.00,  34.99),
    ('Obesidad grado II', 35.00,  39.99),
    ('Obesidad grado III',40.00, 999.99);

-- Contraseña de los tres usuarios de prueba: Imc2026!
-- Hash PBKDF2WithHmacSHA256, 210 000 iteraciones, sal de 16 bytes (RNF_0004).
INSERT INTO usuario
    (nombre, apellidoPaterno, apellidoMaterno, nombreUsuario, contrasenaHash, edad, sexo, estatura, fechaRegistro)
VALUES
    ('Juan',  'Pérez',    'López', 'jperez',
     'pbkdf2$210000$mcKzOKjiC+ZQDXbBGu+OVg==$TvnTqGq7CdlAUb6CtwxR20BiZT9UV1orhHr/M/wrvr0=', 28, 'M', 1.75, '2026-08-31 20:00:00'),
    ('María', 'González', 'Ruiz',  'mgonzalez',
     'pbkdf2$210000$GviWxS5KniiBnofIQ+zbBw==$O2ZASuxkttgoxx0rfw0nhdS/nj2Zpu67J/jeB/B9SNc=', 34, 'F', 1.62, '2026-09-14 18:30:00'),
    ('Ana',   'Torres',   NULL,    'atorres',
     'pbkdf2$210000$UU3Hq0Ptvt/uDKmnU7iWlg==$c4A0DumlBl9LASp9K+1e90FmHz92biBbnVvio0UeG/w=', 19, 'F', 1.58, '2026-10-15 09:00:00');

-- IMC = peso / estatura^2 redondeado a dos decimales (CalculadoraImc)
INSERT INTO medicion (idUsuario, peso, imc, fechaMedicion) VALUES
    (1, 82.00, 26.78, '2026-09-01 08:30:00'),
    (1, 79.40, 25.93, '2026-09-15 08:15:00'),
    (1, 76.50, 24.98, '2026-10-01 08:20:00'),
    (2, 58.00, 22.10, '2026-09-15 19:05:00'),
    (2, 59.30, 22.60, '2026-10-10 18:40:00');


DROP USER IF EXISTS 'imcApp'@'localhost';
DROP USER IF EXISTS 'imcApp'@'127.0.0.1';
CREATE USER 'imcApp'@'localhost' IDENTIFIED BY 'ImcApp2026!';
CREATE USER 'imcApp'@'127.0.0.1' IDENTIFIED BY 'ImcApp2026!';
GRANT SELECT, INSERT, UPDATE, DELETE ON imcDb.* TO 'imcApp'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON imcDb.* TO 'imcApp'@'127.0.0.1';
FLUSH PRIVILEGES;

SELECT 'clasificacionImc' AS tabla, COUNT(*) AS registros FROM clasificacionImc
UNION ALL SELECT 'usuario',  COUNT(*) FROM usuario
UNION ALL SELECT 'medicion', COUNT(*) FROM medicion;

-- Historial de jperez con su clasificación (consulta de MedicionRepository)
SELECT m.idMedicion, m.idUsuario, m.peso, m.imc, m.fechaMedicion,
       c.idClasificacion, c.nombre, c.imcMinimo, c.imcMaximo
FROM medicion AS m
INNER JOIN clasificacionImc AS c
        ON m.imc BETWEEN c.imcMinimo AND c.imcMaximo
WHERE m.idUsuario = 1
ORDER BY m.fechaMedicion DESC, m.idMedicion DESC;


