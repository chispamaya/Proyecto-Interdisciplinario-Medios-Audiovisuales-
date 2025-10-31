package com.example.demo.dto;

import lombok.Data;
// Ya no se importa jakarta.persistence.*
// Ya no se importa java.util.List

@Data
public class Rol {

    // Solo definimos los campos que son columnas
    // directas en tu tabla 'rol'
    
    private Long id;
    private String nombre;
    
    // --- NOTA IMPORTANTE ---
    // La lista '@OneToMany private List<Usuario> usuarios;' se elimina
    // porque eso es algo que JPA resuelve "mágicamente".
    //
    // Con JDBC, tu POJO 'Rol' solo debe representar las columnas
    // de la tabla 'rol'.
    //
    // Si querés "todos los usuarios que tienen el ROL 5", 
    // tendrías que hacer una consulta en tu 'UsuarioRepository'
    // que busque WHERE idRol = 5.
}