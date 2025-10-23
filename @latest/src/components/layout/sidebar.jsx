
import React, { useState } from "react";
import { Link } from 'react-router-dom';
import "../../styles/layout/sidebar.css";
import { Users, UploadCloud, CheckCircle, CalendarDays, Folders } from 'lucide-react';
export default function SideBar({ idRol }) {
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    // 2. Clase dinámica: aplica la clase 'visible' (usada en tu media query CSS) si el menú está abierto
    const menuClasses = `sub-header ${isMenuOpen ? 'visible' : ''}`;

    // Función para manejar el clic en los enlaces y cerrar el menú móvil automáticamente
    const handleLinkClick = () => {
        setIsMenuOpen(false);
    };

    return (
        <>

            <main className="main">
                {!isMenuOpen && (
                    <button
                        className="menu"
                        onClick={() => setIsMenuOpen(true)}
                    >
                        <i className="bi bi-list"></i>
                    </button>
                )}
                <aside className="sideBar">





                    <nav className={menuClasses}>


                        <button
                            className="menu btn-cerrar-movil" // Clase limpia sin 'nomostrar'
                            onClick={() => setIsMenuOpen(false)}
                        >
                            <i className="bi bi-x-lg"></i>
                        </button>

                        <ul className="lista">
                           
                                <p>Navegacion</p>
                            
                            <li >
                                <Link to="/perfil" className="menu-item " onClick={handleLinkClick}>
                                    <Users size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Perfil</span>
                                </Link>
                            </li>

                            <li>
                                <Link to="/subida" className="menu-item " onClick={handleLinkClick}>
                                    <UploadCloud size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Subida multimedia</span>
                                </Link>
                            </li>

                            <li>
                                <Link to="" className="menu-item " onClick={handleLinkClick}>

                                    <CheckCircle size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Estado y aprobacion</span>
                                </Link>
                            </li>

                            <li>
                                <Link to="" className="menu-item " onClick={handleLinkClick}>
                                        
                                    <CalendarDays size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Parrilla semanal</span>
                                </Link>
                            </li>
                            <li>
                                <Link to="" className="menu-item " onClick={handleLinkClick}>

                                    <Folders size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Gestion multimedia</span>
                                </Link>
                            </li>
                        </ul>
                    </nav>

                </aside>
            </main>
        </>




    );
}