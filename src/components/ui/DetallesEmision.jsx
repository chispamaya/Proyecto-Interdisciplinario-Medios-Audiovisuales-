// src/components/form/DetallesEmision.jsx
import React from 'react';
// 💥 Asegúrate que la ruta a InputIcono sea correcta (probablemente '../ui/InputIcono.jsx')
import InputIcono from '../ui/InputIcono.jsx'; 
import { Clock, Calendar, Globe } from 'lucide-react';

export default function DetallesEmision({ 
    formData, 
    handleChange, 
    handleDateChange, 
    addFecha, 
    removeFecha 
}) {

    const handleInputChange = (e) => {
        handleChange(e.target.name, e.target.value);
    };

    return (
        <div className="detalles-emision-grid">

            {/* HORA DE EMISIÓN */}
            <div className="card-detalle hora-emision-card">
                <h3>HORA DE EMISIÓN</h3>
                <InputIcono
                    icon={Clock}
                    name="horaEmision"
                    type="time"
                    placeholder="Ingrese hora de emisión"
                    value={formData.horaEmision}
                    onChange={handleInputChange}
                />
                {/* 💥 AGREGADO: Título para la hora de finalización 💥 */}
                <h3>HORA DE FINALIZACIÓN</h3> 
                <InputIcono
                    icon={Clock}
                    name="horaFinalizacion"
                    type="time"
                    placeholder="Ingrese hora de finalización"
                    value={formData.horaFinalizacion}
                    onChange={handleInputChange}
                />
            </div>

            {/* FECHA DE EMISIÓN */}
            <div className="card-detalle fecha-emision-card">
                <h3>FECHA DE EMISIÓN</h3>
                {(formData?.fechasEmision || []).map((fecha, index) => (
                    <div key={index} className="fecha-item"> 
                        <InputIcono 
                            icon={Calendar}
                            name={`fechaEmision-${index}`}
                            type="date"
                            value={fecha}
                            onChange={(e) => handleDateChange(index, e.target.value)} 
                        />
                        {formData.fechasEmision.length > 1 && (
                            <button 
                                type="button" 
                                className="btn-fecha"
                                onClick={() => removeFecha(index)}
                            >
                                X
                            </button> 
                        )}
                    </div>
                ))}
                <button 
                    type="button" 
                    className="btn-fecha"
                    onClick={addFecha}
                >
                    + Agregar Fecha
                </button> 
            </div>
            {/* 💥 ELIMINADO: Botón duplicado fuera de la tarjeta 💥 */}
            {/* <button type="button" onClick={addFecha}>Agregar Fecha</button> */} 

            {/* LUGAR DE TRANSMISIÓN */}
            <div className="card-detalle lugar-transmision-card">
                <h3>¿Donde se va a transmitir?</h3>
                <div className="input-detalle">
                    <Globe size={20} />
                    <select
                        name="lugarTransmision"
                        value={formData.lugarTransmision}
                        onChange={handleInputChange}
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
    );
}