import React, { useState } from 'react';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
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
        setEditingId(0); 
    };

    const handleEdit = (id) => {
        setEditingId(id);
    };

    const handleCancelOrSuccess = () => {
        setEditingId(null); 
    };
    
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
                initialData={empleadosData.find(e => e.id === editingId)}
            />
        );
    }
    
    return (
        <ABMPageLayout
            title="ABM de Empleados"
            columns={columnasEmpleados}
            data={empleadosData}
            onAdd={handleAdd} 
            onEdit={handleEdit} 
            onDelete={() => { console.log("Usar formulario de edición para eliminar empleado."); }}
        />
    );
}