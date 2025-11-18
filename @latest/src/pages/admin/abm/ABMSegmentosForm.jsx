import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx'; 
import '../../../styles/components/abmForm.css';

export default function ABMSegmentosForm({ segmentoId, onCancel, onSuccess }) {
    
    const isEditing = segmentoId !== 0;
    
    const [formData, setFormData] = useState({
        titulo: '', 
        duracion: '',
        estadoAprobacion: 'Pendiente', // Valor por defecto
        idPrograma: '',
        orden: '' // Campo nuevo que agregamos
    });

    const [listaProgramas, setListaProgramas] = useState([]);

    // 1. Cargar lista de programas (para el select)
    useEffect(() => {
        axios.get('http://localhost:8080/api/programas')
            .then(res => setListaProgramas(res.data))
            .catch(err => console.error("Error cargando programas:", err));
    }, []);

    // 2. Si es edición, cargar datos del segmento
    useEffect(() => {
        if (isEditing) {
            axios.get(`http://localhost:8080/api/segmentos/${segmentoId}`)
                .then(res => {
                    const seg = res.data;
                    setFormData({
                        titulo: seg.titulo,
                        duracion: seg.duracion,
                        estadoAprobacion: seg.estadoAprobacion,
                        idPrograma: seg.idPrograma,
                        orden: seg.orden || ''
                    });
                })
                .catch(err => console.error("Error cargando segmento:", err));
        }
    }, [segmentoId, isEditing]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const segmentoData = {
            titulo: formData.titulo,
            duracion: parseFloat(formData.duracion),
            estadoAprobacion: formData.estadoAprobacion,
            idPrograma: parseInt(formData.idPrograma),
            orden: parseInt(formData.orden)
        };

        try {
            let response;
            if (isEditing) {
                // EDITAR (PUT)
                response = await axios.put(`http://localhost:8080/api/segmentos/${segmentoId}`, segmentoData);
            } else {
                // CREAR (POST)
                response = await axios.post('http://localhost:8080/api/segmentos', segmentoData);
            }

            const msg = response.data;
            if (msg === "Segmento ingresado con éxito." || msg === "Segmento actualizado con éxito.") {
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
            title={isEditing ? `Editar Segmento ID: ${segmentoId}` : "Nuevo Segmento"}
            onSubmit={handleSubmit}
            onDiscard={onCancel}
        >
            {/* Selección de Programa */}
            <div className="form-group">
                <label htmlFor="idPrograma">Programa</label>
                <select 
                    id="idPrograma" 
                    name="idPrograma" 
                    value={formData.idPrograma} 
                    onChange={handleChange} 
                    required
                >
                    <option value="" disabled>Seleccione un programa</option>
                    {listaProgramas.map(prog => (
                        <option key={prog.id} value={prog.id}>
                            {prog.nombre}
                        </option>
                    ))}
                </select>
            </div>

            <div className="form-group">
                <label htmlFor="titulo">Nombre Segmento</label>
                <input 
                    id="titulo" 
                    name="titulo" 
                    type="text" 
                    value={formData.titulo} 
                    onChange={handleChange} 
                    required 
                />
            </div>
            
            <div className="form-group">
                <label htmlFor="duracion">Duración (min)</label>
                <input 
                    id="duracion" 
                    name="duracion" 
                    type="number" 
                    step="0.1"
                    value={formData.duracion} 
                    onChange={handleChange} 
                    required 
                />
            </div>

            <div className="form-group">
                <label htmlFor="orden">Orden</label>
                <input 
                    id="orden" 
                    name="orden" 
                    type="number" 
                    value={formData.orden} 
                    onChange={handleChange} 
                    required 
                    placeholder="Ej: 1, 2, 3..."
                />
            </div>

            <div className="form-group">
                <label htmlFor="estadoAprobacion">Estado</label>
                <select 
                    id="estadoAprobacion" 
                    name="estadoAprobacion" 
                    value={formData.estadoAprobacion} 
                    onChange={handleChange} 
                    required
                >
                    <option value="Pendiente">Pendiente</option>
                    <option value="Aprobado">Aprobado</option>
                    <option value="Rechazado">Rechazado</option>
                </select>
            </div>
        </ABMFormLayout>
    );
}