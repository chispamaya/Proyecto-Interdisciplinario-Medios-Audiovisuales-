import React from 'react';
import { NavLink } from 'react-router-dom';
// 1. Importa el CSS específico para esta página
import './EnVivo.css';

// --- Componente del Header específico para la página EnVivo ---
function EnVivoHeader() {
  return (
    <header className="envivo-header">
      {/* Logo simple de texto con ícono */}
      <div className="envivo-logo">
        <span>My</span>
        <span className="envivo-logo-icon">⚙️</span> {/* Puedes cambiar este ícono */}
      </div>

      {/* Navegación (Ejemplo) */}
      <NavLink
        // Asegúrate que esta ruta exista para los espectadores
        to="/inicio-espectador"
        className={({ isActive }) => isActive ? "envivo-nav-active" : "envivo-nav"}
      >
        Inicio
      </NavLink>
      <NavLink
        to="/encuestas-espectador"
        className={({ isActive }) => isActive ? "envivo-nav-active" : "envivo-nav"}
      >
        Encuestas
      </NavLink>
    </header>
  );
}

// --- Componente Principal de la Página EnVivo ---
export default function EnVivo() {
  return (
    // Contenedor principal de toda la página (fondo oscuro)
    <div className="envivo-page-container">

      {/* 1. Renderiza el header específico de esta página */}
      <EnVivoHeader />

      {/* 2. Contenido principal */}
      <main className="envivo-main-content">
        {/* Contenedor blanco con la imagen de fondo */}
        <div className="envivo-container-white">

          {/* Título "EN VIVO" */}
          <h1 className="envivo-title">EN VIVO</h1>

          {/* Wrapper que ahora solo centra el reproductor de video */}
          <div className="envivo-content-wrapper">
            <div className="envivo-video-wrapper">
              <iframe
                className="envivo-iframe"
                // URL del stream en vivo (ejemplo)
                src="https://www.youtube.com/embed/9Auq9mYxFEE?autoplay=1&mute=1&controls=1"
                title="Transmisión en vivo"
                frameBorder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowFullScreen
              ></iframe>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}