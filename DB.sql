DROP DATABASE IF EXISTS softlutionANDro;
CREATE DATABASE softlutionANDro;
USE softlutionANDro;


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
	horaInicio TIME,
	horaFin TIME,
	idPlataforma int,
	formatoArchivo VARCHAR(50),
	rutaArchivo VARCHAR(500),
	formatoInforme VARCHAR(50),
	rutaInforme VARCHAR(500),
	foreign key(idPlataforma) references plataforma(id)
);

CREATE TABLE dias(
    id INT AUTO_INCREMENT PRIMARY KEY,
    dia DATE, 
    idPrograma int,
	foreign key(idPrograma) references programas(id)
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
    idPrograma INT,
    FOREIGN KEY (idPrograma) REFERENCES programas(id)
);

CREATE TABLE rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50)
);

CREATE TABLE permisos(
	id INT AUTO_INCREMENT PRIMARY KEY,
	tipoPermiso varchar(50)
);

CREATE TABLE permisos_rol(
	id INT AUTO_INCREMENT PRIMARY KEY,
	idRol int,
	idPermiso int,
	FOREIGN KEY (idRol) REFERENCES rol(id),
	FOREIGN KEY (idPermiso) REFERENCES permisos(id)
);

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) UNIQUE,
    nombre VARCHAR(50),
    contrasenia VARCHAR(255),
    idRol INT,
    FOREIGN KEY (idRol) REFERENCES rol(id)
);

CREATE TABLE contenidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    formato VARCHAR(50),
    rutaArchivo VARCHAR(500),
    idUsuario INT,
    FOREIGN KEY (idUsuario) REFERENCES usuario(id)
);

CREATE TABLE tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tag VARCHAR(50)
);

CREATE TABLE contenido_tag (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idContenido int,
    idTag int,
    FOREIGN KEY (idContenido) REFERENCES contenidos(id),
	FOREIGN KEY (idTag) REFERENCES tags(id)
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
	fechaYHora DATETIME,
	idUsuario int,
	FOREIGN KEY (idUsuario) REFERENCES usuario(id)
);

CREATE TABLE auditoria(
	id int AUTO_INCREMENT PRIMARY KEY,
	accion varchar(50),
	usuario_id int,
	tablaM varchar(50),
	registro_afectado_id int,
	fecha datetime,
	foreign key (usuario_id) references usuario(id)
);


DELIMITER // 
CREATE PROCEDURE s(IN tabla VARCHAR(50), IN id1 INT, OUT mensaje VARCHAR(50))
 BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
	  SET mensaje = 'Ocurrio un error.';
	 END;
	 
	 IF tabla = 'usuario' AND id1 IS NOT NULL THEN
		SELECT * FROM usuario WHERE id = id1;
	 ELSEIF tabla = 'programas' AND id1 IS NOT NULL THEN
		SELECT * FROM programas WHERE id = id1;
	 ELSEIF tabla = 'rol' THEN
		 SELECT DISTINCT nombre FROM rol;
	 ELSEIF tabla IN('Productor/Editor', 'Programador', 'Administrador') THEN
		 SELECT tipoPermiso FROM permisos WHERE id IN
		(SELECT idPermiso FROM permisos_rol WHERE idRol IN
		(SELECT id FROM rol WHERE nombre = tabla));
	 ELSEIF tabla = 'encuesta' AND id1 IS NOT NULL THEN
	    SELECT 
	        e.id AS idEncuesta, 
	        e.preguntar, 
	        e.idUsuario AS idCreador, 
	        o.id AS idOpcion, 
	        o.opcion,
	        COUNT(v.idOpcion) AS totalVotos
	    FROM encuesta e
	    JOIN opcion_e o ON e.id = o.idEncuesta
	    LEFT JOIN votar_o v ON o.id = v.idOpcion
	    WHERE e.id = id1
	    GROUP BY e.id, e.preguntar, e.idUsuario, o.id, o.opcion;
	 ELSE
		SET @ct = CONCAT('SELECT * FROM ', tabla, ';');
		PREPARE c FROM @ct; 
		EXECUTE c;
		DEALLOCATE PREPARE c;
	 END IF;
   SET mensaje = 'Mostrando datos.'; 
 END //

 CREATE PROCEDURE cpr(IN categoria1 varchar(50), IN nombre1 varchar(50), IN horaInicio1 TIME, IN horaFin1 TIME, IN idP1 INT, IN formatoArchivo1 varchar(50), IN rutaArchivo1 varchar(500), IN formatoInforme1 varchar(50), IN rutaInforme1 varchar(500), IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO programas(estadoAprobacion, categoria, nombre, horaInicio, horaFin, idPlataforma, formatoArchivo, rutaArchivo, formatoInforme, rutaInforme) 
	 VALUES('En Revisión', categoria1, nombre1, horaInicio1, horaFin1, idP1, formatoArchivo1, rutaArchivo1, formatoInforme1, rutaInforme1);
	COMMIT;
   SET mensaje = 'Programa ingresado con éxito.' ;
 END //
   
 CREATE PROCEDURE bpr(IN id1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
    START TRANSACTION;
	 DELETE FROM programas where id = id1;
	COMMIT;
   SET mensaje = 'Programa borrado con éxito.' ;
 END //
 
 CREATE PROCEDURE mpr(IN id1 int, IN estadoAprobacion1 varchar(50), IN categoria1 varchar(50), IN nombre1 varchar(50), IN horaInicio1 TIME, IN horaFin1 TIME, IN idP1 int, IN formatoArchivo1 varchar(50), IN rutaArchivo1 varchar(500), IN formatoInforme1 varchar(50), IN rutaInforme1 varchar(500), IN idUs int, OUT mensaje varchar(50))
 BEGIN
    DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 UPDATE programas
	 SET estadoAprobacion = estadoAprobacion1, categoria = categoria1, nombre = nombre1, horaInicio = horaInicio1, horaFin = horaFin1, idPlataforma = idP1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Datos del programa actualizados con éxito.';
 END //
 
CREATE PROCEDURE cd(IN dia1 DATE, IN idP1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO dias(dia, idPrograma) 
	 VALUES(dia1, idP1);
	COMMIT;
   SET mensaje = 'Dia asignado al programa con éxito.';
 END //

CREATE PROCEDURE bd(IN id1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 DELETE FROM dias
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Dia eliminado con éxito.' ;
 END //


 CREATE PROCEDURE cu(IN email1 varchar(50), IN nombre1 varchar(50), IN contrasenia1 varchar(255), IN idRol1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO usuario(email, nombre, contrasenia, idRol) VALUES(email1, nombre1, contrasenia1, idRol1);
	COMMIT;
   SET mensaje = 'Usuario ingresado con éxito.';
 END //
 
 CREATE PROCEDURE bu(IN id1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
    START TRANSACTION;
	 DELETE FROM usuario where id = id1;
	COMMIT;
   SET mensaje = 'Usuario borrado con éxito.';
 END //
 
 CREATE PROCEDURE mu(IN id1 int, IN idRol1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 UPDATE usuario
	 SET idRol = idRol1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Rol del usuario actualizado con éxito.';
 END //
 
 CREATE PROCEDURE cs(IN estadoAprobacion1 varchar(50), IN duracion1 FLOAT, IN titulo1 varchar(50), IN idP1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO segmentos(estadoAprobacion, duracion, titulo, idPrograma) VALUES(estadoAprobacion1, duracion1, titulo1, idP1);
	COMMIT;
   SET mensaje = 'Segmento ingresado con éxito.';
 END //
 
 CREATE PROCEDURE bs(IN id1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
    START TRANSACTION;
	 DELETE FROM segmentos where id = id1;
	COMMIT;
   SET mensaje = 'Segmento borrado con éxito.' ;
 END //

 CREATE PROCEDURE ms(IN id1 int, IN estadoAprobacion1 varchar(50), IN duracion1 FLOAT, IN titulo1 varchar(50), IN idP1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 UPDATE segmentos
	 SET estadoAprobacion = estadoAprobacion1, duracion = duracion1, titulo = titulo1, idPrograma = idP1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Segmento actualizado con éxito.';
 END //
 
 
 
 CREATE PROCEDURE cpl(IN nombre1 varchar(50), IN tipo1 varchar(50), IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO plataforma(nombre, tipo) VALUES(nombre1, tipo1);
	COMMIT;
   SET mensaje = 'Plataforma ingresada con éxito.'; 
 END //
   
 CREATE PROCEDURE bpl(IN id1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
    START TRANSACTION;
	 DELETE FROM plataforma where id = id1;
	COMMIT;
   SET mensaje = 'Plataforma borrada con éxito.';
 END //
 
 CREATE PROCEDURE mpl(IN id1 int, IN nombre1 varchar(50), IN tipo1 varchar(50), IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 UPDATE plataforma
	 SET nombre = nombre1, tipo = tipo1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Datos de la plataforma actualizados con éxito.';
 END //
 
 

 
 CREATE PROCEDURE me(IN id1 int, IN enVivo1 BOOLEAN, IN idPr1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 UPDATE emisiones
	 SET enVivo = enVivo1, idPrograma = idPr1
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Datos de la emisión actualizados con éxito.';
 END //
 
 CREATE PROCEDURE cc(IN formato1 varchar(50), IN rutaArchivo1 varchar(500), IN idU1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO contenidos(formato,rutaArchivo,idUsuario) VALUES(formato1,rutaArchivo1,idU1);
	COMMIT;
   SET mensaje = 'Contenido subido con éxito.';
 END // 
 
 CREATE PROCEDURE bc(IN id1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 DELETE FROM contenidos
	 WHERE id = id1;
	COMMIT;
   SET mensaje = 'Contenido borrado con éxito.';
 END //
 
 CREATE PROCEDURE ct(IN tag1 varchar(50), IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO tags(tag) VALUES(tag1);
	COMMIT;
   SET mensaje = CONCAT('Tag ', tag1, ' creado con éxito.');
 END //

 CREATE PROCEDURE cct(IN idC1 int, IN idT1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO contenido_tag(idContenido, idTag) VALUES(idC1, idT1);
	COMMIT;
   SET mensaje = 'Tag añadido con éxito al contenido.';
 END //

 CREATE PROCEDURE bct(IN idC1 int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 DELETE FROM contenido_tag
	 WHERE idContenido = idC1;
	COMMIT;
   SET mensaje = 'Contenido-Tag eliminado con éxito.';
 END //
 
 CREATE PROCEDURE ld(IN likeOdislike1 boolean, IN idC int, IN idU int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 IF NOT EXISTS (SELECT * FROM audiencia_con WHERE idUsuario = idU AND idContenido = idC) THEN
	  INSERT INTO audiencia_con(likeOdislike, idContenido, idUsuario) VALUES (likeOdislike1, idC, idU);
	 ELSE
	  UPDATE audiencia_con
	   SET likeOdislike = likeOdislike1
	   WHERE idUsuario = idU AND idContenido = idC;
	 END IF;
	COMMIT;
   SET mensaje = 'Valoración actualizada con éxito.';
 END //
 
 CREATE PROCEDURE bv(IN idC int, IN idU int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 DELETE FROM audiencia_con
	 WHERE idContenido = idC AND idUsuario = idU;
	COMMIT;
   SET mensaje = 'Valoración actualizada con éxito.';
 END //
 
 
 
 
 CREATE PROCEDURE cen(IN preguntar1 varchar(50), IN idU int, OUT mensaje varchar(50), IN idUs int, OUT idE int)
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO encuesta(preguntar, idUsuario) VALUES (preguntar1, idU);
	 COMMIT;
   SET idE = LAST_INSERT_ID();
   SET mensaje = 'Encuesta creada con éxito.';
 END //

CREATE PROCEDURE co(IN idE int, IN opcion1 varchar(50), IN cont int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
	DECLARE error varchar(500);
	DECLARE errorC varchar(50);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	 BEGIN
		GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
	  ROLLBACK;
	  SET mensaje = 'Ocurrio un error.';
	  INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
	  VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
	 END;
	START TRANSACTION;
	 INSERT INTO opcion_e(idEncuesta, opcion) VALUES (idE, opcion1);
	 COMMIT;
   SET mensaje = CONCAT('Opcion ',cont, ' creada con éxito.');
 END //
 
CREATE PROCEDURE vo(IN idO int, IN idU int, IN idE int, IN idUs int, OUT mensaje varchar(50))
 BEGIN
    DECLARE idOAntigua int;
    DECLARE error varchar(500);
    DECLARE errorC varchar(50);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
     BEGIN
        GET DIAGNOSTICS CONDITION 1
            errorC = MYSQL_ERRNO,      
            error = MESSAGE_TEXT;
      ROLLBACK;
      SET mensaje = CONCAT("Ocurrio un error."); 
      INSERT INTO errores(tipo, mensaje, fechaYHora, idUsuario)
      VALUES(CONCAT('Error: ', errorC), error, NOW(), idUs);
     END;
    START TRANSACTION;
    IF NOT EXISTS(SELECT * FROM votar_o WHERE idUsuario = idU AND idOpcion IN(SELECT id FROM opcion_e WHERE idEncuesta = idE)) THEN 
      INSERT INTO votar_o(idOpcion, idUsuario) VALUES(idO, idU);
      SET mensaje = 'Voto ingresado con éxito.';
    ELSE
      SELECT idOpcion INTO idOAntigua 
      FROM votar_o 
      WHERE idOpcion IN(SELECT id FROM opcion_e WHERE idEncuesta = idE) AND idUsuario = idU;
      IF idOAntigua = idO THEN
        DELETE FROM votar_o
        WHERE idUsuario = idU AND idOpcion = idO;
        SET mensaje = 'Voto eliminado con éxito.';
      ELSE
        UPDATE votar_o
        SET idOpcion = idO 
        WHERE idUsuario = idU AND idOpcion = idOAntigua;
        SET mensaje = 'Voto actualizado con éxito.';
      END IF;
    END IF;
    COMMIT; 
END //
DELIMITER ;
Delimiter //
	  --TRIGGER DE INSERTS

	CREATE TRIGGER trg_after_insert_segmento
	AFTER INSERT ON segmentos
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'segmentos',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_insert_plataforma
	AFTER INSERT ON plataforma
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'plataforma',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_insert_programas
	AFTER INSERT ON programas
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'programas',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_insert_emisiones
	AFTER INSERT ON emisiones
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'emisiones',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_insert_rol
	AFTER INSERT ON rol
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'rol',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_insert_usuario
	AFTER INSERT ON usuario
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'usuario',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_insert_contenidos
	AFTER INSERT ON contenidos
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'contenidos',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_audiencia_con
	AFTER INSERT ON audiencia_con
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'audiencia_con',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_encuesta
	AFTER INSERT ON encuesta
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'encuesta',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_opcion_e
	AFTER INSERT ON opcion_e
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'opcion_e',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_votar_o
	AFTER INSERT ON votar_o
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'votar_o',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_errores
	AFTER INSERT ON errores
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('INSERT',@current_user_id,'errores',NEW.id,NOW());
	END //

	
	
-- TRIGGER UPDATE

	CREATE TRIGGER trg_after_update_plataforma
	AFTER UPDATE ON plataforma
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'plataforma',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_programas
	AFTER UPDATE ON programas
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'programas',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_segmentos
	AFTER UPDATE ON segmentos
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'segmentos',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_emisiones
	AFTER UPDATE ON emisiones
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'emisiones',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_rol
	AFTER UPDATE ON rol
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'rol',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_usuario
	AFTER UPDATE ON usuario
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'usuario',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_contenidos
	AFTER UPDATE ON contenidos
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'contenidos',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_audiencia_con
	AFTER UPDATE ON audiencia_con
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'audiencia_con',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_encuesta
	AFTER UPDATE ON encuesta
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'encuesta',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_opcion_e
	AFTER UPDATE ON opcion_e
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'opcion_e',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_votar_o
	AFTER UPDATE ON votar_o
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'votar_o',NEW.id,NOW());
	END //
	
	CREATE TRIGGER trg_after_update_errores
	AFTER UPDATE ON errores
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('UPDATE',@current_user_id,'errores',NEW.id,NOW());
	END //
	
	
-- TRIGGER DELETE


	CREATE TRIGGER trg_before_delete_plataforma
	BEFORE DELETE ON plataforma
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'plataforma',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_programas
	BEFORE DELETE ON programas
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'programas',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_segmentos
	BEFORE DELETE ON segmentos
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'segmentos',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_emisiones
	BEFORE DELETE ON emisiones
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'emisiones',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_rol
	BEFORE DELETE ON rol
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'rol',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_usuario
	BEFORE DELETE ON usuario
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'usuario',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_contenidos
	BEFORE DELETE ON contenidos
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'contenidos',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_audiencia_con
	BEFORE DELETE ON audiencia_con
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'audiencia_con',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_encuesta
	BEFORE DELETE ON encuesta
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'encuesta',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_opcion_e
	BEFORE DELETE ON opcion_e
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'opcion_e',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_votar_o
	BEFORE DELETE ON votar_o
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'votar_o',OLD.id,NOW());
	END //
	
	CREATE TRIGGER trg_before_delete_errores
	BEFORE DELETE ON errores
	FOR EACH ROW
	BEGIN
		INSERT INTO auditoria (accion,usuario_id,tablaM,registro_afectado_id,fecha)
		VALUES ('DELETE',@current_user_id,'errores',OLD.id,NOW());
	END //

Delimiter ;
 

INSERT INTO rol (nombre) VALUES ('Productor/Editor'); 

INSERT INTO rol (nombre) VALUES ('Productor/Editor'); 

INSERT INTO rol (nombre) VALUES ('Productor/Editor'); 

INSERT INTO rol (nombre) VALUES ('Productor/Editor'); 

INSERT INTO rol (nombre) VALUES ('Productor/Editor'); 

INSERT INTO rol (nombre) VALUES ('Productor/Editor'); 

INSERT INTO rol (nombre) VALUES ('Productor/Editor'); 


INSERT INTO rol (nombre) VALUES ('Administrador');   


INSERT INTO rol (nombre) VALUES ('Programador');

INSERT INTO rol (nombre) VALUES ('Programador');

INSERT INTO rol (nombre) VALUES ('Programador');


INSERT INTO rol (nombre) VALUES ('Espectador');


 
INSERT INTO permisos (tipoPermiso) VALUES ('SUBIR_CONTENIDO');
INSERT INTO permisos (tipoPermiso) VALUES ('ESTADO-APROBACION'); 
INSERT INTO permisos (tipoPermiso) VALUES ('VER_PARRILLA'); 
 
INSERT INTO permisos (tipoPermiso) VALUES ('HACER-TODO');  

INSERT INTO permisos (tipoPermiso) VALUES ('ARMAR_PARRILLA'); 
INSERT INTO permisos (tipoPermiso) VALUES ('CONTROLAR_EMISION');

INSERT INTO permisos (tipoPermiso) VALUES ('HACER-NADA');


INSERT INTO permisos_rol (idRol, idPermiso) VALUES (1, 1); 
INSERT INTO permisos_rol (idRol, idPermiso) VALUES (1, 2); 
INSERT INTO permisos_rol (idRol, idPermiso) VALUES (1, 3); 

INSERT INTO permisos_rol (idRol, idPermiso) VALUES (2, 1); 
INSERT INTO permisos_rol (idRol, idPermiso) VALUES (2, 2); 

INSERT INTO permisos_rol (idRol, idPermiso) VALUES (3, 1); 
INSERT INTO permisos_rol (idRol, idPermiso) VALUES (3, 3); 

INSERT INTO permisos_rol (idRol, idPermiso) VALUES (4, 2); 
INSERT INTO permisos_rol (idRol, idPermiso) VALUES (4, 3); 

INSERT INTO permisos_rol (idRol, idPermiso) VALUES (5, 1); 

INSERT INTO permisos_rol (idRol, idPermiso) VALUES (6, 2); 

INSERT INTO permisos_rol (idRol, idPermiso) VALUES (7, 3); 


INSERT INTO permisos_rol (idRol, idPermiso) VALUES (8, 4); 


INSERT INTO permisos_rol (idRol, idPermiso) VALUES (9, 5); 
INSERT INTO permisos_rol (idRol, idPermiso) VALUES (9, 6); 

INSERT INTO permisos_rol (idRol, idPermiso) VALUES (10, 5); 

INSERT INTO permisos_rol (idRol, idPermiso) VALUES (11, 6); 


INSERT INTO permisos_rol (idRol, idPermiso) VALUES (12, 7); 


































