// src/pages/NotFound.jsx
import React from 'react';
import '../styles/pages/notFound.css'; // Importamos los estilos

export default function NotFound() {
    return (
        <div className="not-found-container">
            <h1 className="not-found-title">
                404 - Página no encontrada
            </h1>
            
            <div className="not-found-image-wrapper">
                <img 
                    src="https://http.cat/404" 
                    alt="Error 404 Not Found" 
                    className="not-found-image" 
                />
            </div>

            <p className="not-found-text">
                Lo sentimos, la página que buscas no existe o ha sido movida.
            </p>
        </div>
    );
}