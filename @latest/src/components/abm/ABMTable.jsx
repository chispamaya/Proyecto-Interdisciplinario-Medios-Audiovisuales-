import React from 'react';

/**
 * Componente de tabla reutilizable para las páginas ABM.
 * Ahora puede renderizar contenido personalizado (como botones) si se le pasa
 * una función 'render' en la definición de la columna.
 * * También aplica una 'className' al <th> y <td> si se provee en la
 * definición de la columna, para poder centrar.
 */
export default function ABMTable({ columns, data }) {

    return (
        <div className="tabla-gestion-wrapper">
            <table className="tabla-gestion abm-table">
                <thead>
                    <tr>
                        {columns.map((col, index) => (
                            // Aplica la clase de la columna al <th>
                            <th 
                                key={col.key || col.header || index} 
                                className={col.className || ''}
                            >
                                {col.header}
                            </th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {data.length > 0 ? (
                        data.map((item) => (
                            <tr key={item.id}>
                                
                                {columns.map((col, index) => (
                                    // Aplica la clase de la columna al <td>
                                    <td 
                                        key={col.key || col.header || index} 
                                        data-label={col.header} 
                                        className={col.className || ''}
                                    >
                                        
                                        {/* Si hay 'render', lo usa. Si no, muestra el dato. */}
                                        {col.render ? col.render(item) : item[col.key]}

                                    </td>
                                ))}
                                
                            </tr>
                        ))
                    ) : (
                        <tr>
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