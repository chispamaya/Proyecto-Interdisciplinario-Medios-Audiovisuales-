import React, { useState } from 'react';
import ABMPageLayout from '../../../components/abm/ABMPageLayout.jsx';
import ABMProgramasForm from './ABMProgramasForm.jsx';
// --- Datos de Ejemplo (Sin cambios) ---
const programasData = [
    { id: 1, nombre: "Noticias Matinales", duracion: "60 min", categoria: "Noticias", estado: "Activo" },
    { id: 2, nombre: "Cine en Casa", duracion: "120 min", categoria: "Entretenimiento", estado: "Inactivo" },
    { id: 3, nombre: "Deportes Hoy", duracion: "90 min", categoria: "Deportes", estado: "Activo" },
    { id: 4, nombre: "El Debate Político", duracion: "75 min", categoria: "Política", estado: "Activo" },
    { id: 5, nombre: "Recetas de Mamá", duracion: "30 min", categoria: "Cocina", estado: "Activo" },
    { id: 6, nombre: "Música Clásica", duracion: "45 min", categoria: "Cultura", estado: "Inactivo" },
    { id: 7, nombre: "Entrevistas Exclusivas", duracion: "60 min", categoria: "Noticias", estado: "Activo" },
    { id: 8, nombre: "Series Retro", duracion: "150 min", categoria: "Entretenimiento", estado: "Activo" },
    { id: 9, nombre: "Resumen Semanal", duracion: "40 min", categoria: "Noticias", estado: "Activo" },
];

const columnasProgramas = [
    { key: 'id', header: 'ID' },
    { key: 'nombre', header: 'Nombre' },
    { key: 'duracion', header: 'Duración' },
    { key: 'categoria', header: 'Categoría' },
    { key: 'estado', header: 'Estado' },
];

export default function ABMProgramas() {
    const [editingId, setEditingId] = useState(null); 
    
    const handleEdit = (id) => {
        setEditingId(id); 
    };
    
    const handleAdd = () => {
        setEditingId(0); 
    };

    const handleCancelOrSuccess = () => {
        setEditingId(null); 
    };
    
    if (editingId !== null) {
        return (
            <ABMProgramasForm 
                programId={editingId} 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
            />
        );
    }
    
    return (
        <ABMPageLayout
            title="ABM de Programas"
            columns={columnasProgramas}
            data={programasData}
            onAdd={handleAdd} 
            onEdit={handleEdit} 
            onDelete={(id) => { 
                if (window.confirm(`¿Seguro que deseas eliminar el programa ID: ${id}?`)) {
                    console.log(`Eliminar programa ID: ${id}`);
                }
            }}
        />
    );
}