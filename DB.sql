DROP DATABASE IF EXISTS softlutionANDro;
CREATE DATABASE softlutionANDro;
USE softlutionANDro;

CREATE TABLE programas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estadoAprobacion VARCHAR(50),
    categoria VARCHAR(50),
    nombre VARCHAR(50),
	fechaYhora DATETIME,
);

CREATE TABLE segmentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estadoAprobacion VARCHAR(50),
    duracion FLOAT,
    titulo VARCHAR(50),
    idPrograma INT,
    FOREIGN KEY (idPrograma) REFERENCES programas(id)
);

CREATE TABLE emisiones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    enVivo BOOLEAN,
    plataforma VARCHAR(50),
    idPrograma INT,
    FOREIGN KEY (idPrograma) REFERENCES programas(id)
);

CREATE TABLE rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    departamento VARCHAR(50),
    permisos VARCHAR(50)
);

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50),
    nombre VARCHAR(50),
    contrasenia VARCHAR(50),
    idRol INT,
    FOREIGN KEY (idRol) REFERENCES rol(id)
);

CREATE TABLE contenidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    formato VARCHAR(50),
    rutaArchivo VARCHAR(500),
    tags VARCHAR(500),
    idUsuario INT,
    FOREIGN KEY (idUsuario) REFERENCES usuario(id)
);

CREATE TABLE audiencia_con (
    id INT AUTO_INCREMENT PRIMARY KEY,
    likeOdislike BOOLEAN,
    idUsuario INT,
    idContenido INT,
    FOREIGN KEY (idUsuario) REFERENCES usuario(id),
    FOREIGN KEY (idContenido) REFERENCES contenidos(id)
);

CREATE TABLE encuesta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    preguntar VARCHAR(50),
    idUsuario INT,
    FOREIGN KEY (idUsuario) REFERENCES usuario(id)
);

CREATE TABLE opcion_e (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idEncuesta INT,
    opcion VARCHAR(50),
    FOREIGN KEY (idEncuesta) REFERENCES encuesta(id)
);

CREATE TABLE votar_o (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idOpcion INT,
    idUsuario INT,
    FOREIGN KEY (idUsuario) REFERENCES usuario(id),
    FOREIGN KEY (idOpcion) REFERENCES opcion_e(id)
);

CREATE TABLE errores{
	tipo varchar(50),
	mensaje varchar(50),
	foreign key (idContenido) references contenidos(id)
}

DELIMITER // 
 CREATE PROCEDURE cpr(IN categoria1 varchar(50), IN nombre1 varchar(50), IN fechaYhora1 DATETIME, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET p_mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 INSERT INTO programas(estadoAprobacion, categoria, nombre, fechaYhora) VALUES('En Revisión', categoria1, nombre1, fechaYhora)
	COMMIT;
   SET mensaje = 'Programa ingresado con éxito.' 
 END;
   
 CREATE PROCEDURE bpr(IN id1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET p_mensaje = 'Ocurrio un error.';
	 END;
    START TRANSACTION;
	 DELETE FROM progamas where id = id1;
	COMMIT;
   SET mensaje = 'Programa borrado con éxito.' 
 END;
 CREATE PROCEDURE mpr(IN id1 int, IN estadoAprobacion1 varchar(50), IN categoria1 varchar(50), IN nombre1 varchar(50), IN fechaYhora1 DATETIME, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET p_mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 UPDATE programas
	 SET estadoAprobacion = estadoAprobacion1, categoria = categoria1, nombre = nombre1, fechaYhora = fechaYhora1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Datos del programa actualizados con éxito.'
 END // 
 DELIMITER ;
