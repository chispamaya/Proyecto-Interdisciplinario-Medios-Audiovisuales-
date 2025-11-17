import React from 'react';
// 1. Importamos Link DE VUELTA
import { Link } from 'react-router-dom';
import './EnVivo.css';

// --- Componente Principal de la Página EnVivo ---
export default function EnVivo() {
  return (
    <main className="envivo-main-content">
      <div className="envivo-container-white">
        
        {/* 2. VOLVEMOS A PONER EL BOTÓN AQUÍ */}
        <div className="envivo-header-area">
          <h1 className="envivo-title">EN VIVO</h1>
          
          <Link to="/encuestas-espectador" className="btn-encuestas-vivo">
            Ir a Encuestas
          </Link>
        </div>

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