import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Edit, Trash2 } from 'lucide-react'; 

import ABMPageLayout from '../../../components/abm/ABMPageLayout.jsx';
import ABMPlataformasForm from './ABMPlataformasForm.jsx';

// Definición de columnas con botones
const getColumnasPlataformas = (onEdit, onDelete) => [
    { key: 'id', header: 'ID' },
    { key: 'nombre', header: 'Nombre Plataforma' },
    { key: 'tipo', header: 'Tipo' },
    // { key: 'estado', header: 'Estado' }, // Tu backend actual no tiene campo 'estado' en Plataforma, lo comento por ahora
    {
        key: 'editar',
        header: 'Editar',
        className: 'abm-columna-accion',
        render: (item) => (
            <button onClick={() => onEdit(item.id)} className="btn-accion btn-editar">
                <Edit size={18} />
            </button>
        )
    },
    {
        key: 'eliminar',
        header: 'Eliminar',
        className: 'abm-columna-accion',
        render: (item) => (
            <button onClick={() => onDelete(item.id)} className="btn-accion btn-eliminar">
                <Trash2 size={18} />
            </button>
        )
    }
];

export default function ABMPlataformas() {
    const [editingId, setEditingId] = useState(null);
    
    // Estados para la API
    const [plataformas, setPlataformas] = useState([]);
    const [loading, setLoading] = useState(true);

    // Cargar datos
    const cargarPlataformas = () => {
        setLoading(true);
        axios.get('http://localhost:8080/api/plataformas')
            .then(response => {
                setPlataformas(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error cargando plataformas:", error);
                setLoading(false);
            });
    };

    useEffect(() => {
        cargarPlataformas();
    }, []);
    
    const handleAdd = () => setEditingId(0);
    const handleEdit = (id) => setEditingId(id);

    // Borrar Plataforma
    const handleDelete = async (id) => {
        if (window.confirm(`¿Seguro que deseas eliminar la plataforma ID: ${id}?`)) {
            try {
                const response = await axios.delete(`http://localhost:8080/api/plataformas/${id}`);
                
                if (response.data === "Plataforma borrada con éxito.") {
                    alert("Plataforma eliminada.");
                    cargarPlataformas();
                } else {
                    alert("No se pudo eliminar: " + response.data);
                }
            } catch (error) {
                console.error("Error al eliminar:", error);
                alert("Ocurrió un error de conexión.");
            }
        }
    };

    const handleCancelOrSuccess = () => {
        setEditingId(null);
        cargarPlataformas(); // Recargar lista al volver
    };

    // Renderizado del Formulario (Edición/Creación)
    if (editingId !== null) {
        return (
            <ABMPlataformasForm 
                plataformaId={editingId} 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
            />
        );
    }
    
    const columnas = getColumnasPlataformas(handleEdit, handleDelete);

    return (
        <ABMPageLayout
            title="ABM de Plataformas"
            columns={columnas}
            data={plataformas}
            isLoading={loading}
            onAdd={handleAdd}
        />
    );
}