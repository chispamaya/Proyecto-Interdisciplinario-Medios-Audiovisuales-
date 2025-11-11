import React from 'react';
// Ya no necesitamos 'Edit' ni 'Trash2' porque sacamos los botones
// import { Edit, Trash2 } from 'lucide-react'; 

/**
 * Componente de tabla reutilizable para las páginas ABM.
 * @param {Array} columns - Definición de las columnas: [{ key: 'id', header: 'ID' }, ...]
 * @param {Array} data - Los datos a mostrar en la tabla.
 *
 * (Se quitaron onEdit y onDelete porque la columna de Acciones fue removida)
 */
export default function ABMTable({ columns, data }) {

    return (
        // Utilizamos las clases de tabla responsive definidas en gestionMultimedia.css
        <div className="tabla-gestion-wrapper">
            <table className="tabla-gestion abm-table">
                <thead>
                    <tr>
                        {columns.map(col => (
                            <th key={col.key}>{col.header}</th>
                        ))}
                        {/* <th>Acciones</th> <-- COLUMNA ELIMINADA */}
                    </tr>
                </thead>
                <tbody>
                    {data.length > 0 ? (
                        data.map((item) => (
                            <tr key={item.id}>
                                
                                {/* Renderiza las celdas de datos, usando data-label para responsive */}
                                {columns.map(col => (
                                    <td key={col.key} data-label={col.header}>
                                        {item[col.key]}
                                    </td>
                                ))}
                                
                                {/* CELDA DE ACCIONES ELIMINADA */}
                                {/* <td data-label="Acciones" className="actions-cell">
                                    ...botones...
                                </td> 
                                */}
                            </tr>
                        ))
                    ) : (
                        <tr>
                            {/* CAMBIO: El colSpan ahora es solo 'columns.length' porque quitamos 1 columna */}
                            <td colSpan={columns.length} className="no-data-cell">
                                No hay datos para mostrar.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
}