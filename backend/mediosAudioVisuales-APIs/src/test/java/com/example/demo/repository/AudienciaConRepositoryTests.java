package com.example.demo.repository;

import com.example.demo.dto.AudienciaCon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class AudienciaConRepositoryTests {

    @Autowired
    private AudienciaConRepository audienciaConRepository;

    
    private static final Long ID_USUARIO_AUDITORIA = 1L; 
    private static final Long ID_CONTENIDO_PRUEBA = 1L;
    private static final Long ID_USUARIO_PRUEBA = 1L;

    @Test
    void testCrearYBorrarVoto() {
        System.out.println("--- Probando AudienciaConRepository ---");

        
        AudienciaCon voto = new AudienciaCon();
        voto.setLikeOdislike(true); 
        voto.setIdContenido(ID_CONTENIDO_PRUEBA);
        voto.setIdUsuario(ID_USUARIO_PRUEBA);

        String msgLike = audienciaConRepository.crearOModificarVoto(voto, ID_USUARIO_AUDITORIA);
        
        assertEquals("Valoración actualizada con éxito.", msgLike);
        System.out.println("Prueba 'ld' (Like) exitosa.");

    
        String msgBorrar = audienciaConRepository.borrarVoto(ID_CONTENIDO_PRUEBA, ID_USUARIO_PRUEBA, ID_USUARIO_AUDITORIA);
     
        assertEquals("Valoración actualizada con éxito.", msgBorrar);
        System.out.println("Prueba 'bv' (Borrar Voto) exitosa.");
    }
}