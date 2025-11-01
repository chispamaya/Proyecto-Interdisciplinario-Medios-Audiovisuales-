package com.example.demo.dto;

import lombok.Data;
// Ya no se importa jakarta.persistence.*
// Ya no se importa java.util.Set

@Data
public class Tag {

    private Long id;
    private String tag;

    // --- NOTA IMPORTANTE ---
    // La relación @ManyToMany private Set<Contenido> contenidos; SE ELIMINA.
    //
    // ¿Por qué?
    // Una relación "Muchos a Muchos" (ManyToMany) funciona con una
    // TERCERA TABLA (una tabla "join" o "intermedia"), por ejemplo: 'contenido_tags'.
    //
    // La clase 'Tag' que definimos aquí solo debe representar 
    // las columnas de la tabla 'tags' (que son 'id' y 'tag').
    //
    // Con JDBC, si querés buscar "todos los contenidos de un tag",
    // tenés que escribir un SQL manual en tu Repositorio que
    // haga un JOIN usando esa tabla intermedia.
    //
    // Ejemplo de SQL:
    // "SELECT c.* FROM contenido c
    //  INNER JOIN contenido_tags ct ON c.id = ct.idContenido
    //  WHERE ct.idTag = ?"
}