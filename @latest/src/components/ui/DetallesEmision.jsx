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
    removeFecha,
    listaPlataformas // 💥 Recibimos la nueva prop 💥 
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
            <div className="form-group">
                <label htmlFor="lugarTransmision">Lugar de Transmisión</label>
                
                {/* 💥 Usamos un SELECT en lugar de INPUT TEXT 💥 */}
                <select
                    id="lugarTransmision"
                    name="lugarTransmision"
                    value={formData.lugarTransmision}
                    onChange={(e) => handleChange(e.target.name, e.target.value)}
                    required
                    className="input-form" // Asegurate de usar tu clase de estilo
                >
                    <option value="" disabled>Seleccione una plataforma</option>
                    
                    {listaPlataformas.length > 0 ? (
                        listaPlataformas.map((plataforma) => (
                            <option key={plataforma.id} value={plataforma.id}>
                                {/* Mostramos Nombre y Tipo para más claridad */}
                                {plataforma.nombre} ({plataforma.tipo})
                            </option>
                        ))
                    ) : (
                        <option value="" disabled>Cargando plataformas...</option>
                    )}
                </select>
            </div>
            

        </div>
    );
}