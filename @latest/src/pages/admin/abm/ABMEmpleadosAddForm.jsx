import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';

export default function ABMEmpleadosAddForm({ onCancel, onSuccess }) {
    
    // Estado para los datos del formulario
    const [formData, setFormData] = useState({
        nombre: '', 
        apellido: '', 
        contrasenia: '', 
        email: '', 
        idRol: '' 
    });

    // Estado para la lista de roles (para llenar el select)
    const [listaRoles, setListaRoles] = useState([]);

    // 1. Cargar los roles desde la API al iniciar
    useEffect(() => {
        axios.get('http://localhost:8080/api/roles')
            .then(res => {
                setListaRoles(res.data);
            })
            .catch(err => console.error("Error cargando roles:", err));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const nuevoUsuario = {
            nombre: `${formData.nombre} ${formData.apellido}`.trim(), 
            email: formData.email,
            contrasenia: formData.contrasenia,
            idRol: parseInt(formData.idRol)
        };

        console.log("Enviando datos:", nuevoUsuario); // Para ver qué mandamos

        try {
            const response = await axios.post('http://localhost:8080/api/usuarios', nuevoUsuario);
            
            // 💥 AQUÍ ESTÁ EL CAMBIO CLAVE 💥
            // Verificamos si el mensaje es el de éxito esperado
            if (response.data === "Usuario ingresado con éxito.") {
                alert('¡Empleado agregado con éxito!');
                if (onSuccess) onSuccess();
            } else {
                // Si la DB devolvió "Ocurrio un error" u otra cosa
                console.error("Respuesta del servidor:", response.data);
                alert("No se pudo agregar: " + response.data + "\n(Revisa la tabla 'errores' en la DB para más detalles)");
            }
            
        } catch (error) {
            console.error("Error de conexión:", error);
            alert("Error de conexión con el servidor.");
        }
    };

    return (
        <ABMFormLayout
            title="Agregar un empleado"
            onSubmit={handleSubmit}
            onDiscard={onCancel}
        >
            <div className="form-group">
                <label htmlFor="nombre">Nombre</label>
                <input 
                    id="nombre" 
                    name="nombre" 
                    type="text" 
                    value={formData.nombre} 
                    onChange={handleChange} 
                    required 
                />
            </div>
            
            <div className="form-group">
                <label htmlFor="apellido">Apellido</label>
                <input 
                    id="apellido" 
                    name="apellido" 
                    type="text" 
                    value={formData.apellido} 
                    onChange={handleChange} 
                    required 
                />
            </div>
            
            <div className="form-group">
                <label htmlFor="contrasenia">Contraseña</label>
                <input 
                    id="contrasenia" 
                    name="contrasenia" 
                    type="password" 
                    value={formData.contrasenia} 
                    onChange={handleChange} 
                    required 
                />
            </div>
            
            <div className="form-group">
                <label htmlFor="email">Email</label>
                <input 
                    id="email" 
                    name="email" 
                    type="email" 
                    value={formData.email} 
                    onChange={handleChange} 
                    required 
                />
            </div>
            
            <div className="form-group">
                <label htmlFor="idRol">Cargo (Rol)</label>
                <select 
                    id="idRol" 
                    name="idRol" 
                    value={formData.idRol} 
                    onChange={handleChange} 
                    required
                >
                    <option value="" disabled>Seleccione un cargo</option>
                    
                    {/* Aquí mostramos el Nombre del Rol Y sus Permisos 
                        para que sepas cuál elegir.
                    */}
                    {listaRoles.map(rol => (
                        <option key={rol.id} value={rol.id}>
                            {rol.nombre} 
                            {rol.detallesPermisos && rol.detallesPermisos.length > 0 
                                ? ` - [${rol.detallesPermisos.join(', ')}]` 
                                : ''}
                        </option>
                    ))}
                </select>
            </div>
        </ABMFormLayout>
    );
}