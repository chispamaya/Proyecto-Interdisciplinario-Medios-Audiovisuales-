package com.example.demo.repository;

import com.example.demo.dto.Permisos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PermisosRepositoryTests {

    @Autowired
    private PermisosRepository permisosRepository;

    @Test
    void probarListarTodosLosPermisos() {
        System.out.println("--- Probando SP 's' (listarTodosLosPermisos) ---");
        List<Permisos> lista = permisosRepository.listarTodosLosPermisos();
        
        assertNotNull(lista);
        // En tu DB.sql insertas 7 permisos
        assertEquals(7, lista.size(), "Deberían cargarse 7 permisos desde el DB.sql");
        System.out.println("Prueba 's' exitosa. Permisos encontrados: " + lista.size());
    }

    @Test
    void probarBuscarPermisosPorRol() {
        System.out.println("--- Probando SP 's' (buscarPermisosPorRol) ---");
        
        // --- Prueba Administrador ---
        // El rol 'Administrador' (ID 8) tiene 1 permiso: 'HACER-TODO' (ID 4)
        List<String> permisosAdmin = permisosRepository.buscarPermisosPorRol("Administrador");
        assertNotNull(permisosAdmin);
        assertEquals(1, permisosAdmin.size());
        assertEquals("HACER-TODO", permisosAdmin.get(0));
        System.out.println("Permisos de Administrador: " + permisosAdmin.get(0));

        // --- Prueba Programador ---
        // El rol 'Programador' (ID 9) tiene 2 permisos
        List<String> permisosProgramador = permisosRepository.buscarPermisosPorRol("Programador");
        assertNotNull(permisosProgramador);
        assertEquals(2, permisosProgramador.size());
        assertTrue(permisosProgramador.contains("ARMAR_PARRILLA"), "Debería tener permiso ARMAR_PARRILLA");
        assertTrue(permisosProgramador.contains("CONTROLAR_EMISION"), "Debería tener permiso CONTROLAR_EMISION");
        System.out.println("Permisos de Programador: " + permisosProgramador.size());

        // --- ¡¡PRUEBA AÑADIDA!! Productor/Editor ---
        // El rol 'Productor/Editor' (ID 1) tiene 3 permisos
        List<String> permisosProductor = permisosRepository.buscarPermisosPorRol("Productor/Editor");
        assertNotNull(permisosProductor);
        // Tu SP 's' busca por nombre, y tenés varios roles con el mismo nombre.
        // El SP agarra TODOS los permisos de TODOS los roles que se llamen 'Productor/Editor'.
        // Tu DB inserta 7 roles "Productor/Editor" (IDs 1 al 7).
        // Las asignaciones (IDs 1 al 11) cubren los permisos 1, 2 y 3.
        // El resultado debería ser la lista ÚNICA de esos permisos.
        assertEquals(3, permisosProductor.size(), "Debería tener 3 permisos únicos (1, 2, y 3)");
        assertTrue(permisosProductor.contains("SUBIR_CONTENIDO"), "Debería tener permiso SUBIR_CONTENIDO");
        assertTrue(permisosProductor.contains("ESTADO-APROBACION"), "Debería tener permiso ESTADO-APROBACION");
        assertTrue(permisosProductor.contains("VER_PARRILLA"), "Debería tener permiso VER_PARRILLA");
        System.out.println("Permisos de Productor/Editor: " + permisosProductor.size());
    }
}