import React from 'react';
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
            <div className="parrilla-titulo-barra">
                <h1>PROGRAMACIÓN</h1>
            </div>

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

