import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { format, startOfWeek, addDays } from 'date-fns';
import { es } from 'date-fns/locale';
// CORRECCIÓN AQUÍ: Subimos dos niveles con ../../
import '../../styles/pages/parrillaSemanal.css';

export default function ParrillaSemanal() {
    const [parrilla, setParrilla] = useState({});
    const [cargando, setCargando] = useState(true);
    
    // Generamos los días de la semana actual para las cabeceras
    const hoy = new Date();
    const inicioSemana = startOfWeek(hoy, { weekStartsOn: 1 }); // Lunes
    const diasSemana = Array.from({ length: 7 }).map((_, i) => addDays(inicioSemana, i));

    useEffect(() => {
        cargarDatos();
    }, []);

    const cargarDatos = async () => {
        try {
            // 1. Cargar Programas (para tener los nombres)
            const resProgramas = await axios.get('http://localhost:8080/api/programas/todos'); 
            const programasMap = {};
            resProgramas.data.forEach(p => {
                programasMap[p.id] = p.nombre || p.titulo;
            });

            // 2. Cargar la Parrilla (Días asignados)
            const resDias = await axios.get('http://localhost:8080/api/dias/todos');
            
            // 3. Organizar por fecha
            const parrillaOrganizada = {};

            resDias.data.forEach(item => {
                const fechaStr = item.dia; // "YYYY-MM-DD"
                if (!parrillaOrganizada[fechaStr]) {
                    parrillaOrganizada[fechaStr] = [];
                }
                parrillaOrganizada[fechaStr].push({
                    programa: programasMap[item.idPrograma] || "Programa Desconocido",
                    hora: "Horario a definir" 
                });
            });

            setParrilla(parrillaOrganizada);
            setCargando(false);

        } catch (error) {
            console.error("Error cargando parrilla:", error);
            setCargando(false);
        }
    };

    return (
        <div className="parrilla-container">
            <div className="parrilla-titulo-barra">
                <h1>PROGRAMACIÓN SEMANAL</h1>
            </div>

            {cargando ? (
                <div style={{color:'white', textAlign:'center', padding:'20px'}}>Cargando programación...</div>
            ) : (
                <div className="parrilla-grid">
                    {diasSemana.map((diaDate) => {
                        const fechaKey = format(diaDate, 'yyyy-MM-dd');
                        const nombreDia = format(diaDate, 'EEEE', { locale: es }).toUpperCase();
                        const bloques = parrilla[fechaKey] || [];

                        return (
                            <div key={fechaKey} className="dia-wrapper">
                                <div className="dia-columna-header">
                                    {nombreDia} <br/>
                                    <span style={{fontSize:'0.8em', opacity:0.8}}>{format(diaDate, 'dd/MM')}</span>
                                </div>
                                
                                <div className="dia-columna-contenido">
                                    {bloques.length > 0 ? (
                                        bloques.map((bloque, index) => (
                                            <div key={`${fechaKey}-${index}`} className="bloque-programa">
                                                <p className="programa-nombre">{bloque.programa}</p>
                                            </div>
                                        ))
                                    ) : (
                                        <div className="bloque-vacio" style={{padding:'10px', opacity:0.5, fontSize:'0.9rem'}}>
                                            Sin programación
                                        </div>
                                    )}
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
}