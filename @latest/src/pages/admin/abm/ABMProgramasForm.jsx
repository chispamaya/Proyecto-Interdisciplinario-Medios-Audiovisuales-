import React, { useState, useEffect } from 'react';
import axios from 'axios'; // 🟢 Importamos Axios
import ABMFormLayout from '../../../components/abm/ABMFormLayout.jsx';
import '../../../styles/components/abmForm.css';
const formatLocalTimeArray = (timeArray) => {
    if (!timeArray || timeArray.length < 2) return '';

    // Aseguramos que tenga dos dígitos, añadiendo un 0 si es necesario (ej: 9 -> 09)
    const hour = String(timeArray[0]).padStart(2, '0');
    const minute = String(timeArray[1]).padStart(2, '0');

    return `${hour}:${minute}`;
};
export default function ABMProgramasForm({ programId, onCancel, onSuccess }) {

    // --- Estados del Formulario (Coinciden con tu DTO Java) ---
    const [nombre, setNombre] = useState('');
    const [duracion, setDuracion] = useState('');
    const [categoria, setCategoria] = useState('');
    const [estado, setEstado] = useState('Activo');
    const [estadoAprobacion, setEstadoAprobacion] = useState('PENDIENTE');
    const [horaInicio, setHoraInicio] = useState('');
    const [horaFin, setHoraFin] = useState('');
    // 🟢 CAMBIO CLAVE: El backend pide Hora Inicio y Fin, no duración texto.
   
    // Otros campos necesarios para que el backend no falle (valores por defecto)
    const [formatoArchivo, setFormatoArchivo] = useState('MP4');
    const [idPlataforma, setIdPlataforma] = useState(1); // Asumimos plataforma 1 por defecto

    const isEditMode = programId !== 0 && programId !== null;
    const title = isEditMode ? `Editar Programa (ID: ${programId})` : "Agregar Nuevo Programa";

    // --- 1. Cargar Datos al Abrir (GET) ---
    useEffect(() => {
        if (isEditMode) {
            axios.get(`http://localhost:8080/api/programas/${programId}`)
                .then(response => {
                    const data = response.data;
                    
                    setNombre(data.nombre || '');
                    setCategoria(data.categoria || '');
                    setEstado(data.estadoAprobacion || 'Activo'); // Usar estadoAprobacion del backend
                    
                    // Aplicar la función de corrección de formato a la hora
                    setHoraInicio(formatLocalTimeArray(data.horaInicio));
                    setHoraFin(formatLocalTimeArray(data.horaFin));

                    // NOTA: "duracion" en el formulario es un campo de texto simple, 
                    // si necesitas su valor, también deberías calcularlo/formatearlo aquí.
                })
                .catch(error => {
                    console.error("Error al cargar programa:", error);
                });
        }
    }, [programId, isEditMode]);

    // --- 2. Guardar Datos (PUT o POST) ---
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Armamos el JSON tal cual lo espera tu DTO Java "Programa"
        const programaDTO = {
            nombre: nombre,
            categoria: categoria,
            estadoAprobacion: estadoAprobacion,
            horaInicio: horaInicio + ":00", // Agregamos segundos para LocalTime
            horaFin: horaFin + ":00",       // Agregamos segundos para LocalTime
            formatoArchivo: formatoArchivo,
            idPlataforma: idPlataforma,
            // Campos opcionales vacíos para evitar null pointers si tu backend es estricto
            dias: [],
            segmentos: [],
            emisiones: []
        };

        try {
            if (isEditMode) {
                // --- MODO EDICIÓN (PUT) ---
                await axios.put(`http://localhost:8080/api/programas/${programId}`, programaDTO);
                alert("¡Programa modificado correctamente!");
            } else {
                // --- MODO CREACIÓN (POST) ---
                // (Por si reutilizas este form para crear)
                await axios.post(`http://localhost:8080/api/programas`, programaDTO);
                alert("¡Programa creado correctamente!");
            }
            onSuccess(); // Cierra el form y recarga la tabla
        } catch (error) {
            console.error("Error al guardar:", error);
            alert("Error al guardar el programa. Revisa la consola.");
        }
    };

    return (
        <ABMFormLayout title={title} onSubmit={handleSubmit} onCancel={onCancel}>

            {/* NOMBRE */}
            <div className="form-group">
                <label htmlFor="nombre">Nombre del Programa</label>
                <input
                    type="text"
                    id="nombre"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    required
                />
            </div>

            {/* 🟢 CAMBIO: HORA INICIO Y FIN (Reemplaza a Duración) */}
            <div style={{ display: 'flex', gap: '15px' }}>
                <div className="form-group" style={{ flex: 1 }}>
                    <label htmlFor="horaInicio">Hora Inicio</label>
                    <input
                        type="time"
                        id="horaInicio"
                        value={horaInicio}
                        onChange={(e) => setHoraInicio(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group" style={{ flex: 1 }}>
                    <label htmlFor="horaFin">Hora Fin</label>
                    <input
                        type="time"
                        id="horaFin"
                        value={horaFin}
                        onChange={(e) => setHoraFin(e.target.value)}
                        required
                    />
                </div>
            </div>

            {/* CATEGORÍA */}
            <div className="form-group">
                <label htmlFor="categoria">Categoría</label>
                <input
                    type="text"
                    id="categoria"
                    value={categoria}
                    onChange={(e) => setCategoria(e.target.value)}
                />
            </div>

            {/* ESTADO (Mapeado a estadoAprobacion) */}
            <div className="form-group">
                <label htmlFor="estado">Estado de Aprobación</label>
                <select
                    id="estado"
                    value={estadoAprobacion}
                    onChange={(e) => setEstadoAprobacion(e.target.value)}
                >
                    <option value="PENDIENTE">Pendiente</option>
                    <option value="APROBADO">Aprobado</option>
                    <option value="RECHAZADO">Rechazado</option>
                    <option value="EN_REVISION">En Revisión</option>
                </select>
            </div>

        </ABMFormLayout>
    );
}