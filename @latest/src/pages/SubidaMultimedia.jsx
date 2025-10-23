// src/pages/SubidaMultimedia.jsx
import React, { useState } from 'react';
import { Clock, Calendar, Globe } from 'lucide-react'; // Íconos para el formulario
import '../styles/pages/subidaMultimedia.css'; // 💥 Importar CSS específico

export default function SubidaMultimedia() {
    // Estado para manejar los datos del formulario
    const [formData, setFormData] = useState({
        tituloPrograma: '',
        horaEmision: '',
        horaFinalizacion: '',
        fechaEmision: '',
        lugarTransmision: '',
        archivo: null,
        informe: null,
    });

    const handleChange = (e) => {
        const { name, value, files } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: files ? files[0] : value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Datos a subir:', formData);
        alert('Formulario enviado. (Ver detalles en la consola)');
        // Aquí iría la lógica de subida a la API
    };

    return (
        <div className="subida-multimedia-container">
            
            <form className="formulario-subida" onSubmit={handleSubmit}>
                
                {/* 💥 1. ÁREA DE CARGA DE ARCHIVOS / INFORME 💥 */}
                <div className="area-carga-archivos">
                    <div className="icon-placeholder">
                        {/* Puedes usar un ícono de Lucide como 'Image' o 'FileText' */}
                        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M15 8v.01" /><rect x="1" y="5" width="22" height="14" rx="2" /><path d="M6 15l4-4 6 6 4-4" /></svg>
                    </div>
                    <h2>SUBA EL CONTENIDO DEL RESPECTIVO PROGRAMA Y EL INFORME DEL MISMO</h2>
                    
                    <div className="input-group-carga">
                         <label htmlFor="archivo-programa" className="btn-archivo">
                            {formData.archivo ? formData.archivo.name : 'Subir Archivo de Programa (.mp4, .mov, etc.)'}
                         </label>
                         <input 
                            id="archivo-programa"
                            name="archivo"
                            type="file"
                            onChange={handleChange}
                            required
                         />
                         <label htmlFor="archivo-informe" className="btn-archivo">
                            {formData.informe ? formData.informe.name : 'Subir Informe (.pdf, .doc)'}
                         </label>
                         <input 
                            id="archivo-informe"
                            name="informe"
                            type="file"
                            onChange={handleChange}
                            required
                         />
                    </div>
                </div>

                {/* 💥 2. TÍTULO DEL PROGRAMA 💥 */}
                <div className="titulo-programa-box">
                    <label htmlFor="tituloPrograma">Título del programa</label>
                    <input 
                        id="tituloPrograma"
                        name="tituloPrograma"
                        type="text"
                        placeholder="Ingrese el título"
                        value={formData.tituloPrograma}
                        onChange={handleChange}
                        required
                    />
                </div>

                {/* 💥 3. DETALLES DE EMISIÓN (Flex/Grid) 💥 */}
                <div className="detalles-emision-grid">
                    
                    {/* HORA DE EMISIÓN */}
                    <div className="card-detalle hora-emision-card">
                        <h3>HORA DE EMISIÓN</h3>
                        <div className="input-detalle">
                            <Clock size={20} />
                            <input 
                                name="horaEmision"
                                type="time"
                                placeholder="Ingrese hora de emisión"
                                value={formData.horaEmision}
                                onChange={handleChange}
                                required
                            />
                        </div>
                         <div className="input-detalle">
                            <Clock size={20} />
                            <input 
                                name="horaFinalizacion"
                                type="time"
                                placeholder="Ingrese hora de finalización"
                                value={formData.horaFinalizacion}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    {/* FECHA DE EMISIÓN */}
                    <div className="card-detalle fecha-emision-card">
                        <h3>FECHA DE EMISIÓN</h3>
                        <div className="input-detalle">
                            <Calendar size={20} />
                            <input 
                                name="fechaEmision"
                                type="date"
                                value={formData.fechaEmision}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    {/* LUGAR DE TRANSMISIÓN */}
                    <div className="card-detalle lugar-transmision-card">
                        <h3>¿Donde se va a transmitir?</h3>
                        <div className="input-detalle">
                            <Globe size={20} />
                            <select 
                                name="lugarTransmision"
                                value={formData.lugarTransmision}
                                onChange={handleChange}
                                required
                            >
                                <option value="" disabled>Seleccione lugar</option>
                                <option value="canal1">Canal 1</option>
                                <option value="canal2">Canal 2</option>
                                <option value="web">Web Streaming</option>
                            </select>
                        </div>
                    </div>

                </div>

                {/* 💥 4. BOTÓN DE ENVÍO 💥 */}
                <button type="submit" className="btn-subir-programa">
                    Subir Programa
                </button>

            </form>
        </div>
    );
}