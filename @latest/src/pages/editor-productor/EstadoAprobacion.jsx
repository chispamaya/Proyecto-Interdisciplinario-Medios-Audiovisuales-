import React, { useState, useEffect } from 'react';
// 👇 Importa useParams y Link
import { useParams, Link } from 'react-router-dom';
import { Download, Calendar, Clock } from 'lucide-react';
import '../../styles/pages/estadoAprobacion.css';
import logoImage from '../../assets/logo.png';

// --- Simulación de DATOS COMPLETOS (incluyendo fechas por programa) ---
// En una app real, estos datos vendrían de tu API
const programasData = [
    {
        id: 1,
        nombre: "La peña",
        usuario: "Leon Vega",
        imagen: logoImage,
        fechas: ['15/10/25', '16/10/25', '17/10/25'],
        horaEmision: '19:40',
        horaFinalizacion: '21:00'
    },
    {
        id: 2,
        nombre: "Programa 2",
        usuario: "Usuario 2",
        imagen: logoImage,
        fechas: ['20/11/25', '21/11/25'],
        horaEmision: '14:00',
        horaFinalizacion: '15:30'
    },
    {
        id: 3,
        nombre: "Programa 3",
        usuario: "Usuario 3",
        imagen: logoImage,
        fechas: ['01/12/25', '05/12/25', '10/12/25', '15/12/25'],
        horaEmision: '10:00',
        horaFinalizacion: '11:00'
    },
    // Puedes añadir más programas aquí
];
// --- Fin Simulación ---

export default function EstadoAprobacion() {
    // 👇 Obtiene el parámetro 'id' de la URL (ej: /estado/2 -> id = '2')
    const { id } = useParams();
    const currentId = parseInt(id, 10); // Convierte el id de string a número

    // 👇 Encuentra el programa actual basado en el ID de la URL
    // Por defecto, muestra el programa con id 1 si no se encuentra o no hay ID
    const currentProgram = programasData.find(p => p.id === currentId) || programasData.find(p => p.id === 1) || null;

    // Si no se encuentra ningún programa (ni siquiera el default id 1), muestra un mensaje o redirige
    if (!currentProgram) {
        return <div className="detalle-propuesta-container">Programa no encontrado.</div>;
    }

    return (
        <div className="detalle-propuesta-container">
            {/* ------------------------------------- */}
            {/* --- 1. BARRA LATERAL IZQUIERDA (Dinámica) --- */}
            {/* ------------------------------------- */}
            <div className="propuesta-sidebar">
                {/* 👇 Itera sobre TODOS los programas para la sidebar */}
                {programasData.map((p) => (
                    // 👇 Envuelve cada item con un Link
                    <Link to={`/estado/${p.id}`} key={p.id} className="propuesta-link">
                        {/* 👇 Aplica 'active' si el ID del programa coincide con el ID de la URL */}
                        <div className={`propuesta-item ${p.id === currentProgram.id ? 'active' : ''}`}>
                            <img className="propuesta-logo-sm" src={p.imagen} alt={`${p.nombre} Logo`} />
                            <div className="propuesta-info">
                                <p className="propuesta-titulo">{p.nombre}</p>
                                <p className="propuesta-proponente">Propuesta de: {p.usuario}</p>
                            </div>
                        </div>
                    </Link>
                ))}
            </div>

            {/* ------------------------------------- */}
            {/* --- 2. CONTENIDO PRINCIPAL (Dinámico) --- */}
            {/* ------------------------------------- */}
            <main className="propuesta-main-content">

                {/* CABECERA */}
                <div className="propuesta-header">
                    {/* 👇 Muestra la imagen del programa actual */}
                    <img className="propuesta-logo-lg" src={currentProgram.imagen} alt={`${currentProgram.nombre} Logo`} />
                    <div className="propuesta-titulo-box">
                        {/* 👇 Muestra el nombre del programa actual */}
                        <h1 className="propuesta-titulo-lg">{currentProgram.nombre}</h1>
                        {/* 👇 Muestra el usuario del programa actual */}
                        <p className="propuesta-proponente-lg">Propuesta de: {currentProgram.usuario}</p>
                    </div>
                </div>

                {/* DETALLES Y BOTÓN */}
                <div className="detalles-grid">

                    {/* FECHA DE EMISIÓN CARD (Con scroll) */}
                    <div className="card-detalle fecha-card">
                        <h2 className='card-title'>FECHA DE EMISIÓN</h2>
                        <div className="fechas-list-wrapper">
                            {/* 👇 Muestra las fechas del programa actual */}
                            {currentProgram.fechas.map((fecha, index) => (
                                <div key={index} className="detalle-item">
                                    <Calendar size={24} className="detalle-icon" />
                                    <p className="detalle-text">{fecha}</p>
                                </div>
                            ))}
                            {/* Mensaje si no hay fechas */}
                            {currentProgram.fechas.length === 0 && <p>No hay fechas programadas.</p>}
                        </div>
                    </div>

                    {/* HORARIOS CARD */}
                    <div className="horarios-card">
                        <div className="card-detalle horario-emision-card">
                            <h2 className='card-title'>HORARIO DE EMISIÓN</h2>
                            <div className="detalle-item">
                                <Clock size={24} className="detalle-icon" />
                                {/* 👇 Muestra la hora de emisión del programa actual */}
                                <p className="detalle-text">{currentProgram.horaEmision || 'N/A'}</p>
                            </div>
                        </div>
                        <div className="card-detalle horario-finalizacion-card">
                            <h2 className='card-title'>HORARIO DE FINALIZACIÓN</h2>
                            <div className="detalle-item">
                                <Clock size={24} className="detalle-icon" />
                                {/* 👇 Muestra la hora de finalización del programa actual */}
                                <p className="detalle-text">{currentProgram.horaFinalizacion || 'N/A'}</p>
                            </div>
                        </div>
                    </div>

                    {/* DESCARGAR ARCHIVOS BUTTON */}
                    <div className="descargar-card-wrapper">
                        <button className="btn-descargar">
                            <Download size={24} />
                            <span>Descargar archivos</span>
                        </button>
                    </div>
                </div>

                {/* BOTONES DE ACCIÓN */}
                <div className="accion-buttons">
                    <button className="btn-accion btn-no-publicar">No publicar</button>
                    <button className="btn-accion btn-publicar">Publicar</button>
                </div>
            </main>
        </div>
    );
}



