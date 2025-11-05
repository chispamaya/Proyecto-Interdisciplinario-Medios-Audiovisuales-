package com.example.demo.repository;

import com.example.demo.dto.AudienciaCon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AudienciaConRepositoryTests {

    @Autowired
    private AudienciaConRepository audienciaConRepository;

    // --- IDs para la prueba ---
    // ¡Asegúrate de que estos IDs existan en tu BD de XAMPP!
    // ID 8 = Rol "Administrador" (para auditoría)
    // ID 12 = Rol "Espectador" (para votar)
    // ID 1 = Un contenido (para votar sobre él)
    private static final Long ID_USUARIO_AUDITORIA = 8L;
    private static final Long ID_CONTENIDO_PRUEBA = 1L;
    private static final Long ID_USUARIO_PRUEBA = 12L;

    @Test
    void probarCrearYBorrarVoto() {
        System.out.println("--- INICIANDO PRUEBA DE REPOSITORIO DE AUDIENCIA_CON ---");

        // 1. Probamos CREAR el voto (SP 'ld')
        System.out.println("Probando SP 'ld' (crearOModificarVoto)...");
        
        AudienciaCon nuevoVoto = new AudienciaCon();
        nuevoVoto.setLikeOdislike(true); // true = Like
        nuevoVoto.setIdContenido(ID_CONTENIDO_PRUEBA);
        nuevoVoto.setIdUsuario(ID_USUARIO_PRUEBA);

        // Ejecutamos la creación
        String mensajeCrear = audienciaConRepository.crearOModificarVoto(nuevoVoto, ID_USUARIO_AUDITORIA);

        // Verificamos el mensaje de éxito del SP 'ld'
        assertEquals("Valoración actualizada con éxito.", mensajeCrear);
        System.out.println("Prueba 'ld' (Like) exitosa.");


        // 2. Probamos BORRAR el voto (SP 'bv')
        // Usamos los mismos IDs para borrar el voto que acabamos de crear.
        System.out.println("Probando SP 'bv' (borrarVoto) con ID Contenido: " + ID_CONTENIDO_PRUEBA + ", ID Usuario: " + ID_USUARIO_PRUEBA);
        
        // Ejecutamos el borrado
        String mensajeBorrar = audienciaConRepository.borrarVoto(ID_CONTENIDO_PRUEBA, ID_USUARIO_PRUEBA, ID_USUARIO_AUDITORIA);
        
        // Verificamos el mensaje de éxito del SP 'bv'
        assertEquals("Valoración actualizada con éxito.", mensajeBorrar);
        System.out.println("Prueba 'bv' (Borrar Voto) exitosa.");

        
        System.out.println("--- PRUEBA DE AUDIENCIA_CON COMPLETADA CON ÉXITO ---");
    }
}