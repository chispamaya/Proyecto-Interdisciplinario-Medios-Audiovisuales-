package com.example.demo.dto;
import java.util.List; // <-- Importante
import lombok.Data;

@Data
public class EmpleadoDto {
	private Long id;
    private String nombre;
    private String email;
    private String nombreRol; 
    

    private List<String> permisos;
}