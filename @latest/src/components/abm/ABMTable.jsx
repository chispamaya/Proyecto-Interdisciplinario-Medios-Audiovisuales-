import React from 'react';
import { Edit, Trash2 } from 'lucide-react'; 

/**
 * Componente de tabla reutilizable para las páginas ABM.
 * @param {Array} columns - Definición de las columnas: [{ key: 'id', header: 'ID' }, ...]
 * @param {Array} data - Los datos a mostrar en la tabla.
 * @param {function} onEdit - Callback para la acción de editar.
 * @param {function} onDelete - Callback para la acción de eliminar.
 */
export default function ABMTable({ columns, data, onEdit, onDelete }) {

    return (
        // Utilizamos las clases de tabla responsive definidas en gestionMultimedia.css
        <div className="tabla-gestion-wrapper">
            <table className="tabla-gestion abm-table">
                <thead>
                    <tr>
                        {columns.map(col => (
                            <th key={col.key}>{col.header}</th>
                        ))}
                        <th>Acciones</th> 
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
                                
                                {/* Celda de Acciones */}
                                <td data-label="Acciones" className="actions-cell">
                                    <button
                                        className="btn-accion-tabla btn-editar"
                                        // Ejecuta la función onEdit con el ID del elemento
                                        onClick={() => onEdit(item.id)} 
                                        title="Editar"
                                    >
                                        <Edit size={18} />
                                    </button>
                                    <button
                                        className="btn-accion-tabla btn-eliminar"
                                        onClick={() => onDelete(item.id)}
                                        title="Eliminar"
                                    >
                                        <Trash2 size={18} />
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            {/* Muestra un mensaje si no hay datos. El colSpan es para que ocupe todo el ancho. */}
                            <td colSpan={columns.length + 1} className="no-data-cell">
                                No hay datos para mostrar.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
}