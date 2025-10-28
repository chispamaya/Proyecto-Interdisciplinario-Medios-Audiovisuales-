import React, { useState } from 'react';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';
const ALL_PERMISSIONS = ['Crear', 'Editar', 'Subir', 'Aprobar', 'Eliminar'];

export default function ABMEmpleadosEditForm({ empleadoId, onCancel, onSuccess, initialData }) {
    
    const [cargo, setCargo] = useState(initialData?.cargo || 'Editor');
    const [permisos, setPermisos] = useState(initialData?.permisos.split(', ').filter(p => p !== 'Full') || ['Crear', 'Editar']);

    const handlePermisoToggle = (permiso) => {
        setPermisos(prev => 
            prev.includes(permiso) 
                ? prev.filter(p => p !== permiso)
                : [...prev, permiso]
        );
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const updatedPermissions = permisos.join(', ');
        console.log(`Actualizando empleado ID ${empleadoId}: Cargo: ${cargo}, Permisos: ${updatedPermissions}`);
        alert(`Empleado ID ${empleadoId} actualizado.`);
        onSuccess && onSuccess();
    };
    
    const handleDelete = () => {
        if (window.confirm(`¿Estás seguro de que deseas ELIMINAR a ${initialData.empleado}?`)) {
            console.log(`Eliminando empleado ID ${empleadoId}`);
            onSuccess && onSuccess();
        }
    };

    return (
        <ABMFormLayout
            title={`Editar datos de empleado: ${initialData?.empleado}`}
            onSubmit={handleSubmit}
            onDiscard={onCancel}
        >
            <h2 className="form-subtitle">Datos actuales</h2>
            <div className="empleado-info-table">
                <div className="empleado-info-row">
                    <span>Empleado: {initialData?.empleado}</span>
                    <span>Cargo actual: {initialData?.cargo}</span>
                    <span>Permisos actuales: {initialData?.permisos}</span>
                </div>
            </div>
            
            <div className="form-group">
                <label htmlFor="cargo">Cargo</label>
                <select id="cargo" name="cargo" value={cargo} onChange={(e) => setCargo(e.target.value)} required>
                    <option value="" disabled>Editar cargo</option>
                    <option value="Admin">Administrador</option>
                    <option value="Editor">Editor</option>
                    <option value="Productor">Productor</option>
                </select>
            </div>
            
            <div className="form-group">
                <label>Permisos</label>
                <div className="permisos-tag-container">
                    {ALL_PERMISSIONS.map(p => (
                        <button key={p} type="button" className={`permiso-tag ${permisos.includes(p) ? 'active' : ''}`} onClick={() => handlePermisoToggle(p)} >
                            {p}
                            {permisos.includes(p) && <span className="close-icon">❌</span>}
                        </button>
                    ))}
                </div>
            </div>

            <div className="eliminar-empleado-container">
                <button type="button" className="btn-eliminar-empleado" onClick={handleDelete}>
                    Eliminar empleado
                </button>
            </div>
        </ABMFormLayout>
    );
}