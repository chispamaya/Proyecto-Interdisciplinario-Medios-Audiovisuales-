// src/pages/programador/errores.jsx
import React, { useState, useEffect } from 'react';
// 🟢 Importamos axios
import axios from 'axios';
// CORRECCIÓN: La ruta correcta para ir de 'pages/programador' a 'src' es bajar 2 niveles (../../)
import '../../styles/pages/errores.css'; 

// Componente de Fila de Error
const ErrorRow = ({ error }) => {

    /**
     * LÓGICA DE FECHA CORREGIDA:
     * Spring Boot envía la fecha como array: [2025, 11, 17, 15, 30, 0].
     * JavaScript new Date() no entiende ese array directamente.
     * Esta función desarma el array y crea la fecha correctamente.
     */
    const formatearFecha = (fechaArray) => {
        // Validación por seguridad
        if (!fechaArray || !Array.isArray(fechaArray)) return '-';

        // 1. Desestructuramos el array
        const [year, month, day, hour, minute, second] = fechaArray;

        // 2. Creamos la fecha en JS.
        //    IMPORTANTE: En JS los meses van de 0 a 11.
        //    Por eso restamos 1 al mes que viene del backend (1-12).
        const fecha = new Date(year, month - 1, day, hour || 0, minute || 0, second || 0);

        // 3. Formateamos a string local
        return fecha.toLocaleString();
    };

    return (
        <div className="error-row-scroll-wrapper">
            <div className="error-row-content">
                <div className="error-cell fecha">{formatearFecha(error.fechaYHora)}</div>
                <div className="error-cell usuario">{error.idUsuario}</div>
                <div className="error-cell tipo">
                    <span className="tipo-tag tipo-error">
                        {error.tipo || "Error"}
                    </span>
                </div>
                <div className="error-cell mensaje">
                    {error.mensaje}
                </div>
            </div>
        </div>
    );
};

// Componente Principal
export default function Errores() {
    const [listaErrores, setListaErrores] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState(null); // Estado para el modo HTTP CATS

    // Función para traer los datos
    const fetchErrores = async () => {
        setIsLoading(true);
        setHttpError(null);

        try {
            // 🟢 Usamos axios.get
            const response = await axios.get("http://localhost:8080/api/errores");
            
            // Axios devuelve el cuerpo en response.data
            const data = response.data;

            // ÉXITO: Ordenamos los datos (más nuevo primero)
            const sortedData = data.sort((a, b) => {
                // Reconstruimos fechas temporalmente solo para comparar
                const dateA = new Date(a.fechaYHora[0], a.fechaYHora[1]-1, a.fechaYHora[2], a.fechaYHora[3]||0, a.fechaYHora[4]||0, a.fechaYHora[5]||0);
                const dateB = new Date(b.fechaYHora[0], b.fechaYHora[1]-1, b.fechaYHora[2], b.fechaYHora[3]||0, b.fechaYHora[4]||0, b.fechaYHora[5]||0);
                return dateB - dateA; 
            });
            setListaErrores(sortedData);

        } catch (error) {
            console.error("Error fetching errores:", error);
            
            let status = 503; 
            let message = "No se pudo conectar al servidor";

            if (error.response) {
                // El servidor respondió (4xx/5xx)
                status = error.response.status;
                // Intentamos obtener el mensaje de error del cuerpo de la respuesta
                message = error.response.data?.message || error.response.data?.error || (typeof error.response.data === 'string' ? error.response.data : error.response.statusText);
            } else if (error.request) {
                // La solicitud fue hecha, pero no hubo respuesta (error de red/servidor caído)
                message = "El servidor no respondió. Asegúrate de que esté corriendo en http://localhost:8080.";
            }

            // Guardamos el error con status
            setHttpError({ status: status, message: message });
            
        } finally {
            // SIEMPRE: Apagamos el indicador de carga
            setIsLoading(false);
        }
    };

    // Hook: Se ejecuta una vez al cargar la página
    useEffect(() => {
        fetchErrores();
    }, []);

    // ------------------------------------------------------
    // VISTA 1: MODO HTTP CATS (Si hay error de conexión/backend)
    // ------------------------------------------------------
    if (httpError) {
        return (
            <div className="http-cat-container">
                <h1 className="http-cat-title">
                    ¡Miau! Ocurrió un error ({httpError.status})
                </h1>
                <p className="http-cat-message">
                    {httpError.message || "No se pudo conectar al servidor"}
                </p>
                
                <img 
                    src={`https://http.cat/${httpError.status}`} 
                    alt={`Error ${httpError.status}`} 
                    className="http-cat-image"
                />

                <button onClick={fetchErrores} className="http-cat-button">
                    🔄 Reintentar Conexión
                </button>
            </div>
        );
    }

    // ------------------------------------------------------
    // VISTA 2: TABLA DE ERRORES (Funcionamiento Normal)
    // ------------------------------------------------------
    return (
        <div className="errores-container">
            
            <header className="errores-header">
                <h1>Registro de Errores</h1>
            </header>

            {/* Wrapper para el scroll horizontal de la cabecera */}
            <div className="errores-header-scroll-wrapper">
                <div className="errores-list-header-content">
                    <div className="error-cell fecha">Fecha y Hora</div>
                    <div className="error-cell usuario">Usuario ID</div>
                    <div className="error-cell tipo">Tipo</div>
                    <div className="error-cell mensaje">Mensaje</div>
                </div>
            </div>

            {/* Contenedor de la lista con scroll vertical */}
            <div className="errores-list-container">
                {isLoading ? (
                    <div className="errores-empty-state loading">
                        Cargando registros...
                    </div>
                ) : listaErrores.length > 0 ? (
                    listaErrores.map((error) => (
                        // Usamos error.id como clave única
                        <ErrorRow key={error.id || Math.random()} error={error} />
                    ))
                ) : (
                    <div className="errores-empty-state success">
                        <h2>¡Todo limpio!</h2>
                        <p>No se encontraron errores registrados en el sistema.</p>
                    </div>
                )}
            </div>
        </div>
    );
}