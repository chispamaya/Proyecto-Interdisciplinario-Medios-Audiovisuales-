DROP DATABASE IF EXISTS softlutionANDro;
CREATE DATABASE softlutionANDro;
USE softlutionANDro;


CREATE TABLE segmentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estadoAprobacion VARCHAR(50),
    duracion FLOAT,
    titulo VARCHAR(50),
    idPrograma INT,
    FOREIGN KEY (idPrograma) REFERENCES programas(id)
);

CREATE TABLE plataforma(
	id INT AUTO_INCREMENT PRIMARY KEY,
	nombre varchar(50),
	tipo varchar(50)
);

CREATE TABLE programas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estadoAprobacion VARCHAR(50),
    categoria VARCHAR(50),
    nombre VARCHAR(50),
	fechaYhora DATETIME,
	idPlataforma int,
	foreign key(idPlataforma) references plataforma(id)
);

CREATE TABLE emisiones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    enVivo BOOLEAN,
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

CREATE TABLE errores(
	id int AUTO_INCREMENT PRIMARY KEY,
	tipo varchar(50),
	mensaje varchar(500),
	idContenido int,
	foreign key(idContenido) references contenidos(id)
);

CREATE TABLE auditoria(
	id int AUTO_INCREMENT PRIMARY KEY,
	accion varchar(50),
	usuario_id int,
	tablaM varchar(50),
	fecha datetime,
	foreign key (usuario_id) references usuario(id)
);


DELIMITER // 
CREATE PROCEDURE s(IN tabla varchar(50), IN idU int, IN idP int, OUT mensaje varchar(50))
 BEGIN
	DECLARE ct LONGTEXT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  SET mensaje = 'Ocurrio un error.';
	 END;
	 IF idU IS NOT NULL THEN
		SELECT * FROM usuario WHERE id = idU;
	 ELSEIF idP IS NOT NULL THEN
		SELECT * FROM programas WHERE id = idP;
	 ELSE
		SET ct = CONCAT('SELECT * FROM ', tabla, ';');
		PREPARE c FROM ct;
		EXECUTE c;
		DEALLOCATE PREPARE c;
	 END IF;
   SET mensaje = 'Mostrando datos.' 
 END;

 CREATE PROCEDURE cpr(IN categoria1 varchar(50), IN nombre1 varchar(50), IN fechaYhora1 DATETIME, IN idP1 INT, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 INSERT INTO programas(estadoAprobacion, categoria, nombre, fechaYhora, idPlataforma) 
	 VALUES('En Revisión', categoria1, nombre1, fechaYhora1, idP1)
	COMMIT;
   SET mensaje = 'Programa ingresado con éxito.' 
 END;
   
 CREATE PROCEDURE bpr(IN id1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
    START TRANSACTION;
	 DELETE FROM programas where id = id1;
	COMMIT;
   SET mensaje = 'Programa borrado con éxito.' 
 END;
 
 CREATE PROCEDURE mpr(IN id1 int, IN estadoAprobacion1 varchar(50), IN categoria1 varchar(50), IN nombre1 varchar(50), IN fechaYhora1 DATETIME, IN idP1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 UPDATE programas
	 SET estadoAprobacion = estadoAprobacion1, categoria = categoria1, nombre = nombre1, fechaYhora = fechaYhora1, idPlataforma = idP1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Datos del programa actualizados con éxito.'
 END;
 
 

 CREATE PROCEDURE cu(IN email1 varchar(50), IN nombre1 varchar(50), IN contrasenia1 varchar(50), IN idRol1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 INSERT INTO usuario(email, nombre, contrasenia, idRol) VALUES(email1, nombre1, contrasenia1, idRol1)
	COMMIT;
   SET mensaje = 'Usuario ingresado con éxito.' 
 END;
 
 CREATE PROCEDURE bu(IN id1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
    START TRANSACTION;
	 DELETE FROM usuario where id = id1;
	COMMIT;
   SET mensaje = 'Usuario borrado con éxito.' 
 END;
 
 CREATE PROCEDURE mu(IN id1 int, IN idRol1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 UPDATE usuario
	 SET idRol = idRol1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Rol del usuario actualizado con éxito.'
 END;
 
 CREATE PROCEDURE cs(IN estadoAprobacion1 varchar(50), IN duracion1 varchar(50), IN titulo1 varchar(50), IN idP1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 INSERT INTO segmentos(estadoAprobacion, duracion, titulo, idPrograma) VALUES(estadoAprobacion1, duracion1, titulo1, idP1)
	COMMIT;
   SET mensaje = 'Segmento ingresado con éxito.' 
 END;
 
 CREATE PROCEDURE bs(IN id1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
    START TRANSACTION;
	 DELETE FROM segmentos where id = id1;
	COMMIT;
   SET mensaje = 'Segmento borrado con éxito.' 
 END;
 CREATE PROCEDURE ms(IN id1 int, IN estadoAprobacion1 varchar(50), IN duracion1 varchar(50), IN titulo1 varchar(50), IN idP1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 UPDATE segmentos
	 SET estadoAprobacion = estadoAprobacion1, duracion = duracion1, titulo = titulo1, idPrograma = idP1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Segmento actualizado con éxito.'
 END;
 
 
 
 CREATE PROCEDURE cpl(IN nombre1 varchar(50), IN tipo1 varchar(50), OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 INSERT INTO plataforma(nombre, tipo) VALUES(nombre1, tipo1)
	COMMIT;
   SET mensaje = 'Plataforma ingresada con éxito.' 
 END;
   
 CREATE PROCEDURE bpl(IN id1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
    START TRANSACTION;
	 DELETE FROM plataforma where id = id1;
	COMMIT;
   SET mensaje = 'Plataforma borrada con éxito.' 
 END;
 
 CREATE PROCEDURE mpl(IN id1 int, IN nombre1 varchar(50), IN tipo1 varchar(50), OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 UPDATE plataforma
	 SET nombre = nombre1, tipo = tipo1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Datos de la plataforma actualizados con éxito.'
 END;
 
 

 
 CREATE PROCEDURE me(IN id1 int, IN enVivo1 varchar(50), IN idPr1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 UPDATE emision
	 SET enVivo = enVivo1, idPrograma = idPr1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Datos de la emisión actualizados con éxito.'
 END; 
 
 
 
 
 CREATE PROCEDURE cc(IN formato1 varchar(50), IN rutaArchivo1 varchar(500), IN tags1 varchar(500), IN idU1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 INSERT INTO contenidos(formato,rutaArchivo,tags,idUsuario) VALUES(formato1,rutaArchivo1,tags1,idU1)
	COMMIT;
   SET mensaje = 'Contenido subido con éxito.'
 END; 
 
 CREATE PROCEDURE bc(IN id1 int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 DELETE FROM contenidos
	 WHERE id = id1
	COMMIT;
   SET mensaje = 'Contenido borrado con éxito.'
 END; 
 
 
 
 
 CREATE PROCEDURE ld(IN likeOdislike1 boolean, IN idC int, IN idU int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 IF NOT EXISTS (SELECT * FROM audiencia_con WHERE idUsuario = idU AND idContenido = idC) THEN
	  INSERT INTO audiencia_con(likeOdislike, idContenido, idUsuario) VALUES (likeOdislike1, idC, idU)
	 ELSE
	  UPDATE audiencia_con
	   SET likeOdislike = likeOdislike1
	   WHERE idUsuario = idU AND idContenido = idC;
	 END IF;
	COMMIT;
   SET mensaje = 'Valoración actualizada con éxito.'
 END;
 
 CREATE PROCEDURE bv(IN idC int, IN idU int, OUT mensaje varchar(50))
 BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 DELETE FROM audiencia_con
	 WHERE idContenido = idC AND idUsuario = idU;
	COMMIT;
   SET mensaje = 'Valoración actualizada con éxito.'
 END;
 
 
 
 
 CREATE PROCEDURE ce(IN preguntar1 varchar(50), IN idU int, IN opciones JSON, OUT mensaje varchar(50))
 BEGIN
    DECLARE idE int;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 INSERT INTO encuesta(preguntar, idUsuario) VALUES (preguntar1, idU);
	 SET idE = LAST_INSERT_ID();
		INSERT INTO opcion_e (idEncuesta, opcion)
     SELECT 
        idE, opcion_valor
     FROM 
        JSON_TABLE(
            opciones,
            '$[*]' COLUMNS (
                opcion_valor VARCHAR(50) PATH '$' 
            )
        ) AS jt;
	 COMMIT;
   SET mensaje = 'Encuesta creada con éxito.'
 END;
 
 CREATE PROCEDURE vo(IN idO int, IN idU int, IN idE int, OUT mensaje varchar(50))
 BEGIN
	DECLARE idOAntigua int;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	 END;
	START TRANSACTION;
	 IF NOT EXISTS(SELECT * FROM votar_opcion WHERE idUsuario = idU AND idOpcion IN(SELECT id FROM opcion_e WHERE idEncuesta = idE)) THEN
      INSERT INTO votar_opcion(idOpcion, idUsuario) VALUES(idO, idU);
	  SET mensaje = 'Voto ingresado con éxito.'
     ELSE
	  SELECT idOpcion INTO idOAntigua FROM votar_opcion WHERE idOpcion IN(SELECT id FROM opcion_e WHERE idEncuesta = idE) AND idUsuario = idU;
	  IF idOAntigua = idO THEN
		DELETE FROM votar_opcion 
		WHERE idUsuario = idU AND idOpcion = idO;
		SET mensaje = 'Voto eliminado con éxito.'
	  ELSE
		UPDATE votar_opcion
		SET idOpcion = idO WHERE idUsuario = idU AND idOpcion = idOAntigua;
		SET mensaje = 'Voto actualizado con éxito.'
     END IF;	  
  COMMIT;
 END //

 
 
 
 
 
 
 

 



