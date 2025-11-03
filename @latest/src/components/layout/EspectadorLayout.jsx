import React from 'react';
import { Outlet, NavLink } from 'react-router-dom';
// Importamos el CSS que crearemos en el siguiente paso
import '../../styles/layout/EspectadorLayout.css'; 

// --- Componente del Header (Ahora vive en el Layout) ---
function EspectadorHeader() {
  return (
    <header className="envivo-header">
      <div className="envivo-logo">
        <span>My</span>
        <span className="envivo-logo-icon">⚙️</span>
      </div>
      <NavLink
        to="/en-vivo"
        end
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

// --- Layout Principal del Espectador ---
export default function EspectadorLayout() {
  return (
    <div className="envivo-page-container">
      {/* 1. Muestra el header negro */}
      <EspectadorHeader />
      
      {/* 2. 'Outlet' renderiza la página hija (EnVivo o Encuestas) */}
      <Outlet /> 
    </div>
  );
}