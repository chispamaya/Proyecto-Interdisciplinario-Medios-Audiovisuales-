package com.example.demo.repository;

import com.example.demo.dto.Programa; // Importa tu DTO
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
public class ProgramaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP 'cpr' (CORREGIDO)
     * Ahora pasa los 10 parámetros
     */
    public String crearPrograma(Programa programa, Long idUsuarioAuditoria) {
        
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cpr");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("categoria1", programa.getCategoria());
        inParams.put("nombre1", programa.getNombre());
        inParams.put("horaInicio1", programa.getHoraInicio());
        inParams.put("horaFin1", programa.getHoraFin());
        inParams.put("idP1", programa.getIdPlataforma());
        inParams.put("formatoArchivo1", programa.getFormatoArchivo());
        inParams.put("rutaArchivo1", programa.getRutaArchivo());
        
        // --- 💥 ¡LÍNEAS FALTANTES AGREGADAS AQUÍ! 💥 ---
        inParams.put("formatoInforme1", programa.getFormatoInforme());
        inParams.put("rutaInforme1", programa.getRutaInforme());
        // --- 💥 ---
        
        inParams.put("idUs", idUsuarioAuditoria);


        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    
    public String borrarPrograma(Long idProgramaAEliminar, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bpr");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idProgramaAEliminar);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al SP 'mpr' (CORREGIDO)
     * Ahora pasa todos los parámetros que el SP 'mpr' espera
     */
    public String modificarPrograma(Programa programa, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("mpr");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", programa.getId());
        inParams.put("estadoAprobacion1", programa.getEstadoAprobacion());
        inParams.put("categoria1", programa.getCategoria());
        inParams.put("nombre1", programa.getNombre());
        inParams.put("horaInicio1", programa.getHoraInicio());
        inParams.put("horaFin1", programa.getHoraFin());
        inParams.put("idP1", programa.getIdPlataforma());
        
        // --- 💥 ¡LÍNEAS FALTANTES AGREGADAS AQUÍ! 💥 ---
        inParams.put("formatoArchivo1", programa.getFormatoArchivo());
        inParams.put("rutaArchivo1", programa.getRutaArchivo());
        inParams.put("formatoInforme1", programa.getFormatoInforme());
        inParams.put("rutaInforme1", programa.getRutaInforme());
        // --- 💥 ---
        
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

 
    public List<Programa> listarTodosLosProgramas() {
        // (Este método está perfecto, usa SQL directo)
        String sql = "SELECT * FROM programas";     
        return jdbcTemplate.query(sql, new ProgramaRowMapper());
    }

    
    public Programa buscarProgramaPorId(Long idProgramaBuscado) {
        // (Este método está perfecto, usa el SP 's' de 3 params)
        String sql = "CALL s('programas', ?, @mensaje)";       
        try {
            return jdbcTemplate.queryForObject(sql, new ProgramaRowMapper(), idProgramaBuscado);
        } catch (Exception e) {
            return null; 
        }
    }
    
    
    /**
     * "Traductor" (CORREGIDO)
     * Ahora lee los campos de informe
     */
    private static class ProgramaRowMapper implements RowMapper<Programa> {
        @Override
        public Programa mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            Programa programa = new Programa();
            
            programa.setId(rs.getLong("id"));
            programa.setEstadoAprobacion(rs.getString("estadoAprobacion"));
            programa.setCategoria(rs.getString("categoria"));
            programa.setNombre(rs.getString("nombre"));
            
            java.sql.Time sqlHoraInicio = rs.getTime("horaInicio");
            if (sqlHoraInicio != null) {
                programa.setHoraInicio(sqlHoraInicio.toLocalTime()); 
            }

            java.sql.Time sqlHoraFin = rs.getTime("horaFin");
            if (sqlHoraFin != null) {
                programa.setHoraFin(sqlHoraFin.toLocalTime());      
            }
            
            programa.setIdPlataforma(rs.getLong("idPlataforma"));
            programa.setFormatoArchivo(rs.getString("formatoArchivo"));
            programa.setRutaArchivo(rs.getString("rutaArchivo"));

            // --- 💥 ¡LÍNEAS FALTANTES AGREGADAS AQUÍ! 💥 ---
            programa.setFormatoInforme(rs.getString("formatoInforme"));
            programa.setRutaInforme(rs.getString("rutaInforme"));
            // --- 💥 ---
            
            return programa;
        }
    }
}