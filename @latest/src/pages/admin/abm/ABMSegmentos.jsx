import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Edit, Trash2 } from 'lucide-react';

import ABMPageLayout from '../../../components/abm/ABMPageLayout.jsx';
import ABMSegmentosForm from './ABMSegmentosForm.jsx';

// Definición de columnas
const getColumnasSegmentos = (onEdit, onDelete) => [
    { key: 'id', header: 'ID' },
    // Nota: El backend devuelve 'nombrePrograma', no 'programa'
    { key: 'nombrePrograma', header: 'Programa' }, 
    { key: 'titulo', header: 'Nombre Segmento' }, // 'titulo' es el nombre en tu DTO
    { key: 'duracion', header: 'Duración (min)' },
    // { key: 'orden', header: 'Orden' }, // Si decidís agregarlo al DTO de lista
    { key: 'estadoAprobacion', header: 'Estado' },
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

export default function ABMSegmentos() {
    const [editingId, setEditingId] = useState(null);
    const [segmentos, setSegmentos] = useState([]);
    const [loading, setLoading] = useState(true);

    // Cargar datos
    const cargarSegmentos = () => {
        setLoading(true);
        axios.get('http://localhost:8080/api/segmentos')
            .then(response => {
                setSegmentos(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error cargando segmentos:", error);
                setLoading(false);
            });
    };

    useEffect(() => {
        cargarSegmentos();
    }, []);
    
    const handleAdd = () => setEditingId(0);
    const handleEdit = (id) => setEditingId(id);

    // Borrar Segmento
    const handleDelete = async (id) => {
        if (window.confirm(`¿Seguro que deseas eliminar el segmento ID: ${id}?`)) {
            try {
                const response = await axios.delete(`http://localhost:8080/api/segmentos/${id}`);
                
                if (response.data === "Segmento borrado con éxito.") {
                    alert("Segmento eliminado.");
                    cargarSegmentos();
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
        cargarSegmentos(); 
    };

    if (editingId !== null) {
        return (
            <ABMSegmentosForm 
                segmentoId={editingId} 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
            />
        );
    }
    
    const columnas = getColumnasSegmentos(handleEdit, handleDelete);

    return (
        <ABMPageLayout
            title="ABM de Segmentos"
            columns={columnas}
            data={segmentos}
            isLoading={loading}
            onAdd={handleAdd}
        />
    );
}