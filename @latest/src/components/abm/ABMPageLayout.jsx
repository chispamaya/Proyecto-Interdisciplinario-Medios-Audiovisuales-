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
            {/* 💥 BOTÓN DE VOLVER AÑADIDO 💥 */}
            <button onClick={() => navigate(-1)} className="btn-volver">
                <ArrowLeft size={20} />
                Volver
            </button>

            <header className="abm-page-header">
                <h1><span>{title}</span></h1>
                <button 
                    className="btn-anadir" 
                    onClick={onAdd}
                    title={`Añadir ${entityName}`}
                >
                    <Plus size={20} />
                    Añadir
                </button>
            </header>

            <ABMTable 
                columns={columns} 
                data={data} 
                onEdit={onEdit} 
                onDelete={onDelete} 
            />
        </div>
    );
}