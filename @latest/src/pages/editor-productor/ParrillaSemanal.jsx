import React, { useState, useEffect } from 'react';
import '../../styles/pages/parrillaSemanal.css';

const diasSemana = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"];

export default function ParrillaSemanal() {
    
    // 1. Estado local para guardar la parrilla que viene del Backend
    const [parrillaReal, setParrillaReal] = useState({});
    const [loading, setLoading] = useState(true);

    // 2. Cargar datos al iniciar la página
    useEffect(() => {
        const cargarParrilla = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/programas/parrilla-semanal');
                if (!response.ok) throw new Error("Error cargando parrilla");
                
                const data = await response.json();
                // data será algo como: { "LUNES": [{nombrePrograma: "Noticias", ...}], "MARTES": [] }
                setParrillaReal(data);
            } catch (error) {
                console.error(error);
            } finally {
                setLoading(false);
            }
        };
        cargarParrilla();
    }, []);

    if (loading) return <div className="parrilla-container"><p>Cargando programación...</p></div>;

    return (
        <div className="parrilla-container">
            <div className="parrilla-titulo-barra">
                <h1>PROGRAMACIÓN SEMANAL</h1>
            </div>

            <div className="parrilla-grid">
                
                {diasSemana.map((dia) => {
                    
                    // 3. Obtenemos los bloques del backend para este día
                    const bloquesDelDia = parrillaReal[dia] || []; 

                    return (
                        <div key={`wrapper-${dia}`} className="dia-wrapper"> 
                            
                            <div className="dia-columna-header">
                                {dia}
                            </div>
                            
                            <div className="dia-columna-contenido">
                                
                                {bloquesDelDia.length > 0 ? (
                                    bloquesDelDia.map((bloque, index) => (
                                        <div key={`${dia}-${index}`} className="bloque-programa">
                                            <p className="programa-nombre">{bloque.nombrePrograma}</p>
                                            <p className="programa-hora">
                                                {bloque.horaInicio} - {bloque.horaFin}
                                            </p>
                                            
                                            {/* Si quisieras mostrar "En vivo" basado en la hora actual, podrías calcularlo aquí */}
                                        </div>
                                    ))
                                ) : (
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