package com.example.demo.repository;

import com.example.demo.dto.PermisosRol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PermisosRolRepositoryTests {

    @Autowired
    private PermisosRolRepository permisosRolRepository;

    @Test
    void probarListarTodasLasAsignaciones() {
        System.out.println("--- Probando SP 's' (listarTodasLasAsignaciones) ---");
        List<PermisosRol> lista = permisosRolRepository.listarTodasLasAsignaciones();
        
        assertNotNull(lista);
        // En tu DB.sql insertas 17 asignaciones
        assertEquals(17, lista.size(), "Deberían cargarse 17 asignaciones desde el DB.sql");
        System.out.println("Prueba 's' exitosa. Asignaciones encontradas: " + lista.size());
    }
}