import React from 'react';
// 1. Importa el CSS que ahora solo tiene estilos de contenido
import './EnVivo.css';

// --- Componente Principal de la Página EnVivo ---
// ❌ (Ya no tiene el 'EnVivoHeader', el layout lo maneja)
export default function EnVivo() {
  return (
    // Ya no necesita <div className="envivo-page-container">
    <main className="envivo-main-content">
      <div className="envivo-container-white">
        <h1 className="envivo-title">EN VIVO</h1>
        <div className="envivo-content-wrapper">
          <div className="envivo-video-wrapper">
            <iframe
              className="envivo-iframe"
              src="https://www.youtube.com/watch?v=KP5OFHf4Dag"
              title="Transmisión en vivo"
              frameBorder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            ></iframe>
          </div>
        </div>
      </div>
    </main>
  );
}