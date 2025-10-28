// src/components/abm/ABMPageLayout.jsx - MODIFICADO

import React from 'react';
import { Plus } from 'lucide-react';
import ABMTable from './ABMTable';
import '../../styles/components/abmTable.css'; 

export default function ABMPageLayout({ title, columns, data, onAdd, onEdit, onDelete }) {
    
    // Extraemos la entidad (ej: "Programas" de "ABM de Programas") para el tooltip.
    const entityName = title.replace('ABM de ', '').slice(0, -1);

    return (
        <div className="abm-container">
            {/* Cabecera con Título y Botón Añadir */}
            <header className="abm-page-header">
                <h1><span>{title}</span></h1>
                
                {/* 💥 MODIFICACIÓN: Cambiamos el texto del botón 💥 */}
                <button 
                    className="btn-anadir" 
                    onClick={onAdd}
                    title={`Añadir ${entityName}`} // Tooltip con el nombre completo
                >
                    <Plus size={20} />
                    Añadir {/* Texto simple y visible */}
                </button>
            </header>

            {/* Inyecta el componente de tabla reutilizable */}
            <ABMTable 
                columns={columns} 
                data={data} 
                onEdit={onEdit} 
                onDelete={onDelete} 
            />
        </div>
    );
}