import React, { useState, useEffect } from 'react';
import axios from 'axios';
// Importamos los íconos (igual que en Programas)
import { Edit, Trash2 } from 'lucide-react'; 

import ABMPageLayout from '../../../components/abm/ABMPageLayout.jsx'; 
import ABMEmpleadosAddForm from './ABMEmpleadosAddForm.jsx';
import ABMEmpleadosEditForm from './ABMEmpleadosEditForm.jsx';
import '../../../styles/components/abmForm.css';

// --- 1. DEFINICIÓN DE COLUMNAS (DENTRO DE UNA FUNCIÓN) ---
// Ahora recibe 'onEdit' y 'onDelete' para conectar los botones
const getColumnasEmpleados = (onEdit, onDelete) => [
    { key: 'id', header: 'ID' },
    { key: 'nombre', header: 'Empleado' },
    { key: 'email', header: 'Correo' },
    { key: 'nombreRol', header: 'Cargo' },
    { 
        key: 'permisos', 
        header: 'Permisos',
        render: (rowData) => rowData.permisos.join(', ') 
    },
    // Columna EDITAR
    {
        key: 'editar',
        header: 'Editar',
        className: 'abm-columna-accion',
        render: (item) => (
            <button 
                onClick={() => onEdit(item.id)} 
                className="btn-accion btn-editar"
                aria-label={`Editar ${item.nombre}`}
            >
                <Edit size={18} />
            </button>
        )
    },
    // Columna ELIMINAR
    {
        key: 'eliminar',
        header: 'Eliminar',
        className: 'abm-columna-accion',
        render: (item) => (
            <button 
                onClick={() => onDelete(item.id)} 
                className="btn-accion btn-eliminar"
                aria-label={`Eliminar ${item.nombre}`}
            >
                <Trash2 size={18} />
            </button>
        )
    }
];

export default function ABMEmpleados() {
    const [editingId, setEditingId] = useState(null); 
    const [empleados, setEmpleados] = useState([]); 
    const [loading, setLoading] = useState(true);

    // URL Base de la API
    const API_URL = 'http://localhost:8080/api/usuarios';

    // --- Cargar Datos (GET) ---
    const cargarEmpleados = () => {
        setLoading(true);
        axios.get(API_URL)
            .then(response => {
                setEmpleados(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error al cargar empleados:', error);
                setLoading(false);
            });
    };

    useEffect(() => {
        cargarEmpleados();
    }, []);

    const handleAdd = () => {
        setEditingId(0); 
    };

    const handleEdit = (id) => {
        setEditingId(id);
    };

    // --- Lógica para BORRAR (DELETE) ---
    const handleDelete = (id) => {
        if (window.confirm(`¿Seguro que deseas eliminar al empleado con ID: ${id}?`)) {
            // Llamada a la API para borrar
            axios.delete(`${API_URL}/${id}`)
                .then(() => {
                    alert('Empleado eliminado correctamente.');
                    // Recargamos la lista para que desaparezca de la tabla
                    cargarEmpleados(); 
                })
                .catch(error => {
                    console.error('Error al eliminar:', error);
                    alert('No se pudo eliminar el empleado.');
                });
        }
    };

    const handleCancelOrSuccess = () => {
        setEditingId(null); 
        cargarEmpleados(); // Recargamos la lista al volver de editar/agregar
    };
    
    // --- Renderizado Condicional ---

    if (editingId === 0) {
        return (
            <ABMEmpleadosAddForm 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
            />
        );
    }

    if (editingId !== null) {
        return (
            <ABMEmpleadosEditForm 
                empleadoId={editingId} 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
                initialData={empleados.find(e => e.id === editingId)}
            />
        );
    }
    
    // --- Generamos las columnas con las funciones ---
    const columnas = getColumnasEmpleados(handleEdit, handleDelete);

    return (
        <ABMPageLayout
            title="ABM de Empleados"
            columns={columnas} // Pasamos las columnas nuevas
            data={empleados}
            isLoading={loading}
            onAdd={handleAdd} 
            // Ya no pasamos onEdit/onDelete acá porque están en las columnas
        />
    );
}