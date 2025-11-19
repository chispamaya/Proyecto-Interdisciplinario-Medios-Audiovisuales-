import React, { useState, useEffect } from 'react';
import axios from 'axios'; // Importamos Axios
import { Megaphone, Tv, ListVideo, RefreshCw } from 'lucide-react'; 
import '../../styles/pages/controlEmision.css';

export default function ControlEmision() {
    // Estado para la lista real de emisiones
    const [emisiones, setEmisiones] = useState([]);
    const [loading, setLoading] = useState(true);
    
    // Segmentos (Del código de tus compañeros, lo mantenemos)
    const [segmentos, setSegmentos] = useState([]);

    // --- 1. Cargar Datos Reales ---
    useEffect(() => {
        cargarDatos();
    }, []);

    const cargarDatos = async () => {
        setLoading(true);
        try {
            // A. Cargar Emisiones (TU PARTE)
            const resEmisiones = await axios.get('http://localhost:8080/api/emisiones');
            setEmisiones(resEmisiones.data);

            // B. Cargar Segmentos (PARTE COMPAÑEROS)
            const resSegmentos = await axios.get('http://localhost:8080/api/segmentos');
            setSegmentos(resSegmentos.data.sort((a, b) => a.orden - b.orden));
            
        } catch (error) {
            console.error("Error cargando datos:", error);
        } finally {
            setLoading(false);
        }
    };

    // --- 2. Función para cambiar estado (EN VIVO / APAGADO) ---
    const cambiarEstado = async (idEmision, ponerEnVivo) => {
        const usuarioId = localStorage.getItem('usuarioId');
        if (!usuarioId) return alert("Error: No estás logueado.");

        const accion = ponerEnVivo ? 'vivo' : 'apagado';
        // PUT /api/emisiones/{id}/vivo?idUsuarioAuditoria=...
        try {
            await axios.put(`http://localhost:8080/api/emisiones/${idEmision}/${accion}`, null, {
                params: { idUsuarioAuditoria: usuarioId }
            });
            alert(ponerEnVivo ? "¡Programa Puesto AL AIRE!" : "Programa finalizado.");
            cargarDatos(); // Recargar para ver el cambio de estado
        } catch (error) {
            alert("Error al cambiar estado: " + (error.response?.data || error.message));
        }
    };

    // Encontrar la emisión que está actualmente EN VIVO (si hay alguna)
    const emisionActual = emisiones.find(e => e.enVivo);

    return (
        <div className="control-emision-container">
            <h1 className="main-title">CONTROL DE EMISIÓN</h1>

            {/* --- ZONA DE PROGRAMA ACTUAL --- */}
            <div className={`programa-actual-card ${emisionActual ? 'is-live' : ''}`}>
                <div className="programa-header-row">
                    <div>
                        <span className={emisionActual ? "badge-live" : "badge-offline"}>
                            {emisionActual ? 'EN VIVO 🔴' : 'OFFLINE ⚫'}
                        </span>
                        
                        <div className="programa-info-box" style={{marginTop: '1rem'}}>
                            {emisionActual ? (
                                <>
                                    <h2 className="programa-titulo-sm">Emisión ID: {emisionActual.id}</h2>
                                    <p className="programa-horario-sm">Programa ID: {emisionActual.idPrograma}</p>
                                    
                                    <button 
                                        className="btn-apagar"
                                        style={{marginTop:'10px', background:'red', color:'white', padding:'10px', border:'none', borderRadius:'5px', cursor:'pointer'}}
                                        onClick={() => cambiarEstado(emisionActual.id, false)}
                                    >
                                        SACAR DEL AIRE
                                    </button>
                                </>
                            ) : (
                                <p style={{color:'#aaa'}}>No hay nada transmitiendo ahora.</p>
                            )}
                        </div>
                    </div>
                </div>
                
                {/* Aquí iría tu sección de segmentos (la dejo resumida) */}
                {emisionActual && (
                     <div className="segmentos-section" style={{marginTop:'20px'}}>
                        <h3 style={{color:'#ccc'}}><ListVideo size={18}/> Segmentos del Programa</h3>
                        <div className="segmentos-list">
                            {/* Filtrar segmentos del programa actual si tuvieras el ID */}
                            {segmentos
                                .filter(s => s.idPrograma === emisionActual.idPrograma)
                                .map(seg => (
                                <div key={seg.id} style={{padding:'5px', borderBottom:'1px solid #333'}}>
                                    {seg.titulo} ({seg.duracion} min)
                                </div>
                            ))}
                        </div>
                     </div>
                )}
            </div>

            {/* --- LISTA DE PRÓXIMAS EMISIONES (Para poner en vivo) --- */}
            <div className="proximos-programas-section">
                <h2 className="proximos-title">Programación Disponible</h2>
                <div className="lista-proximos">
                    {loading && <p>Cargando...</p>}
                    
                    {emisiones.filter(e => !e.enVivo).map(emision => (
                        <div key={emision.id} className="programa-card" style={{background:'#1e1e20', padding:'15px', marginBottom:'10px', borderRadius:'8px', display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                            <div>
                                <h4 style={{color:'white', margin:0}}>Emisión {emision.id}</h4>
                                <p style={{color:'#888', fontSize:'0.9rem'}}>Programa ID: {emision.idPrograma}</p>
                            </div>
                            <button 
                                onClick={() => cambiarEstado(emision.id, true)}
                                style={{background:'#22c55e', color:'white', padding:'8px 15px', border:'none', borderRadius:'5px', cursor:'pointer'}}
                            >
                                PONER EN VIVO
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}