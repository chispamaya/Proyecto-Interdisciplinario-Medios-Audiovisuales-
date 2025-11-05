package com.example.demo.repository;

import com.example.demo.dto.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RolRepositoryTests {

    @Autowired
    private RolRepository rolRepository;

    @Test
    void probarMetodosDeLecturaRol() {
        System.out.println("--- INICIANDO PRUEBA DE REPOSITORIO DE ROL ---");

        System.out.println("Probando SP 's' (listarTodosLosRoles)...");
        
        List<Rol> listaDeRoles = rolRepository.listarTodosLosRoles();

        assertNotNull(listaDeRoles);
        
        assertTrue(listaDeRoles.size() > 0); 
        
        System.out.println("Prueba de listado exitosa. Roles encontrados: " + listaDeRoles.size());

        System.out.println("Probando 'buscarRolPorId(4L)'...");
        
        Rol rolEncontrado = rolRepository.buscarRolPorId(4L);

        assertNotNull(rolEncontrado);
        
        assertEquals("Productor/Editor", rolEncontrado.getNombre());
        
        System.out.println("Prueba de búsqueda por ID exitosa. Encontrado: " + rolEncontrado.getNombre());
    }
}