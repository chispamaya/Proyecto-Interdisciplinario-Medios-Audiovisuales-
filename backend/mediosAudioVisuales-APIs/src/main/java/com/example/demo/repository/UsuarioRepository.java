package com.example.demo.repository;
import com.example.demo.dto.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// Marcar la clase repository
@Repository
public class UsuarioRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

   public void crearUsuario(Usuario usuario) {
      
       String sql = "CALL cu(?, ?, ?, ?)"; 
       
       
       jdbcTemplate.update(sql, 
           usuario.getEmail(),
           usuario.getNombre(),
           usuario.getContrasenia(),
           usuario.getIdRol()
       );
   }

 
   public void borrarUsuario(Long idUsuario) {
       String sql = "CALL bu(?)";
       
       jdbcTemplate.update(sql, idUsuario);
   }

   
   public void modificarUsuario(Usuario usuario) {
       String sql = "CALL mu(?, ?, ?, ?, ?)"; 
       
       jdbcTemplate.update(sql, 
           usuario.getId(),
           usuario.getEmail(),
           usuario.getNombre(),
           usuario.getContrasenia(),
           usuario.getIdRol()
       );
   }
	
}
