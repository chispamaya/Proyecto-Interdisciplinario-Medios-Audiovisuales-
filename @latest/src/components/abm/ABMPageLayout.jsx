import React from 'react';
import ABMTable from './ABMTable';
import { Plus } from 'lucide-react'; 

export default function ABMPageLayout({ 
    title, 
    columns, // Recibe las columnas (que ya incluyen los botones)
    data, 
    onAdd, 
    // Ya no necesita recibir onEdit ni onDelete
    addButtonText = "Añadir"
}) {
    return (
        <div className="abm-page-layout">
            
            {/* Header con estilos para alinear el botón a la derecha */}
            <header 
                className="abm-header" 
                style={{ 
                    display: 'flex', 
                    justifyContent: 'space-between', 
                    alignItems: 'center',
                    width: '100%'
                }}
            >
                <h1>{title}</h1>
                {onAdd && (
                    <button onClick={onAdd} className="btn-primary btn-add">
                        <Plus size={20} />
                        {addButtonText}
                    </button>
                )}
            </header>
            
            <ABMTable 
                columns={columns} // Simplemente pasa las columnas
                data={data}
                // Ya no pasa onEdit/onDelete
            />
        </div>
    );
}