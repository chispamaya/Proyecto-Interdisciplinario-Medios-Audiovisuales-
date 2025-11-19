import React, { useState, useEffect } from 'react';
import { Megaphone, Tv, ListVideo } from 'lucide-react'; // Agregué ListVideo para el ícono
import ProximoProgramaCard from '../../components/ui/ProximoProgramaCard.jsx';
import '../../styles/pages/controlEmision.css';

// --- Datos de Simulación (PROGRAMAS HARDCODEADOS) ---
const programaActual = {
    id: 1, // Le pongo un ID simulado para filtrar si fuera necesario
    titulo: "La peña",
    horario: "09:00 - 10:00", // Hora inicio: 09:00
    imagen: "https://placehold.co/200x200/FFFFFF/000000?text=La+Peña",
};

const proximosProgramas = [
    { hora: "10:00 - 11:00", titulo: "Cine en Casa", descripcion: "Bloque de cine clásico." },
    { hora: "11:00 - 11:15", titulo: "Bloque Publicitario", descripcion: "Pautas comerciales." },
    { hora: "11:15 - 12:00", titulo: "Deportes Hoy", descripcion: "Análisis deportivo en vivo." },
];

// --- Función Auxiliar para calcular la hora (HH:MM) ---
const sumarMinutos = (horaString, minutosASumar) => {
    if (!horaString) return "--:--";
    const [horas, minutos] = horaString.split(':').map(Number);
    const fecha = new Date();
    fecha.setHours(horas);
    fecha.setMinutes(minutos + minutosASumar);
    
    const h = fecha.getHours().toString().padStart(2, '0');
    const m = fecha.getMinutes().toString().padStart(2, '0');
    return `${h}:${m}`;
};

export default function ControlEmision() {
    const [isLive, setIsLive] = useState(true);
    
    // 1. Estado para guardar los segmentos que vienen del Backend
    const [segmentos, setSegmentos] = useState([]);

    // 2. Conexión al Backend (Solo Segmentos)
    useEffect(() => {
        fetch('http://localhost:8080/api/segmentos')
            .then(res => {
                if (!res.ok) throw new Error("Error al obtener segmentos");
                return res.json();
            })
            .then(data => {
                // Ordenamos por el campo 'orden' para que la lista salga correcta
                // NOTA: Si quieres filtrar solo los de este programa, harías: 
                // data.filter(s => s.idPrograma === programaActual.id)
                // Por ahora mostramos todos los que traiga el endpoint.
                const segmentosOrdenados = data.sort((a, b) => a.orden - b.orden);
                setSegmentos(segmentosOrdenados);
            })
            .catch(err => console.error("Error fetching segmentos:", err));
    }, []);


    const LiveBadge = ({ isLive }) => (
        <span className={isLive ? "badge-live" : "badge-offline"}>
            {isLive ? 'EN VIVO' : 'OFFLINE'}
        </span>
    );

    // Extraemos la hora de inicio "09:00" del string "09:00 - 10:00"
    const horaInicioBase = programaActual.horario.split(' - ')[0];

    return (
        <div className="control-emision-container">

            {/* TÍTULO PRINCIPAL */}
            <h1 className="main-title">CONTROL DE EMISIÓN</h1>

            {/* 1. TARJETA DE PROGRAMA ACTUAL */}
            <div className={`programa-actual-card ${isLive ? 'is-live' : ''}`}>

                <div className="programa-header-row" style={{display:'flex', justifyContent:'space-between', alignItems:'flex-start'}}>
                    <div>
                        <LiveBadge isLive={isLive} />
                        <div className="programa-info-box" style={{marginTop: '1rem'}}>
                            <img className="programa-logo" src={programaActual.imagen} alt={`${programaActual.titulo} logo`} />
                            <div>
                                <p className="programa-titulo-sm">Programa: {programaActual.titulo}</p>
                                <p className="programa-horario-sm">Horario: {programaActual.horario}</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* --- SECCIÓN DE SEGMENTOS (RUNDOWN) --- */}
                <div className="segmentos-section" style={{marginTop: '20px', background: 'rgba(0,0,0,0.2)', padding: '15px', borderRadius: '8px'}}>
                    <h3 style={{fontSize: '1rem', color: '#ccc', display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '10px'}}>
                        <ListVideo size={18}/> SEGMENTOS DEL AIRE
                    </h3>

                    <div className="segmentos-list">
                        {segmentos.length > 0 ? (
                            segmentos.map((seg, index) => {
                                // Lógica: Sumamos duraciones de los anteriores
                                const minutosAcumulados = segmentos
                                    .slice(0, index)
                                    .reduce((acc, curr) => acc + (curr.duracion || 0), 0);
                                
                                const horaTransmision = sumarMinutos(horaInicioBase, minutosAcumulados);

                                return (
                                    <div key={seg.id} style={{
                                        display: 'grid', 
                                        gridTemplateColumns: '60px 1fr auto', 
                                        padding: '8px', 
                                        borderBottom: '1px solid rgba(255,255,255,0.05)'
                                    }}>
                                        <span style={{color: '#60a5fa', fontWeight: 'bold', fontFamily: 'monospace'}}>
                                            {horaTransmision}
                                        </span>
                                        <span style={{color: '#fff'}}>
                                            {seg.titulo}
                                        </span>
                                        <span style={{color: '#888', fontSize: '0.9em'}}>
                                            {seg.duracion} min
                                        </span>
                                    </div>
                                );
                            })
                        ) : (
                            <p style={{color: '#666'}}>Cargando segmentos o lista vacía...</p>
                        )}
                    </div>
                </div>
                {/* -------------------------------------- */}

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
        </div>
    );
}