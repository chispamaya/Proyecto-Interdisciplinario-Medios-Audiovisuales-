// src/pages/admin/Auditoria.jsx (Nuevo)

import React from 'react';
import '../../styles/pages/auditoria.css'; // Importamos el nuevo CSS

// Datos de ejemplo basados en la tabla 'auditoria'
const MOCK_AUDITORIA_DATA = [
    {
        id: 1,
        fecha: '2025-09-08 10:30:15',
        usuario_id: 2, // Admin
        accion: 'INSERT',
        tablaM: 'programas',
        registro_afectado_id: 112
    },
    {
        id: 2,
        fecha: '2025-09-08 10:32:00',
        usuario_id: 5, // Editor
        accion: 'UPDATE',
        tablaM: 'contenido',
        registro_afectado_id: 504
    },
    {
        id: 3,
        fecha: '2025-09-07 15:00:10',
        usuario_id: 5, // Editor
        accion: 'UPDATE',
        tablaM: 'contenido',
        registro_afectado_id: 503
    },
    {
        id: 4,
        fecha: '2025-09-07 14:10:05',
        usuario_id: 3, // Programador
        accion: 'DELETE',
        tablaM: 'segmentos',
        registro_afectado_id: 88
    },
    {
        id: 5,
        fecha: '2025-09-06 18:00:00',
        usuario_id: 2, // Admin
        accion: 'INSERT',
        tablaM: 'empleados',
        registro_afectado_id: 25
    },
    {
        id: 6,
        fecha: '2025-09-06 17:55:12',
        usuario_id: 2, // Admin
        accion: 'UPDATE',
        tablaM: 'roles',
        registro_afectado_id: 3 // Programador
    }
];

// Componente de Fila de Auditoría
const AuditRow = ({ log }) => {
    return (
        // DIV con scroll horizontal INDIVIDUAL
        <div className="auditoria-row-scroll-wrapper">
            <div className="auditoria-row-content">
                <div className="auditoria-cell fecha">{log.fecha}</div>
                <div className="auditoria-cell usuario">{log.usuario_id}</div>
                <div className="auditoria-cell accion">{log.accion}</div>
                <div className="auditoria-cell tabla">{log.tablaM}</div>
                <div className="auditoria-cell registro">{log.registro_afectado_id}</div>
            </div>
        </div>
    );
};

// Componente Principal
export default function Auditoria() {
    return (
        <div className="auditoria-container">
            
            {/* TÍTULO */}
            <header className="auditoria-header">
                <h1>Registro de Auditoría</h1>
            </header>

            {/* 1. Wrapper para el scroll-x de la cabecera */}
            <div className="auditoria-header-scroll-wrapper">
                <div className="auditoria-list-header-content">
                    <div className="auditoria-cell fecha">Fecha y Hora</div>
                    <div className="auditoria-cell usuario">Usuario ID</div>
                    <div className="auditoria-cell accion">Acción</div>
                    <div className="auditoria-cell tabla">Tabla Modificada</div>
                    <div className="auditoria-cell registro">Registro ID</div>
                </div>
            </div>

            {/* 2. DIV con SCROLL VERTICAL para toda la lista */}
            <div className="auditoria-list-container">
                {MOCK_AUDITORIA_DATA.map((log) => (
                    <AuditRow key={log.id} log={log} />
                ))}
            </div>
        </div>
    );
}