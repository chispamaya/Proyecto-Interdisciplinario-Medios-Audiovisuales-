import React from 'react';
import '../../styles/pages/parrillaSemanal.css';
import { useParrilla } from '../../context/ParrillaContext'; // 1. Importamos el Hook

const diasSemana = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"];

export default function ParrillaSemanal() {
    
    // 2. Leemos la parrilla real desde el Contexto
    const { parrilla } = useParrilla(); 
    // 'parrilla' ahora es un objeto: { LUNES: [...], MARTES: [...] }

    return (
        <div className="parrilla-container">
            <div className="parrilla-titulo-barra">
                <h1>PROGRAMACIÓN</h1>
            </div>

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