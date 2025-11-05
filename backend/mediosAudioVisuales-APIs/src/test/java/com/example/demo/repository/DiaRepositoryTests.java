package com.example.demo.repository;

import com.example.demo.dto.Dia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DiaRepositoryTests {

    @Autowired
    private DiaRepository diaRepository;

    // IDs de prueba (Deben existir en la BD por el Paso 0)
    private static final Long ID_USUARIO_AUDITORIA = 8L; // Admin
    private static final Long ID_PROGRAMA_PRUEBA = 1L;   // Programa de prueba

    @Test
    void probarCrearListarYBorrarDia() {
        System.out.println("--- Probando DiaRepository ---");

        // 1. Probamos CREAR (SP 'cd')
        System.out.println("Probando SP 'cd' (crearDia)...");
        Dia nuevoDia = new Dia();
        nuevoDia.setDia(LocalDate.now()); // Usamos el día de hoy
        nuevoDia.setIdPrograma(ID_PROGRAMA_PRUEBA);

        String msgCrear = diaRepository.crearDia(nuevoDia, ID_USUARIO_AUDITORIA);
        assertEquals("Dia asignado al programa con éxito.", msgCrear);

        // 2. Probamos LISTAR (SP 's')
        System.out.println("Probando SP 's' (listarTodosLosDias)...");
        List<Dia> lista = diaRepository.listarTodosLosDias();
        assertNotNull(lista);
        assertTrue(lista.size() > 0);
        System.out.println("Dias encontrados: " + lista.size());

        // 3. Probamos BORRAR (SP 'bd')
        Long idParaBorrar = lista.get(lista.size() - 1).getId();
        System.out.println("Probando SP 'bd' (borrarDia) con ID: " + idParaBorrar);
        
        String msgBorrar = diaRepository.borrarDia(idParaBorrar, ID_USUARIO_AUDITORIA);
        assertEquals("Dia eliminado con éxito.", msgBorrar);

        System.out.println("--- PRUEBA DE DIA COMPLETADA ---");
    }
}