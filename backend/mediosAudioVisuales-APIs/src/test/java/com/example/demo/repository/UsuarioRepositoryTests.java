package com.example.demo.repository; // El paquete raíz

// Importamos las clases que vamos a usar
import com.example.demo.dto.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class UsuarioRepositoryTests {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void probarCrearYListarUsuarios() {
        
        System.out.println("--- INICIANDO PRUEBA DE REPOSITORIO DE USUARIO ---");

       
        
        System.out.println("Probando SP 'cu' (crearUsuario)...");
        

        Usuario nuevoUsuario = new Usuario();
        
        
        String emailDePrueba = "test" + System.currentTimeMillis() + "@correo.com";
        nuevoUsuario.setEmail(emailDePrueba);
        nuevoUsuario.setNombre("Usuario de Prueba");
        nuevoUsuario.setContrasenia("123456");
        nuevoUsuario.setIdRol(4L); 

        
        String mensajeRespuesta = usuarioRepository.crearUsuario(nuevoUsuario, 1L);

      
        System.out.println("Respuesta del SP 'cu': " + mensajeRespuesta);
        assertEquals("Usuario ingresado con éxito.", mensajeRespuesta);



        System.out.println("Probando SP 's' (listarTodosLosUsuarios)...");
        
        List<Usuario> listaDeUsuarios = usuarioRepository.listarTodosLosUsuarios();

        assertNotNull(listaDeUsuarios); 
        assertTrue(listaDeUsuarios.size() > 0); 

        System.out.println("Prueba exitosa. Usuarios encontrados: " + listaDeUsuarios.size());
        System.out.println("El primer usuario en la lista es: " + listaDeUsuarios.get(0).getNombre());
    }
}
