// src/pages/admin/ABMProgramasForm.jsx

import React, { useState, useEffect } from 'react';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';

export default function ABMProgramasForm({ programId, onCancel, onSuccess, initialData }) {
    
    // Estado local para los campos del formulario
    const [nombre, setNombre] = useState('');
    const [duracion, setDuracion] = useState('');
    const [categoria, setCategoria] = useState('');
    const [estado, setEstado] = useState('Activo');

    const isEditMode = programId !== 0;
    const title = isEditMode ? `Editar Programa (ID: ${programId})` : "Agregar Nuevo Programa";

    // Cargar datos iniciales si estamos en modo edición
    useEffect(() => {
        if (isEditMode && initialData) {
            setNombre(initialData.nombre || '');
            setDuracion(initialData.duracion || '');
            setCategoria(initialData.categoria || '');
            setEstado(initialData.estado || 'Activo');
        }
    }, [isEditMode, initialData]);

    const handleSubmit = (e) => {
        e.preventDefault();
        // Aquí iría la lógica para guardar (fetch, etc.)
        console.log("Datos guardados:", { id: programId, nombre, duracion, categoria, estado });
        onSuccess(); // Volvemos a la lista
    };

    return (
        <ABMFormLayout title={title} onSubmit={handleSubmit} onCancel={onCancel}>
            
            <div className="form-group">
                <label htmlFor="nombre">Nombre del Programa</label>
                <input 
                    type="text" 
                    id="nombre" 
                    value={nombre} 
                    onChange={(e) => setNombre(e.target.value)}
                    required 
                />
            </div>

            <div className="form-group">
                <label htmlFor="duracion">Duración (ej: "90 min")</label>
                <input 
                    type="text" 
                    id="duracion" 
                    value={duracion} 
                    onChange={(e) => setDuracion(e.target.value)} 
                />
            </div>

            <div className="form-group">
                <label htmlFor="categoria">Categoría</label>
                <input 
                    type="text" 
                    id="categoria" 
                    value={categoria} 
                    onChange={(e) => setCategoria(e.target.value)} 
                />
            </div>
            
            <div className="form-group">
                <label htmlFor="estado">Estado</label>
                <select id="estado" value={estado} onChange={(e) => setEstado(e.target.value)}>
                    <option value="Activo">Activo</option>
                    <option value="Inactivo">Inactivo</option>
                </select>
            </div>
            
        </ABMFormLayout>
    );
}