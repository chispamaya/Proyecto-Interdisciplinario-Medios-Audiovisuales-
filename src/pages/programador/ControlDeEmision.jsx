import React, { useState } from 'react';
import {Megaphone, Tv } from 'lucide-react';
import ProximoProgramaCard from '../../components/ui/ProximoProgramaCard.jsx';
import '../../styles/pages/controlEmision.css';

// --- Datos de Simulación ---
const programaActual = {
    titulo: "La peña",
    horario: "09:00 - 10:00",
    imagen: "https://placehold.co/200x200/FFFFFF/000000?text=La+Peña", // Placeholder de ejemplo
};

const proximosProgramas = [
    { hora: "10:00 - 11:00", titulo: "Cine en Casa", descripcion: "Bloque de cine clásico." },
    { hora: "11:00 - 11:15", titulo: "Bloque Publicitario", descripcion: "Pautas comerciales." },
    { hora: "11:15 - 12:00", titulo: "Deportes Hoy", descripcion: "Análisis deportivo en vivo." },
];

export default function ControlEmision() {
    // Estado para simular si el programa está EN VIVO (idealmente viene de una API)
    const [isLive, setIsLive] = useState(true);

    // Renderizado del Badge de estado
    const LiveBadge = ({ isLive }) => (
        <span className={isLive ? "badge-live" : "badge-offline"}>
            {isLive ? 'EN VIVO' : 'OFFLINE'}
        </span>
    );

    return (
        <div className="control-emision-container">

            {/* TÍTULO PRINCIPAL */}
            <h1 className="main-title">CONTROL DE EMISIÓN</h1>

            {/* 1. TARJETA DE PROGRAMA ACTUAL */}
            <div className={`programa-actual-card ${isLive ? 'is-live' : ''}`}>

                <LiveBadge isLive={isLive} />

                <div className="programa-info-box">
                    <img className="programa-logo" src={programaActual.imagen} alt={`${programaActual.titulo} logo`} />
                    <p className="programa-titulo-sm">Programa: {programaActual.titulo}</p>
                    <p className="programa-horario-sm">Horario: {programaActual.horario}</p>
                </div>

                {/* Controles de Simulación (Opcionales, solo para testeo) */}




            </div>

            {/* 2. PRÓXIMOS PROGRAMAS (Lista) */}
            <div className="proximos-programas-section">
                <h2 className="proximos-title">PRÓXIMOS PROGRAMAS</h2>

                <div className="lista-proximos">
                    {proximosProgramas.map((programa, index) => (
                        <ProximoProgramaCard key={index} programa={programa} />
                    ))}
                </div>
            </div>

            {/* 3. BOTONES DE ACCIÓN (Ej: Iniciar Bloque Publicitario) */}
            <div className="action-buttons-emision">
               
                
                <button className="btn-action-emision btn-publicidad">
                    <Megaphone size={20} />
                    Bloque Publicitario
                </button>
                
               
                <button className="btn-action-emision btn-cambio">
                    <Tv size={20} />

                    Iniciar/Cambiar Programa
                </button>
            </div>


        </div>
    );
}
