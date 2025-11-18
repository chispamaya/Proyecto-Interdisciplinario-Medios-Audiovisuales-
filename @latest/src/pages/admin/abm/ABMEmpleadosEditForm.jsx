import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';

export default function ABMEmpleadosEditForm({ empleadoId, onCancel, onSuccess, initialData }) {
    
    const [idRolSeleccionado, setIdRolSeleccionado] = useState('');
    const [listaRoles, setListaRoles] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/roles')
            .then(res => {
                const roles = res.data;
                setListaRoles(roles);
                
                // Pre-seleccionamos el rol actual buscando por nombre
                // (Nota: Como hay roles con el mismo nombre, esto seleccionará el primero que encuentre.
                // Si quisieras ser exacto, necesitarías que el 'initialData' trajera el idRol también).
                const rolActual = roles.find(r => r.nombre === initialData?.nombreRol);
                if (rolActual) {
                    setIdRolSeleccionado(rolActual.id);
                }
            })
            .catch(err => console.error("Error cargando roles:", err));
    }, [initialData]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const datosParaEditar = {
            idUsuario: empleadoId,
            idNuevoRol: parseInt(idRolSeleccionado)
        };

        try {
            await axios.put('http://localhost:8080/api/usuarios/rol', datosParaEditar);
            alert(`Rol de empleado actualizado correctamente.`);
            if (onSuccess) onSuccess();
        } catch (error) {
            console.error("Error al actualizar:", error);
            alert("No se pudo actualizar el rol.");
        }
    };
    
    const handleDelete = async () => {
        if (window.confirm(`¿Estás seguro de que deseas ELIMINAR a ${initialData.nombre}?`)) {
            try {
                // Guardamos la respuesta en una variable
                const response = await axios.delete(`http://localhost:8080/api/usuarios/${empleadoId}`);
                
                // 💥 CAMBIO CLAVE: Verificamos qué nos respondió la base de datos
                if (response.data === "Usuario borrado con éxito.") {
                    alert("Empleado eliminado correctamente.");
                    if (onSuccess) onSuccess();
                } else {
                    // Si la DB dijo "Ocurrio un error" u otra cosa
                    alert("No se pudo eliminar: " + response.data);
                    console.error("Respuesta del servidor:", response.data);
                }

            } catch (error) {
                console.error("Error al eliminar:", error);
                alert("Error de conexión al intentar eliminar.");
            }
        }
    };

    return (
        <ABMFormLayout
            title={`Editar datos de empleado: ${initialData?.nombre}`}
            onSubmit={handleSubmit}
            onDiscard={onCancel}
        >
            <h2 className="form-subtitle">Datos actuales (Solo lectura)</h2>
            <div className="empleado-info-table">
                <div className="empleado-info-row">
                    <span><strong>Email:</strong> {initialData?.email}</span>
                    <span><strong>Cargo actual:</strong> {initialData?.nombreRol}</span>
                </div>
                <div className="empleado-info-row">
                    <span><strong>Permisos actuales:</strong> {initialData?.permisos?.join(', ')}</span>
                </div>
            </div>
            
            <div className="form-group">
                <label htmlFor="cargo">Nuevo Cargo (Rol)</label>
                <select 
                    id="cargo" 
                    name="cargo" 
                    value={idRolSeleccionado} 
                    onChange={(e) => setIdRolSeleccionado(e.target.value)} 
                    required
                >
                    <option value="" disabled>Seleccione un rol</option>
                    
                    {/* 💥 AQUÍ ESTÁ EL CAMBIO VISUAL 💥 */}
                    {listaRoles.map(rol => (
                        <option key={rol.id} value={rol.id}>
                             {rol.nombre} {rol.detallesPermisos && rol.detallesPermisos.length > 0 
                                ? ` - [${rol.detallesPermisos.join(', ')}]` 
                                : ''}
                        </option>
                    ))}

                </select>
                <small style={{display:'block', marginTop:'5px', color:'#888'}}>
                    * Seleccioná el rol específico para asignar la combinación de permisos deseada.
                </small>
            </div>

            <div className="eliminar-empleado-container">
                <button type="button" className="btn-eliminar-empleado" onClick={handleDelete}>
                    Eliminar empleado
                </button>
            </div>
        </ABMFormLayout>
    );
}