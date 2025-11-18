import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';

export default function ABMPlataformasForm({ plataformaId, onCancel, onSuccess }) {
    
    // Verificamos si es edición (si ID no es 0)
    const isEditing = plataformaId !== 0;
    
    const [formData, setFormData] = useState({
        nombre: '', 
        tipo: ''
    });

    // Si estamos editando, cargamos los datos de la plataforma
    useEffect(() => {
        if (isEditing) {
            // 💥 CORREGIDO: Usamos 'plataformaId' (singular)
            axios.get(`http://localhost:8080/api/plataformas/${plataformaId}`)
                .then(res => {
                    setFormData({
                        nombre: res.data.nombre,
                        tipo: res.data.tipo
                    });
                })
                .catch(err => console.error("Error cargando plataforma:", err));
        }
    }, [plataformaId, isEditing]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const plataformaData = {
            nombre: formData.nombre,
            tipo: formData.tipo
        };

        try {
            let response;
            if (isEditing) {
                // EDITAR (PUT) - 💥 CORREGIDO: Usamos 'plataformaId'
                response = await axios.put(`http://localhost:8080/api/plataformas/${plataformaId}`, plataformaData);
            } else {
                // CREAR (POST)
                response = await axios.post('http://localhost:8080/api/plataformas', plataformaData);
            }

            // Verificamos los mensajes de éxito de tus SPs
            const msg = response.data;
            if (msg === "Plataforma ingresada con éxito." || msg === "Datos de la plataforma actualizados con éxito.") {
                alert(msg);
                if (onSuccess) onSuccess();
            } else {
                alert("Error del servidor: " + msg);
            }

        } catch (error) {
            console.error("Error al guardar:", error);
            alert("Error de conexión o validación.");
        }
    };

    return (
        <ABMFormLayout
            // 💥 CORREGIDO: Usamos 'plataformaId' para el título
            title={isEditing ? `Editar Plataforma ID: ${plataformaId}` : "Nueva Plataforma"}
            onSubmit={handleSubmit}
            onDiscard={onCancel}
        >
            <div className="form-group">
                <label htmlFor="nombre">Nombre Plataforma</label>
                <input 
                    id="nombre" 
                    name="nombre" 
                    type="text" 
                    value={formData.nombre} 
                    onChange={handleChange} 
                    required 
                    placeholder="Ej: YouTube, Canal 13..."
                />
            </div>
            
            <div className="form-group">
                <label htmlFor="tipo">Tipo</label>
                <select 
                    id="tipo" 
                    name="tipo" 
                    value={formData.tipo} 
                    onChange={handleChange} 
                    required
                >
                    <option value="" disabled>Seleccione un tipo</option>
                    <option value="TV">TV</option>
                    <option value="Radio">Radio</option>
                    <option value="Online">Online / Streaming</option>
                    <option value="Red Social">Red Social</option>
                </select>
            </div>
        </ABMFormLayout>
    );
}