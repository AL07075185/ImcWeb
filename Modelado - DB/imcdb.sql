DROP DATABASE IF EXISTS imcDb;
CREATE DATABASE imcDb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE imcDb;
 
CREATE TABLE clasificacionImc (
    idClasificacion TINYINT       NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(30)   NOT NULL,
    imcMinimo       DECIMAL(5,2)  NOT NULL,
    imcMaximo       DECIMAL(5,2)  NOT NULL,
    CONSTRAINT pkClasificacionImc PRIMARY KEY (idClasificacion),
    CONSTRAINT ukClasificacionImcNombre UNIQUE (nombre),
    CONSTRAINT ckClasificacionImcRango CHECK (imcMinimo < imcMaximo)
) ENGINE = InnoDB;
 
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
 
CREATE OR REPLACE VIEW vistaHistorialImc AS
SELECT m.idMedicion, m.idUsuario, u.nombreUsuario, m.fechaMedicion,
       m.peso, m.imc, c.nombre AS clasificacion
FROM medicion AS m
INNER JOIN usuario AS u ON u.idUsuario = m.idUsuario
INNER JOIN clasificacionImc AS c ON m.imc BETWEEN c.imcMinimo AND c.imcMaximo;
