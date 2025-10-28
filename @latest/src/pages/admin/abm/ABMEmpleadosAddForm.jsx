import React, { useState } from 'react';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';
export default function ABMEmpleadosAddForm({ onCancel, onSuccess }) {
    const [formData, setFormData] = useState({
        nombre: '', apellido: '', contrasenia: '', email: '', cargo: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Agregando empleado:', formData);
        alert('Empleado agregado con éxito!');
        onSuccess && onSuccess();
    };

    return (
        <ABMFormLayout
            title="Agregar un empleado"
            onSubmit={handleSubmit}
            onDiscard={onCancel}
        >
            <div className="form-group">
                <label htmlFor="nombre">Nombre</label>
                <input id="nombre" name="nombre" type="text" value={formData.nombre} onChange={handleChange} required />
            </div>
            <div className="form-group">
                <label htmlFor="apellido">Apellido</label>
                <input id="apellido" name="apellido" type="text" value={formData.apellido} onChange={handleChange} required />
            </div>
            <div className="form-group">
                <label htmlFor="contrasenia">Contraseña</label>
                <input id="contrasenia" name="contrasenia" type="password" value={formData.contrasenia} onChange={handleChange} required />
            </div>
            <div className="form-group">
                <label htmlFor="email">Email</label>
                <input id="email" name="email" type="email" value={formData.email} onChange={handleChange} required />
            </div>
            <div className="form-group">
                <label htmlFor="cargo">Cargo</label>
                <select id="cargo" name="cargo" value={formData.cargo} onChange={handleChange} required>
                    <option value="" disabled>Seleccione un cargo</option>
                    <option value="Admin">Administrador</option>
                    <option value="Editor">Editor</option>
                    <option value="Productor">Productor</option>
                </select>
            </div>
        </ABMFormLayout>
    );
}