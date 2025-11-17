import React, { useState } from 'react';

// --- Importaciones Corregidas ---
// 1. El layout principal que te faltaba
import ABMPageLayout from '../../../components/abm/ABMPageLayout.jsx'; 
// 2. El formulario para agregar (asumiendo que está en la misma carpeta)
import ABMEmpleadosAddForm from './ABMEmpleadosAddForm.jsx';
// 3. El formulario para editar (asumiendo que está en la misma carpeta)
import ABMEmpleadosEditForm from './ABMEmpleadosEditForm.jsx';

// Nota: No estabas usando ABMFormLayout, así que lo quité.
// import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 

import '../../../styles/components/abmForm.css';

// --- Datos de Ejemplo (Sin cambios) ---
const empleadosData = [
    { id: 1, empleado: "Carlos Perez", correo: "carlos.perez@gmail.com", cargo: "Editor", permisos: "Crear, editar" },
    { id: 2, empleado: "Carlos Lopez", correo: "carlos.lopez@gmail.com", cargo: "Editor", permisos: "Crear, editar" },
    { id: 3, empleado: "Luis Garcia", correo: "luis.g@gmail.com", cargo: "Productor", permisos: "Crear, subir" },
    { id: 4, empleado: "Ana Torres", correo: "a.torres@gmail.com", cargo: "Admin", permisos: "Full" },
];

const columnasEmpleados = [
    { key: 'id', header: 'ID' },
    { key: 'empleado', header: 'Empleado' },
    { key: 'correo', header: 'Correo' },
    { key: 'cargo', header: 'Cargo' },
    { key: 'permisos', header: 'Permisos' },
];

export default function ABMEmpleados() {
    const [editingId, setEditingId] = useState(null); 
    
    const handleAdd = () => {
        setEditingId(0); // 0 significa "agregando nuevo"
    };

    const handleEdit = (id) => {
        setEditingId(id);
    };

    const handleCancelOrSuccess = () => {
        setEditingId(null); // Vuelve a la lista principal
    };
    
    // --- Renderizado Condicional ---

    // Estado 1: Creando un nuevo empleado
    if (editingId === 0) {
        return (
            <ABMEmpleadosAddForm 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
            />
        );
    }

    // Estado 2: Editando un empleado existente
    if (editingId !== null) {
        return (
            <ABMEmpleadosEditForm 
                empleadoId={editingId} 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
                initialData={empleadosData.find(e => e.id === editingId)}
            />
        );
    }
    
    // Estado 3: Mostrando la lista principal (Default)
    return (
        <ABMPageLayout
            title="ABM de Empleados"
            columns={columnasEmpleados}
            data={empleadosData}
            onAdd={handleAdd} 
            onEdit={handleEdit} 
            onDelete={() => { console.log("La eliminación se maneja desde el formulario de edición."); }}
        />
    );
}