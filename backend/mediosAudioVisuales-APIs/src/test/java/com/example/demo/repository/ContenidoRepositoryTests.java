package com.example.demo.repository;

import com.example.demo.dto.Contenido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContenidoRepositoryTests {

    @Autowired
    private ContenidoRepository contenidoRepository;

    // IDs de prueba (Deben existir en la BD)
    private static final Long ID_USUARIO_AUDITORIA = 8L; // Admin
    private static final Long ID_USUARIO_CREADOR = 1L;   // Productor/Editor

    @Test
    void probarCrearListarYBorrarContenido() {
        System.out.println("--- Probando ContenidoRepository ---");

        // 1. Probamos CREAR (SP 'cc')
        System.out.println("Probando SP 'cc' (crearContenido)...");
        Contenido nuevoContenido = new Contenido();
        nuevoContenido.setFormato("mp3");
        nuevoContenido.setRutaArchivo("/audio/test-" + System.currentTimeMillis() + ".mp3");
        nuevoContenido.setIdUsuario(ID_USUARIO_CREADOR);

        String msgCrear = contenidoRepository.crearContenido(nuevoContenido, ID_USUARIO_AUDITORIA);
        assertEquals("Contenido subido con éxito.", msgCrear);

        // 2. Probamos LISTAR (SP 's')
        System.out.println("Probando SP 's' (listarTodosLosContenidos)...");
        List<Contenido> lista = contenidoRepository.listarTodosLosContenidos();
        assertNotNull(lista);
        assertTrue(lista.size() > 0);
        System.out.println("Contenidos encontrados: " + lista.size());

        // 3. Probamos BORRAR (SP 'bc')
        // Obtenemos el ID del contenido que acabamos de crear
        Long idParaBorrar = lista.get(lista.size() - 1).getId();
        System.out.println("Probando SP 'bc' (borrarContenido) con ID: " + idParaBorrar);
        
        String msgBorrar = contenidoRepository.borrarContenido(idParaBorrar, ID_USUARIO_AUDITORIA);
        assertEquals("Contenido borrado con éxito.", msgBorrar);

        System.out.println("--- PRUEBA DE CONTENIDO COMPLETADA ---");
    }
}