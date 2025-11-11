// src/pages/programador/errores.jsx (Actualizado al nuevo schema y estilo)

import React from 'react';
import '../../styles/pages/errores.css'; // Mantiene el mismo CSS, pero lo actualizaremos

// Datos de ejemplo basados en la nueva tabla 'errores'
const MOCK_ERRORES_DATA = [
    {
        id: 1,
        fechaYHora: '2025-09-08 10:30:15',
        idUsuario: 5, // ID del Editor/Productor
        tipo: 'Error de subida',
        mensaje: 'Error 404: Error en la entrega del mensaje de texto. Verifique su conexión de red y vuelva a intentarlo.'
    },
    {
        id: 2,
        fechaYHora: '2025-09-08 09:15:00',
        idUsuario: 3, // ID del Programador
        tipo: 'Error',
        mensaje: "Error evaluating field 'date_converted': The problem was located in calculate expression for /test_date/ date_converted cannot convert '1980-2-30' to a date."
    },
    {
        id: 3,
        fechaYHora: '2025-09-07 18:00:00',
        idUsuario: 5,
        tipo: 'Advertencia',
        mensaje: 'El archivo "spot_navidad.mp4" excede el tamaño recomendado de 500MB.'
    },
    {
        id: 4,
        fechaYHora: '2025-09-06 14:20:10',
        idUsuario: 2, // ID del Admin
        tipo: 'Error de transcodificación',
        mensaje: 'No se pudo procesar el audio del archivo "podcast_ep12.wav". Formato no compatible.'
    },
    {
        id: 5,
        fechaYHora: '2025-09-05 11:05:30',
        idUsuario: 3,
        tipo: 'Error',
        mensaje: 'NullPointerException: No se pudo encontrar el ID de usuario al procesar el segmento 45.'
    },
];

// Componente de Fila de Error (con su propio scroll-x)
const ErrorRow = ({ error }) => {
    // Lógica para los tags de color
    const isError = error.tipo.toLowerCase().includes('error');
    const tagClass = isError ? 'tipo-error' : 'tipo-advertencia';

    return (
        // DIV con scroll horizontal INDIVIDUAL
        <div className="error-row-scroll-wrapper">
            <div className="error-row-content">
                <div className="error-cell fecha">{error.fechaYHora}</div>
                <div className="error-cell usuario">{error.idUsuario}</div>
                <div className="error-cell tipo">
                    <span className={`tipo-tag ${tagClass}`}>
                        {error.tipo}
                    </span>
                </div>
                <div className="error-cell mensaje">
                    {error.mensaje}
                </div>
            </div>
        </div>
    );
};

// Componente Principal
export default function Errores() {
    return (
        <div className="errores-container">
            
            {/* TÍTULO */}
            <header className="errores-header">
                <h1>Registro de Errores</h1>
            </header>

            {/* 1. Wrapper para el scroll-x de la cabecera */}
            <div className="errores-header-scroll-wrapper">
                <div className="errores-list-header-content">
                    <div className="error-cell fecha">Fecha y Hora</div>
                    <div className="error-cell usuario">Usuario ID</div>
                    <div className="error-cell tipo">Tipo</div>
                    <div className="error-cell mensaje">Mensaje</div>
                </div>
            </div>

            {/* 2. DIV con SCROLL VERTICAL para toda la lista */}
            <div className="errores-list-container">
                {MOCK_ERRORES_DATA.map((error) => (
                    <ErrorRow key={error.id} error={error} />
                ))}
            </div>
        </div>
    );
}