import React, { useState } from 'react';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';
// --- Datos de Ejemplo (Sin cambios) ---
const plataformasData = [
    { id: 1, nombre: "Canal 7 TV", tipo: "TV", estado: "Activa" },
    { id: 2, nombre: "YouTube Live", tipo: "Online", estado: "Activa" },
    { id: 3, nombre: "Radio City 99.5", tipo: "Radio", estado: "Inactiva" },
    { id: 4, nombre: "Twitch Stream", tipo: "Online", estado: "Activa" },
    { id: 5, nombre: "TV Abierta Local", tipo: "TV", estado: "Activa" },
    { id: 6, nombre: "Spotify Podcast", tipo: "Online", estado: "Activa" },
    { id: 7, nombre: "Radio FM Clásicos", tipo: "Radio", estado: "Inactiva" },
    { id: 8, nombre: "Plataforma VOD Propia", tipo: "Online", estado: "Activa" },
];

const columnasPlataformas = [
    { key: 'id', header: 'ID' },
    { key: 'nombre', header: 'Nombre Plataforma' },
    { key: 'tipo', header: 'Tipo' },
    { key: 'estado', header: 'Estado' },
];

export default function ABMPlataformas() {
    const [editingId, setEditingId] = useState(null); 
    
    const handleEdit = (id) => setEditingId(id);
    const handleAdd = () => setEditingId(0);
    const handleCancelOrSuccess = () => setEditingId(null);

    if (editingId !== null) {
        return (
            <ABMPlataformasForm 
                plataformaId={editingId} 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
            />
        );
    }
    
    return (
        <ABMPageLayout
            title="ABM de Plataformas"
            columns={columnasPlataformas}
            data={plataformasData}
            onAdd={handleAdd}
            onEdit={handleEdit}
            onDelete={(id) => { 
                if (window.confirm(`¿Seguro que deseas eliminar la plataforma ID: ${id}?`)) {
                    console.log(`Eliminar plataforma ID: ${id}`);
                }
            }}
        />
    );
}