import React, { useState } from 'react';
import ABMPageLayout from '../../../components/abm/ABMPageLayout.jsx';
import ABMSegmentosForm from './ABMSegmentosForm.jsx';
// --- Datos de Ejemplo (Sin cambios) ---
const segmentosData = [
    { id: 1, programa: "Noticias Matinales", nombre: "Bloque Nacional", duracion: "20 min", orden: 1 },
    { id: 2, programa: "Noticias Matinales", nombre: "Bloque Internacional", duracion: "25 min", orden: 2 },
    { id: 3, programa: "Deportes Hoy", nombre: "Resumen Fútbol", duracion: "30 min", orden: 1 },
    { id: 4, programa: "Cine en Casa", nombre: "Inicio Película A", duracion: "15 min", orden: 1 },
    { id: 5, programa: "Cine en Casa", nombre: "Corte Publicitario", duracion: "10 min", orden: 2 },
    { id: 6, programa: "El Debate Político", nombre: "Introducción", duracion: "15 min", orden: 1 },
    { id: 7, programa: "El Debate Político", nombre: "Bloque Temático 1", duracion: "30 min", orden: 2 },
    { id: 8, programa: "Recetas de Mamá", nombre: "Preparación", duracion: "15 min", orden: 1 },
    { id: 9, programa: "Series Retro", nombre: "Episodio 1", duracion: "60 min", orden: 1 },
];

const columnasSegmentos = [
    { key: 'id', header: 'ID' },
    { key: 'programa', header: 'Programa' },
    { key: 'nombre', header: 'Nombre Segmento' },
    { key: 'duracion', header: 'Duración' },
    { key: 'orden', header: 'Orden' },
];

export default function ABMSegmentos() {
    const [editingId, setEditingId] = useState(null); 
    
    const handleEdit = (id) => setEditingId(id);
    const handleAdd = () => setEditingId(0);
    const handleCancelOrSuccess = () => setEditingId(null);

    if (editingId !== null) {
        return (
            <ABMSegmentosForm 
                segmentoId={editingId} 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
            />
        );
    }
    
    return (
        <ABMPageLayout
            title="ABM de Segmentos"
            columns={columnasSegmentos}
            data={segmentosData}
            onAdd={handleAdd}
            onEdit={handleEdit}
            onDelete={(id) => { 
                if (window.confirm(`¿Seguro que deseas eliminar el segmento ID: ${id}?`)) {
                    console.log(`Eliminar segmento ID: ${id}`);
                }
            }}
        />
    );
}