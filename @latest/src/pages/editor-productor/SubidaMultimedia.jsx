// src/pages/SubidaMultimedia.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import CargaArchivos from '../../components/ui/CargaArchivos.jsx';
import DetallesEmision from '../../components/ui/DetallesEmision.jsx';
import '../../styles/pages/subidaMultimedia.css';

// 1. Agregamos 'categoria' al estado inicial
const INITIAL_FORM_STATE = {
    tituloPrograma: '',
    categoria: '', // <--- NUEVO CAMPO REQUERIDO POR TU DB
    horaEmision: '',
    horaFinalizacion: '',
    fechasEmision: [''],
    lugarTransmision: '', // ID de la plataforma
    archivo: null,
    informe: null,
};

export default function SubidaMultimedia() {
    const [formData, setFormData] = useState(INITIAL_FORM_STATE);
    const [isUploading, setIsUploading] = useState(false);
    const [listaPlataformas, setListaPlataformas] = useState([]);

    // Cargar plataformas al inicio
    useEffect(() => {
        const cargarPlataformas = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/plataformas');
                setListaPlataformas(response.data);
            } catch (error) {
                console.error("Error al cargar plataformas:", error);
            }
        };
        cargarPlataformas();
    }, []);

    const handleChange = (name, value) => {
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleDateChange = (index, value) => {
        setFormData(prev => {
            const newFechas = [...prev.fechasEmision];
            newFechas[index] = value;
            return { ...prev, fechasEmision: newFechas };
        });
    };

    const addFecha = () => {
        setFormData(prev => ({
            ...prev,
            fechasEmision: [...prev.fechasEmision, ''] 
        }));
    };

    const removeFecha = (index) => {
        setFormData(prev => ({
            ...prev,
            fechasEmision: prev.fechasEmision.filter((_, i) => i !== index)
        }));
    };

    // --- LÓGICA DE ENVÍO AL BACKEND ---
    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsUploading(true);

        try {
            // 1. Validaciones básicas
            if (!formData.lugarTransmision) {
                alert("Por favor seleccione una plataforma de transmisión.");
                setIsUploading(false);
                return;
            }

            // 2. Mapeo de datos para tu DTO 'Programa' en Java
            // Tu backend espera hora formato "HH:mm:ss" (String) o Array.
            // Al enviarlo como string "HH:mm:00" suele ser más seguro para LocalTime.
            const programaPayload = {
                nombre: formData.tituloPrograma,
                categoria: formData.categoria, // Enviamos la categoría
                
                // Añadimos segundos :00 para cumplir formato LocalTime
                horaInicio: formData.horaEmision ? `${formData.horaEmision}:00` : null,
                horaFin: formData.horaFinalizacion ? `${formData.horaFinalizacion}:00` : null,
                
                idPlataforma: parseInt(formData.lugarTransmision),
                estadoAprobacion: "Pendiente", // Valor por defecto seguro
                
                // Simulamos rutas ya que es un JSON body
                rutaArchivo: formData.archivo ? `/programas/${formData.archivo.name}` : "N/A",
                formatoArchivo: formData.archivo ? formData.archivo.name.split('.').pop().toUpperCase() : "N/A",
                
                rutaInforme: formData.informe ? `/informes/${formData.informe.name}` : null, // O crea una carpeta '/informes' en public                formatoInforme: formData.informe ? formData.informe.name.split('.').pop().toUpperCase() : null
            };

            console.log("Enviando al backend:", programaPayload);

            // 3. Llamada POST al Controller de Programas
            const response = await axios.post('http://localhost:8080/api/programas', programaPayload);
            
            // 4. Manejo de respuesta (Tu backend devuelve un String con el mensaje)
            const mensaje = response.data;
            
            if (typeof mensaje === 'string' && (mensaje.includes("éxito") || mensaje.includes("Exito"))) {
                alert('¡Programa creado exitosamente!');
                setFormData(INITIAL_FORM_STATE); // Limpiar formulario
            } else {
                alert('El servidor respondió: ' + mensaje);
            }

        } catch (error) {
            console.error('Error durante la creación:', error);
            alert('Ocurrió un error al conectar con el servidor.');
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="subida-multimedia-container">
            <form className="formulario-subida" onSubmit={handleSubmit}>
                
                <CargaArchivos handleChange={handleChange} formData={formData} />
                
                {/* Sección de Datos Básicos */}
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

                {/* 💥 NUEVO CAMPO: CATEGORÍA (Requerido por DB) 💥 */}
                <div className="titulo-programa-box" style={{ marginTop: '1rem' }}>
                    <label htmlFor="categoria">Categoría</label>
                    <select 
                        id="categoria"
                        name="categoria"
                        value={formData.categoria}
                        onChange={(e) => handleChange(e.target.name, e.target.value)}
                        required
                        style={{ width: '100%', padding: '10px', borderRadius: '8px', border: '1px solid #ccc' }}
                    >
                        <option value="" disabled>Seleccione una categoría</option>
                        <option value="NOTICIERO">Noticiero</option>
                        <option value="DEPORTES">Deportes</option>
                        <option value="ENTRETENIMIENTO">Entretenimiento</option>
                        <option value="CULTURA">Cultura</option>
                        <option value="SERIE">Serie / Ficción</option>
                    </select>
                </div>

                <DetallesEmision 
                    formData={formData} 
                    handleChange={handleChange} 
                    handleDateChange={handleDateChange} 
                    addFecha={addFecha} 
                    removeFecha={removeFecha} 
                    listaPlataformas={listaPlataformas}
                />

                <button 
                    type="submit" 
                    className="btn-subir-programa"
                    disabled={isUploading} 
                >
                    {isUploading ? 'Guardando...' : 'Crear Programa'}
                </button>
            </form>
        </div>
    );
}