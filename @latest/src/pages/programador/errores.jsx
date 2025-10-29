// src/pages/programador/errores.jsx (Restaurado + Modificado)

import React from 'react';
import '../../styles/pages/errores.css'; 

// Datos de ejemplo
const MOCK_ERRORES_DATA = [
    {
        id: 1,
        fecha: '2025-09-08',
        tipo: 'Error de subida',
        mensaje: 'Error 404: Error en la entrega del mensaje de texto. Verifique su conexión de red y vuelva a intentarlo.'
    },
    {
        id: 2,
        fecha: '2025-09-08',
        tipo: 'Error',
        mensaje: "Error: Error evaluating field 'date_converted': The problem was located in calculate expression for /test_date/ date_converted cannot convert '1980-2-30' to a date."
    },
    {
        id: 3,
        fecha: '2025-09-07',
        tipo: 'Advertencia',
        mensaje: 'El archivo "spot_navidad.mp4" excede el tamaño recomendado de 500MB.'
    },
    {
        id: 4,
        fecha: '2025-09-06',
        tipo: 'Error de transcodificación',
        mensaje: 'No se pudo procesar el audio del archivo "podcast_ep12.wav". Formato no compatible.'
    },
    {
        id: 5,
        fecha: '2025-09-05',
        tipo: 'Error',
        mensaje: 'NullPointerException: No se pudo encontrar el ID de usuario al procesar el segmento 45.'
    },
    {
        id: 6,
        fecha: '2025-09-04',
        tipo: 'Advertencia',
        mensaje: 'El contenido "resumen_semanal.mov" tiene una duración de 00:00. Verifique el archivo.'
    }
];

// Componente de Fila de Error (con su propio scroll-x)
const ErrorRow = ({ error }) => {
    const isError = error.tipo.toLowerCase().includes('error');
    const tagClass = isError ? 'tipo-error' : 'tipo-advertencia';

    return (
        // 3. DIV con scroll horizontal INDIVIDUAL (restaurado)
        <div className="error-row-scroll-wrapper">
            <div className="error-row-content">
                <div className="error-cell fecha">{error.fecha}</div>
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
                <h1>Errores y contenidos faltantes</h1>
            </header>

            {/* 1. NUEVO Wrapper para el scroll-x de la cabecera */}
            <div className="header-scroll-wrapper">
                <div className="errores-list-header-content">
                    <div className="error-cell fecha">Fecha</div>
                    <div className="error-cell tipo">Tipo</div>
                    <div className="error-cell mensaje">Mensaje</div>
                </div>
            </div>

            {/* 2. DIV con SCROLL VERTICAL para toda la lista (restaurado) */}
            <div className="errores-list-container">
                {MOCK_ERRORES_DATA.map((error) => (
                    <ErrorRow key={error.id} error={error} />
                ))}
            </div>
        </div>
    );
}
