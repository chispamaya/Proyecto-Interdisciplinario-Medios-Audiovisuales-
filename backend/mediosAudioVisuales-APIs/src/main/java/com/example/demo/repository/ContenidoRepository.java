package com.example.demo.repository;

import com.example.demo.dto.Contenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ContenidoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * (ACTUALIZADO) Llama al SP cc (Crear Contenido) con todos los campos.
     * Usado por 'SubidaMultimedia.jsx'.
     */
    public String crearContenido(Contenido contenido, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("formato1", contenido.getFormato());
        inParams.put("rutaArchivo1", contenido.getRutaArchivo());
        inParams.put("idU1", contenido.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria);
        
        // (5) Pasamos los nuevos parámetros al SP
        inParams.put("titulo1", contenido.getTitulo());
        inParams.put("tipo1", contenido.getTipo());
        inParams.put("duracion1", contenido.getDuracion());
        inParams.put("tamano1", contenido.getTamano());

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * (EXISTENTE) Llama al SP bc (Borrar Contenido).
     * Usado por 'GestionMultimedia.jsx'.
     */
    public String borrarContenido(Long idContenidoAEliminar, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idContenidoAEliminar);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * (NUEVO) Llama al SP 'mces' (Modificar Contenido Estado).
     * Usado por 'EstadoAprobacion.jsx'.
     */
    public String modificarEstadoContenido(Long idContenido, String nuevoEstado, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("mces");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idContenido1", idContenido);
        inParams.put("nuevoEstado1", nuevoEstado);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * (NUEVO) Lista TODO el contenido.
     * Usado por 'EstadoAprobacion.jsx'.
     */
    public List<Contenido> listarTodosLosContenidos() {
        // Usamos el SP 's' genérico para traer todo
        String sql = "CALL s('contenidos', null, @mensaje)";
        return jdbcTemplate.query(sql, new ContenidoRowMapper());
    }

    /**
     * (NUEVO) Lista contenido FILTRADO por usuario.
     * Usado por 'GestionMultimedia.jsx' (que solo muestra "Tu contenido").
     */
    public List<Contenido> listarContenidosPorUsuario(Long idUsuario) {
        // (6) Como el SP 's' no puede filtrar por usuario, escribimos el SQL.
        String sql = "SELECT * FROM contenidos WHERE idUsuario = ?";
        return jdbcTemplate.query(sql, new ContenidoRowMapper(), idUsuario);
    }
    /**
     * (NUEVO) RowMapper para Contenido.
     * Le dice a Spring cómo convertir una fila de la BD (con las nuevas columnas)
     * en un objeto DTO Contenido.java.
     */
    class ContenidoRowMapper implements RowMapper<Contenido> {
        @Override
        public Contenido mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contenido contenido = new Contenido();
            contenido.setId(rs.getLong("id"));
            contenido.setFormato(rs.getString("formato"));
            contenido.setRutaArchivo(rs.getString("rutaArchivo"));
            contenido.setIdUsuario(rs.getLong("idUsuario"));
            
            // Mapeamos los nuevos campos
            contenido.setTitulo(rs.getString("titulo"));
            contenido.setTipo(rs.getString("tipo"));
            contenido.setEstado(rs.getString("estado"));
            contenido.setDuracion(rs.getString("duracion"));
            contenido.setTamano(rs.getString("tamano"));
            
            return contenido;
        }
    }
}

