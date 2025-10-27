import React, { useState } from 'react';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';
export default function ABMSegmentosForm({ segmentoId, onCancel, onSuccess }) {
    const initialData = segmentoId 
        ? { programa: 'Noticias Mañana', nombreBloque: 'Bloque Nacional', duracion: '20', orden: '1' } 
        : { programa: '', nombreBloque: '', duracion: '', orden: '' };
    
    const [formData, setFormData] = useState(initialData);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Guardando datos del segmento:', formData);
        alert(`Segmento ${segmentoId ? 'actualizado' : 'creado'} con éxito!`);
        onSuccess && onSuccess();
    };

    return (
        <ABMFormLayout
            title="Segmentos"
            onSubmit={handleSubmit}
            onDiscard={onCancel}
        >
            <div className="form-group">
                <label htmlFor="programa">Programa</label>
                <select id="programa" name="programa" value={formData.programa} onChange={handleChange} required >
                    <option value="" disabled>Seleccione Programa</option>
                    <option value="Noticias Mañana">Noticias Mañana</option>
                    <option value="Deportes Hoy">Deportes Hoy</option>
                </select>
            </div>
            <div className="form-group">
                <label htmlFor="nombreBloque">Nombre del bloque</label>
                <input id="nombreBloque" name="nombreBloque" type="text" value={formData.nombreBloque} onChange={handleChange} placeholder="Ingrese nombre del bloque" required />
            </div>
            <div className="form-group">
                <label htmlFor="duracion">Duracion</label>
                <select id="duracion" name="duracion" value={formData.duracion} onChange={handleChange} required >
                    <option value="" disabled>Duracion del bloque</option>
                    <option value="20">20 min</option>
                    <option value="25">25 min</option>
                    <option value="30">30 min</option>
                </select>
            </div>
            <div className="form-group">
                <label htmlFor="orden">Orden</label>
                <select id="orden" name="orden" value={formData.orden} onChange={handleChange} required >
                    <option value="" disabled>Orden del bloque</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                </select>
            </div>
        </ABMFormLayout>
    );
}