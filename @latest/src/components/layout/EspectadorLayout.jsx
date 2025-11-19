import React, { useState, useEffect } from 'react';
import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom';
import '../../pages/espectador/EnVivo.css'; // <--- Importa el CSS unificado aquí
import logo from '../../assets/logo.png';

export default function EspectadorLayout() {
    const navigate = useNavigate();
    const location = useLocation();
    const [usuario, setUsuario] = useState(null);

    // Detectamos si hay usuario logueado cada vez que cambia la ruta
    useEffect(() => {
        const userStr = localStorage.getItem('usuario');
        if (userStr) {
            try {
                setUsuario(JSON.parse(userStr));
            } catch (e) {
                setUsuario(null);
            }
        } else {
            setUsuario(null);
        }
    }, [location]);

    const handleLogout = () => {
        localStorage.removeItem('usuario');
        setUsuario(null);
        navigate('/en-vivo');
    };

    return (
        <div className="espectador-layout" style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', backgroundColor: '#333' }}>
            
            {/* --- HEADER INTEGRADO --- */}
            <header className="espectador-header">
                
                {/* IZQUIERDA: Botón Login/Logout */}
                <div className="header-left">
                    {usuario ? (
                        <button 
                            onClick={handleLogout} 
                            className="btn-auth-espectador logout"
                        >
                            <i className="bi bi-box-arrow-right"></i> Cerrar Sesión
                        </button>
                    ) : (
                        <Link 
                            to="/login-espectador" 
                            className="btn-auth-espectador login"
                        >
                            Iniciar Sesión
                        </Link>
                    )}
                </div>

                {/* CENTRO: Logo */}
                <div className="header-center">
                    <img src={logo} alt="Logo Canal" className="header-logo" />
                </div>

                {/* DERECHA: Botón "Ver En Vivo" (Solo si no estamos ya en /en-vivo) */}
                <div className="header-right">
                    {location.pathname !== '/en-vivo' && (
                        <Link to="/en-vivo" className="btn-live-nav">
                            <span className="live-dot"></span> ¡Ver En Vivo!
                        </Link>
                    )}
                </div>
            </header>

            {/* CONTENIDO DE LAS PÁGINAS */}
            <main className="espectador-content" style={{ flex: 1, width: '100%' }}>
                <Outlet />
            </main>
        </div>
    );
}