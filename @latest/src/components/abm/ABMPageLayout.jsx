// src/components/abm/ABMPageLayout.jsx

import React from 'react';
import { Plus, ArrowLeft } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import ABMTable from './ABMTable';
import '../../styles/components/abmTable.css'; 

export default function ABMPageLayout({ title, columns, data, onAdd, onEdit, onDelete }) {
    
    const navigate = useNavigate();
    const entityName = title.replace('ABM de ', '').slice(0, -1);

    return (
        <div className="abm-container">
            {/* --- BOTÓN DE VOLVER --- */}
            <button onClick={() => navigate(-1)} className="btn-volver">
                <ArrowLeft size={20} />
                Volver
            </button>

            {/* --- HEADER (SOLO TÍTULO) --- */}
            <header className="abm-page-header">
                <h1><span>{title}</span></h1>
            </header>

            {/* 👇 CONTENEDOR DEL BOTÓN 'AÑADIR' CON COMPROBACIÓN 👇 */}
            {/* Esto significa: "Solo muestra este bloque si 'onAdd' existe 
              (es decir, si la página pasó la prop onAdd)" 
            */}
            {onAdd && (
                <div className="abm-actions-header">
                    <button 
                        className="btn-anadir" 
                        onClick={onAdd}
                        title={`Añadir ${entityName}`}
                    >
                        <Plus size={20} />
                        Añadir
                    </button>
                </div>
            )}
            {/* 👆 FIN DEL BLOQUE DEL BOTÓN 👆 */}

            {/* --- TABLA --- */}
            <ABMTable 
                columns={columns} 
                data={data} 
                onEdit={onEdit} 
                onDelete={onDelete} 
            />
        </div>
    );
}