import React, { useState } from 'react';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';
export default function ABMPlataformasForm({ plataformaId, onCancel, onSuccess }) {
    const initialData = plataformaId 
        ? { tipo: 'TV', nombre: 'Canal 7 TV' } 
        : { tipo: '', nombre: '' };
    
    const [formData, setFormData] = useState(initialData);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Guardando datos de la plataforma:', formData);
        alert(`Plataforma ${plataformaId ? 'actualizada' : 'creada'} con éxito!`);
        onSuccess && onSuccess();
    };

    return (
        <ABMFormLayout
            title="Plataforma"
            onSubmit={handleSubmit}
            onDiscard={onCancel}
        >
            <div className="form-group">
                <label htmlFor="tipoPlataforma">Tipo de plataforma</label>
                <select id="tipoPlataforma" name="tipo" value={formData.tipo} onChange={handleChange} required >
                    <option value="" disabled>Ingrese nueva categoria</option>
                    <option value="TV">TV</option>
                    <option value="Online">Online</option>
                    <option value="Radio">Radio</option>
                </select>
            </div>
            <div className="form-group">
                <label htmlFor="nombrePlataforma">Nombre de plataforma</label>
                <select id="nombrePlataforma" name="nombre" value={formData.nombre} onChange={handleChange} required >
                    <option value="" disabled>Ingrese nombre de nueva plataforma</option>
                    <option value="Canal 7 TV">Canal 7 TV</option>
                    <option value="YouTube Live">YouTube Live</option>
                    <option value="Radio City 99.5">Radio City 99.5</option>
                </select>
            </div>
        </ABMFormLayout>
    );
}