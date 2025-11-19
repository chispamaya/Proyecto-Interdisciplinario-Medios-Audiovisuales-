package com.example.demo.repository;

import com.example.demo.dto.Emision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class EmisionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

   
    public String modificarEmision(Emision emision, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("me");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", emision.getId());
        inParams.put("enVivo1", emision.getEnVivo());
        inParams.put("idPr1", emision.getIdPrograma());
        inParams.put("idUs", idUsuarioAuditoria); // Parámetro de auditoría

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    
    public Emision buscarEmisionPorId(Long idEmision) {
       
        String sql = "SELECT * FROM emisiones WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new EmisionRowMapper(), idEmision);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    
    public List<Emision> listarTodasLasEmisiones() {
        String sql = "CALL s('emisiones', null, @mensaje)";
        return jdbcTemplate.query(sql, new EmisionRowMapper());
    }

    // --- 🔥 MÉTODO "FUERZA BRUTA" (Prueba todas las combinaciones) 🔥 ---
    public boolean insertarEmisionEnVivo(Long idPrograma, Long idUsuario) {
        try {
            // 1. Apagamos emisiones anteriores
            // Usamos 'enVivo' (CamelCase) como nos dijo el diagnóstico
            String sqlApagar = "UPDATE emisiones SET enVivo = 0 WHERE enVivo = 1";
            jdbcTemplate.update(sqlApagar);

            // 2. Insertamos la nueva emisión
            // SOLO insertamos 'idPrograma' y 'enVivo'. No insertamos fecha ni hora porque NO existen en la tabla.
            String sqlInsert = "INSERT INTO emisiones (idPrograma, enVivo) VALUES (?, 1)";
            
            int filas = jdbcTemplate.update(sqlInsert, idPrograma);
            return filas > 0;
            
        } catch (Exception e) {
            System.err.println("❌ Error insertando emisión: " + e.getMessage());
            return false;
        }
    }
    
    // --- 🔥 NUEVO MÉTODO: FINALIZAR EMISIÓN (Para el botón ROJO) 🔥 ---
    public boolean finalizarEmision(Long idEmision, Long idUsuario) {
        try {
            // ⚠️ CORRECCIÓN CRÍTICA: Eliminé 'horaFin = CURTIME()' porque esa columna NO EXISTE.
            // Usamos la opción 'Nuclear' (apagar todo) para asegurar que se limpie la pantalla.
            String sql = "UPDATE emisiones SET enVivo = 0 WHERE enVivo = 1";
            
            int filas = jdbcTemplate.update(sql);
            return filas > 0;
        } catch (Exception e) {
            System.err.println("❌ Error finalizando emisión: " + e.getMessage());
            return false;
        }
    }
    private static class EmisionRowMapper implements RowMapper<Emision> {
        @Override
        public Emision mapRow(ResultSet rs, int rowNum) throws SQLException {
            Emision emision = new Emision();
            emision.setId(rs.getLong("id"));
            emision.setEnVivo(rs.getBoolean("enVivo"));
            emision.setIdPrograma(rs.getLong("idPrograma"));
            return emision;
        }
    }
}