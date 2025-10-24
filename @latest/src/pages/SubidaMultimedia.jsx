// src/pages/SubidaMultimedia.jsx (Lógica Central)
import React, { useState } from 'react';
import CargaArchivos from '../components/ui/CargaArchivos.jsx'; // Nuevo componente
import DetallesEmision from '../components/ui/DetallesEmision.jsx'; // Nuevo componente
import '../styles/pages/subidaMultimedia.css';

// Estado inicial del formulario (limpio y modular)
const INITIAL_FORM_STATE = {
    tituloPrograma: '',
    horaEmision: '',
    horaFinalizacion: '',
    fechasEmision: [''],
    lugarTransmision: '',
    archivo: null,
    informe: null,
};

export default function SubidaMultimedia() {
    const [formData, setFormData] = useState(INITIAL_FORM_STATE);
    const [isUploading, setIsUploading] = useState(false);

    // Función unificada para manejar todos los cambios de campos y archivos
    const handleChange = (name, value) => {
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };
    const handleDateChange = (index, value) => {
        setFormData(prev => {
            // Copia el array de fechas actual
            const newFechas = [...prev.fechasEmision];
            // Actualiza el valor en la posición (index) específica
            newFechas[index] = value;
            // Devuelve el nuevo estado con el array de fechas modificado
            return { ...prev, fechasEmision: newFechas };
        });
    };
    const addFecha = () => {
        setFormData(prev => ({
            ...prev,
            // Añade un string vacío al final del array de fechas
            fechasEmision: [...prev.fechasEmision, ''] 
        }));
    };

    // 💥 NUEVA FUNCIÓN: Elimina una fecha del array por su índice
    const removeFecha = (index) => {
        setFormData(prev => ({
            ...prev,
            // Crea un nuevo array excluyendo el elemento en la posición 'index'
            fechasEmision: prev.fechasEmision.filter((_, i) => i !== index)
        }));
    };
    // Función de envío con manejo asíncrono
    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsUploading(true);
        console.log('Datos a subir:', formData);
        
        try {
            // 💥 Aquí se llamaría a la API con fetch o Axios
            await new Promise(resolve => setTimeout(resolve, 2000)); // Simular espera
            
            alert('Programa subido exitosamente!');
            setFormData(INITIAL_FORM_STATE); // Limpiar formulario
        } catch (error) {
            console.error('Error durante la subida:', error);
            alert('Error al subir el programa.');
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="subida-multimedia-container">
            <form className="formulario-subida" onSubmit={handleSubmit}>
                
                {/* 1. ÁREA DE CARGA DE ARCHIVOS (Componente Abstraído) */}
                <CargaArchivos handleChange={handleChange} formData={formData} />
                
                {/* 2. TÍTULO DEL PROGRAMA (Sigue siendo simple, se queda aquí) */}
                <div className="titulo-programa-box">
                    <label htmlFor="tituloPrograma">Título del programa</label>
                    <input 
                        id="tituloPrograma"
                        name="tituloPrograma"
                        type="text"
                        placeholder="Ingrese el título"
                        value={formData.tituloPrograma}
                        onChange={(e) => handleChange(e.target.name, e.target.value)}
                        required
                    />
                </div>

                {/* 3. DETALLES DE EMISIÓN (Componente Abstraído) */}
                <DetallesEmision 
                    formData={formData} 
                    handleChange={handleChange} // Para campos simples (hora, lugar)
                    handleDateChange={handleDateChange} // Para manejar cambios en fechas
                    addFecha={addFecha} // Para agregar fechas
                    removeFecha={removeFecha} // Para quitar fechas
                />

                {/* 4. BOTÓN DE ENVÍO */}
                <button 
                    type="submit" 
                    className="btn-subir-programa"
                    disabled={isUploading} // Deshabilita durante la subida
                >
                    {isUploading ? 'Subiendo...' : 'Subir Programa'}
                </button>
            </form>
        </div>
    );
}