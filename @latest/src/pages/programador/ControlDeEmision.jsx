import React, { useState, useEffect } from 'react';
import { ListVideo } from 'lucide-react'; 
import '../../styles/pages/controlEmision.css';

export default function ControlEmision() {
    // Usamos una estructura que coincida con tu DTO de Java (ControlEmisionDTO)
    const [dashboardData, setDashboardData] = useState({
        enVivo: null,    // Objeto ProgramaEnVivoInfo o null
        proximos: [],    // Lista de ProgramaInfo
        publicidades: [] // Lista de publicidades
    });
    
    const [loading, setLoading] = useState(true);

    // --- 1. Cargar Datos (USANDO EL ENDPOINT "INTELIGENTE") ---
    const cargarDatos = async () => {
        setLoading(true);
        try {
            // 🔥 CAMBIO CLAVE: Usamos el endpoint que mira la Agenda (Dias)
            const response = await fetch('http://localhost:8080/api/programas/control-emision');
            if (!response.ok) throw new Error("Error al cargar dashboard");
            
            const data = await response.json();
            setDashboardData(data);

        } catch (error) {
            console.error("Error cargando datos:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        cargarDatos();
    }, []);

    // --- 2. Lógica para "PONER EN VIVO" (Crear nueva emisión) ---
    const iniciarTransmision = async (idPrograma) => {
        try {
            const response = await fetch('http://localhost:8080/api/emisiones/comenzar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ idPrograma: idPrograma })
            });

            if (!response.ok) throw new Error("Error al iniciar transmisión");
            
            alert("¡Programa AL AIRE!");
            cargarDatos(); // Recargar para actualizar la pantalla
        } catch (error) {
            alert(error.message);
        }
    };

    // --- 3. Lógica para "SACAR DEL AIRE" (Finalizar emisión actual) ---
    const finalizarTransmision = async (idEmision) => {
        try {
            // Asumiendo que tienes un endpoint para apagar
            const response = await fetch(`http://localhost:8080/api/emisiones/${idEmision}/finalizar`, { // O '/apagado'
                method: 'PUT', // o POST según tu backend
                headers: { 'Content-Type': 'application/json' }
            });

            if (!response.ok) throw new Error("Error al finalizar");
            
            alert("Programa finalizado.");
            cargarDatos();
        } catch (error) {
            alert(error.message);
        }
    };

    const { enVivo, proximos, publicidades } = dashboardData;

    return (
        <div className="control-emision-container">
            <h1 className="main-title">CONTROL DE EMISIÓN</h1>

            {/* --- ZONA DE PROGRAMA ACTUAL (EN VIVO) --- */}
            <div className={`programa-actual-card ${enVivo ? 'is-live' : ''}`}>
                <div className="programa-header-row">
                    <div>
                        <span className={enVivo ? "badge-live" : "badge-offline"}>
                            {enVivo ? 'EN VIVO 🔴' : 'OFFLINE ⚫'}
                        </span>
                        
                        <div className="programa-info-box" style={{marginTop: '1rem'}}>
                            {enVivo ? (
                                <>
                                    {/* Datos vienen del DTO ProgramaEnVivoInfo */}
                                    <h2 className="programa-titulo-sm">{enVivo.titulo}</h2>
                                    <p className="programa-horario-sm">
                                        {enVivo.horaInicio} - {enVivo.horaFin}
                                    </p>
                                    <p style={{fontSize:'0.8rem', color:'#aaa'}}>ID Emisión: {enVivo.idEmision}</p>
                                    
                                    <button 
                                        className="btn-apagar"
                                        style={{marginTop:'10px', background:'red', color:'white', padding:'10px', border:'none', borderRadius:'5px', cursor:'pointer'}}
                                        onClick={() => finalizarTransmision(enVivo.idEmision)}
                                    >
                                        SACAR DEL AIRE
                                    </button>
                                </>
                            ) : (
                                <p style={{color:'#aaa'}}>No hay señal en el aire.</p>
                            )}
                        </div>
                    </div>
                </div>
                
                {/* Lista de Publicidades del programa en vivo */}
                {enVivo && publicidades && (
                     <div className="segmentos-section" style={{marginTop:'20px'}}>
                        <h3 style={{color:'#ccc'}}><ListVideo size={18}/> Bloque Publicitario</h3>
                        <div className="segmentos-list">
                            {publicidades.map((pub, index) => (
                                <div key={index} style={{padding:'5px', borderBottom:'1px solid #333'}}>
                                    {pub.titulo} ({pub.duracion} seg)
                                </div>
                            ))}
                        </div>
                     </div>
                )}
            </div>

            {/* --- LISTA DE PRÓXIMAS EMISIONES (Agenda del día) --- */}
            <div className="proximos-programas-section">
                <h2 className="proximos-title">Agenda de Hoy</h2>
                <div className="lista-proximos">
                    {loading && <p>Cargando agenda...</p>}
                    
                    {!loading && proximos.length === 0 && <p>No hay más programas agendados para hoy.</p>}

                    {proximos.map((prog, index) => (
                        <div key={index} className="programa-card" style={{background:'#1e1e20', padding:'15px', marginBottom:'10px', borderRadius:'8px', display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                            <div>
                                <h4 style={{color:'white', margin:0}}>{prog.titulo}</h4>
                                <p style={{color:'#888', fontSize:'0.9rem'}}>
                                    {prog.horaInicio} - {prog.horaFin}
                                </p>
                            </div>
                            
                            {/* Si NO hay nada en vivo, mostramos el botón para iniciar este */}
                            {!enVivo && (
                                <button 
                                    onClick={() => iniciarTransmision(prog.idPrograma)} // Necesitamos el ID del programa
                                    style={{background:'#22c55e', color:'white', padding:'8px 15px', border:'none', borderRadius:'5px', cursor:'pointer'}}
                                >
                                    PONER EN VIVO
                                </button>
                            )}
                             {/* Si YA hay algo en vivo, bloqueamos el botón */}
                            {enVivo && (
                                <button disabled style={{background:'#444', color:'#888', padding:'8px 15px', border:'none', borderRadius:'5px', cursor:'not-allowed'}}>
                                    ESPERA FINALIZAR ACTUAL
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}