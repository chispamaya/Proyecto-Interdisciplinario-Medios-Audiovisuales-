// chispamaya/proyecto-interdisciplinario-medios-audiovisuales-/Proyecto-Interdisciplinario-Medios-Audiovisuales--Codigo-fuente/@latest/src/components/abm/ABMTable.jsx

import React from 'react';
// ❌ Ya no se importan 'Edit' ni 'Trash2'

/**
 * Componente de tabla reutilizable para las páginas ABM.
 * @param {Array} columns - Definición de las columnas: [{ key: 'id', header: 'ID' }, ...]
 * @param {Array} data - Los datos a mostrar en la tabla.
 * * ❌ Ya no recibe onEdit ni onDelete
 */
export default function ABMTable({ columns, data }) {

    // ❌ Ya no hay 'showActions', 'handleEdit', 'handleDelete'

    return (
        // Utilizamos las clases de tabla responsive definidas en gestionMultimedia.css
        <div className="tabla-gestion-wrapper">
            <table className="tabla-gestion abm-table">
                <thead>
                    <tr>
                        {columns.map(col => (
                            <th key={col.key}>{col.header}</th>
                        ))}
                        {/* ❌ Ya no hay cabecera "Acciones" */}
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
                                
                                {/* ❌ Ya no hay celda "Acciones" */}
                            </tr>
                        ))
                    ) : (
                        <tr>
                            {/* El colSpan vuelve a ser simple */}
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