// src/components/layout/EspectadorLayout.jsx (CORREGIDO)

import React from 'react';
import { Outlet, Link } from 'react-router-dom';
import '../../styles/layout/EspectadorLayout.css';
import logo from '../../assets/logo.png'; 

export default function EspectadorLayout() {
    return (
        <div className="espectador-layout-container">
            
            {/* --- HEADER --- */}
            <header className="header-espectador">
                <div className="header-espectador-logo-container">
                    <img src={logo} alt="Logo Canal" className="header-espectador-logo" />
                </div>
                
                {/* 💥 BOTÓN APUNTA AL NUEVO LOGIN 💥 */}
                <div className="header-espectador-login">
                    <Link to="/login-espectador" className="btn-login-espectador">
                        Iniciar Sesión
                    </Link>
                </div>
            </header>

            {/* --- CONTENIDO PRINCIPAL (EnVivo, etc.) --- */}
            <main className="espectador-main-content">
                <Outlet /> 
            </main>
        </div>
    );
}

