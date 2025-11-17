import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
// Importa los iconos aquí
import { Edit, Trash2 } from 'lucide-react'; 
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


// Función que crea la definición de columnas
const getColumnasProgramas = (onEdit, onDelete) => [
    { key: 'id', header: 'ID' },
    { key: 'nombre', header: 'Nombre' },
    { key: 'duracion', header: 'Duración' },
    { key: 'categoria', header: 'Categoría' },
    { key: 'estado', header: 'Estado' },
    {
        key: 'editar',
        header: 'Editar',
        className: 'abm-columna-accion', // Clase para centrar
        render: (item) => (
            <button 
                onClick={() => onEdit(item.id)} 
                className="btn-accion btn-editar"
                aria-label={`Editar ${item.id}`}
            >
                <Edit size={18} />
            </button>
        )
    },
    {
        key: 'eliminar',
        header: 'Eliminar',
        className: 'abm-columna-accion', // Clase para centrar
        render: (item) => (
            <button 
                onClick={() => onDelete(item.id)} 
                className="btn-accion btn-eliminar"
                aria-label={`Eliminar ${item.id}`}
            >
                <Trash2 size={18} />
            </button>
        )
    }
];

export default function ABMProgramas() {
    const [editingId, setEditingId] = useState(null); 
    const navigate = useNavigate(); 
    
    const handleEdit = (id) => {
        setEditingId(id); 
    };

    const handleDelete = (id) => {
        if (window.confirm(`¿Seguro que deseas eliminar el programa ID: ${id}?`)) {
            console.log(`Eliminar programa ID: ${id}`);
        }
    };
    
    const handleAdd = () => {
        navigate('/subida'); // Sigue yendo a /subida
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
    
    // Genera las columnas llamando a la función
    const columnas = getColumnasProgramas(handleEdit, handleDelete);
    
    return (
        <ABMPageLayout
            title="ABM de Programas"
            columns={columnas} // Pasa las columnas listas
            data={programasData}
            onAdd={handleAdd} 
            // Ya no pasa onEdit/onDelete aquí
        />
    );
}