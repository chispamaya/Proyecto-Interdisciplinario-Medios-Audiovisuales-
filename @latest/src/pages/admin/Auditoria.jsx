import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../styles/pages/auditoria.css'; 

// --- Función auxiliar para arreglar la fecha ---
const formatearFecha = (fecha) => {
    if (!fecha) return '-';
    
    // Caso 1: Si Java manda un array [2025, 11, 19, 10, 30, 15]
    if (Array.isArray(fecha)) {
        // new Date(año, mes-1, dia, hora, min, seg) -> Mes en JS empieza en 0
        return new Date(fecha[0], fecha[1] - 1, fecha[2], fecha[3], fecha[4], fecha[5]).toLocaleString();
    }
    
    // Caso 2: Si ya viene como texto ISO "2025-11-19T10:30:00"
    return new Date(fecha).toLocaleString();
};

// Componente de Fila de Auditoría
const AuditRow = ({ log }) => {
    return (
        <div className="auditoria-row-scroll-wrapper">
            <div className="auditoria-row-content">
                
                {/* 🟢 USAMOS LA FUNCIÓN AQUÍ */}
                <div className="auditoria-cell fecha">
                    {formatearFecha(log.fecha)}
                </div>

                <div className="auditoria-cell usuario">
                    {log.usuario_id || log.idUsuario} 
                </div> 

                <div className="auditoria-cell accion">{log.accion}</div>
                
                <div className="auditoria-cell tabla">
                    {log.tablaM || log.nombreTabla}
                </div>

                <div className="auditoria-cell registro">
                    {log.registro_afectado_id || log.idRegistroAfectado || "-"}
                </div>

                <div className="auditoria-cell detalle">
                    {log.detalle || "Sin detalles"}
                </div>
            </div>
        </div>
    );
};

export default function Auditoria() {
    const [logs, setLogs] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState(null);

    const [filtroTabla, setFiltroTabla] = useState("");
    const [filtroAccion, setFiltroAccion] = useState("");

    useEffect(() => {
        cargarAuditoria();
    }, []);

    const cargarAuditoria = async () => {
        setCargando(true);
        try {
            let url = 'http://localhost:8080/api/auditoria';
            const params = {};

            if (filtroTabla || filtroAccion) {
                url = 'http://localhost:8080/api/auditoria/filtro';
                if (filtroTabla) params.tabla = filtroTabla;
                if (filtroAccion) params.accion = filtroAccion;
            }

            const response = await axios.get(url, { params });
            setLogs(response.data);
            setError(null);
        } catch (err) {
            console.error("Error cargando auditoría:", err);
            setError("No se pudo cargar el historial.");
        } finally {
            setCargando(false);
        }
    };

    return (
        <div className="auditoria-container">
            <header className="auditoria-header">
                <h1>Registro de Auditoría</h1>
                
                <div className="filtros-auditoria" style={{display:'flex', gap:'10px', marginTop:'10px'}}>
                    <input 
                        placeholder="Filtrar por Tabla" 
                        value={filtroTabla}
                        onChange={(e) => setFiltroTabla(e.target.value)}
                        style={{padding:'5px'}}
                    />
                    <input 
                        placeholder="Filtrar por Acción" 
                        value={filtroAccion}
                        onChange={(e) => setFiltroAccion(e.target.value)}
                        style={{padding:'5px'}}
                    />
                    <button onClick={cargarAuditoria} style={{cursor:'pointer'}}>Filtrar</button>
                </div>
            </header>

            <div className="auditoria-header-scroll-wrapper">
                <div className="auditoria-list-header-content">
                    <div className="auditoria-cell fecha">Fecha y Hora</div>
                    <div className="auditoria-cell usuario">ID Usuario</div>
                    <div className="auditoria-cell accion">Acción</div>
                    <div className="auditoria-cell tabla">Tabla</div>
                    <div className="auditoria-cell registro">Registro ID</div>
                    <div className="auditoria-cell detalle">Detalle</div>
                </div>
            </div>

            <div className="auditoria-list-container">
                {cargando && <p style={{padding:'20px', color:'white'}}>Cargando...</p>}
                {error && <p style={{padding:'20px', color:'red'}}>{error}</p>}
                
                {!cargando && !error && logs.length === 0 && (
                    <p style={{padding:'20px', color:'#ccc'}}>No hay registros de auditoría.</p>
                )}

                {!cargando && logs.map((log) => (
                    <AuditRow key={log.id} log={log} />
                ))}
            </div>
        </div>
    );
}