import React from 'react';
import { Outlet, Link } from 'react-router-dom';
import '../../styles/layout/EspectadorLayout.css';
import logo from '../../assets/logo.png'; 

export default function EspectadorLayout() {
  return (
    <div className="espectador-layout-container">
      <header className="espectador-header">
        <div className="espectador-header-logo">
          <Link to="/en-vivo">
            <img src={logo} alt="Logo" className="espectador-logo" />
          </Link>
        </div>
        
        {/* NAV CORREGIDO - SIN EL BOTÓN DE ADMIN */}
        <nav className="espectador-nav">
          {/* <Link to="/login" className="espectador-nav-link admin">Admin</Link>  <-- ELIMINADO */}
          <Link to="/login-espectador" className="espectador-nav-link login">
            Login
          </Link>
          <Link to="/registro" className="espectador-nav-link register">
            Registrarse
          </Link>
        </nav>
      </header>
      <main className="espectador-main-content">
        <Outlet />
      </main>
    </div>
  );
}