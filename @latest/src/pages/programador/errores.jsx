// src/pages/Errores.jsx

import React from 'react';
import '../../styles/pages/errores.css'; // Crearemos este archivo CSS a continuación

// Datos de ejemplo, inspirados en tu imagen. En una app real, vendrían de una API.
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
    }
];

export default function Errores() {
    return (
        <div className="errores-container">
            {/* Título de la página */}
            <header className="errores-header">
                <h1>Errores y contenidos faltantes</h1>
            </header>

            {/* Envoltorio de la tabla para permitir el scroll horizontal */}
            <div className="tabla-errores-wrapper">
                {/* Reutilizamos la clase .tabla-gestion para mantener la consistencia visual */}
                <table className="tabla-gestion">
                    <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Tipo</th>
                            <th>Mensaje</th>
                        </tr>
                    </thead>
                    <tbody>
                        {MOCK_ERRORES_DATA.map((error) => (
                            <tr key={error.id}>
                                {/* Usamos data-label para la vista responsive */}
                                <td data-label="Fecha">{error.fecha}</td>
                                <td data-label="Tipo">
                                    {/* Aplicamos una clase especial para los errores para poder darles color */}
                                    <span className={`tipo-tag ${error.tipo.toLowerCase().includes('error') ? 'tipo-error' : 'tipo-advertencia'}`}>
                                        {error.tipo}
                                    </span>
                                </td>
                                <td data-label="Mensaje" className="mensaje-cell">{error.mensaje}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}