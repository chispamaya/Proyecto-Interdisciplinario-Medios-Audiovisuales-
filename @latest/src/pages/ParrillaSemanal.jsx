import React from 'react';
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> beed6e4 (Frontend terminado)
// 💥 Ruta de importación corregida. Asegúrate de que el archivo exista en esta ubicación.
import '../styles/pages/parrillaSemanal.css'; 

// --- Datos de Ejemplo para la Parrilla ---
const diasSemana = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"];
const bloquesHorarios = [
    { hora: "09:00 - 11:00", programa: "DIAS SUAVES", enVivo: true }, 
    { hora: "09:00 - 11:00", programa: "DIAS SUAVES" },
    { hora: "09:00 - 11:00", programa: "DIAS SUAVES" },
    { hora: "09:00 - 11:00", programa: "DIAS SUAVES" },
    { hora: "09:00 - 11:00", programa: "DIAS SUAVES" },
    { hora: "09:00 - 11:00", programa: "DIAS SUAVES" },
    { hora: "09:00 - 11:00", programa: "DIAS SUAVES" },
    { hora: "09:00 - 11:00", programa: "DIAS SUAVES" },
];

export default function ParrillaSemanal() {
    return (
        <div className="parrilla-container">
            {/* Título de la Sección */}
<<<<<<< HEAD
=======
=======
import '../styles/pages/parrillaSemanal.css';
import { useParrilla } from '../context/ParrillaContext'; // 1. Importamos el Hook

const diasSemana = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"];

export default function ParrillaSemanal() {
    
    // 2. Leemos la parrilla real desde el Contexto
    const { parrilla } = useParrilla(); 
    // 'parrilla' ahora es un objeto: { LUNES: [...], MARTES: [...] }

    return (
        <div className="parrilla-container">
>>>>>>> eac45b2 (frontend terminado)
>>>>>>> beed6e4 (Frontend terminado)
            <div className="parrilla-titulo-barra">
                <h1>PROGRAMACIÓN</h1>
            </div>

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> beed6e4 (Frontend terminado)
            {/* Grilla de la Parrilla */}
            <div className="parrilla-grid">
                
                {/* Mapeamos CADA DÍA completo */}
                {diasSemana.map((dia) => (
                    // Div que envuelve cabecera y contenido del día
                    <div key={`wrapper-${dia}`} className="dia-wrapper"> 
                        
                        {/* Cabecera del día */}
                        <div className="dia-columna-header">
                            {dia}
                        </div>
                        
                        {/* Contenido de la columna del día */}
                        <div className="dia-columna-contenido">
                            {/* Mapea los bloques horarios */}
                            {bloquesHorarios.map((bloque, index) => (
                                <div key={`${dia}-${index}`} className="bloque-programa">
                                    <p className="programa-nombre">{bloque.programa}</p>
                                    <p className="programa-hora">{bloque.hora}</p>
                                    {/* Muestra "En vivo" condicionalmente */}
                                    {bloque.enVivo && dia === "LUNES" && index === 0 && (
                                        <span className="en-vivo-badge">En vivo</span>
                                    )}
                                </div>
                            ))}
                        </div>
                    </div> 
                ))}
            </div>
        </div>
    );
}

<<<<<<< HEAD
=======
=======
            <div className="parrilla-grid">
                
                {diasSemana.map((dia) => {
                    
                    // 3. Obtenemos los bloques específicos PARA ESE DÍA
                    const bloquesDelDia = parrilla[dia] || []; 

                    return (
                        <div key={`wrapper-${dia}`} className="dia-wrapper"> 
                            
                            <div className="dia-columna-header">
                                {dia}
                            </div>
                            
                            <div className="dia-columna-contenido">
                                
                                {/* 4. Mapeamos los bloques DE ESE DÍA */}
                                {bloquesDelDia.length > 0 ? (
                                    bloquesDelDia.map((bloque, index) => (
                                        <div key={`${dia}-${index}`} className="bloque-programa">
                                            <p className="programa-nombre">{bloque.programa}</p>
                                            <p className="programa-hora">{bloque.hora}</p>
                                            
                                            {/* 5. Lógica 'enVivo' simplificada (viene de los datos) */}
                                            {bloque.enVivo && (
                                                <span className="en-vivo-badge">En vivo</span>
                                            )}
                                        </div>
                                    ))
                                ) : (
                                    // Opcional: Mostrar algo si el día está vacío
                                    <div className="bloque-vacio">
                                        <p>Sin programación</p>
                                    </div>
                                )}
                            </div>
                        </div> 
                    );
                })}
            </div>
        </div>
    );
}
>>>>>>> eac45b2 (frontend terminado)
>>>>>>> beed6e4 (Frontend terminado)
