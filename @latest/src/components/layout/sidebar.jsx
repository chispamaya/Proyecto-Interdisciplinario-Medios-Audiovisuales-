
import React, { useState } from "react";
import { Link } from 'react-router-dom';
import "../../styles/layout/sidebar.css";

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
                            <li >

                                <Link to="/inicio" className="menu-item activo" onClick={handleLinkClick}>
                                    <span className="label">Perfil</span>
                                </Link>
                            </li>

                            <li>
                                <Link to="/mensajes" className="menu-item no" onClick={handleLinkClick}>
                                    <span className="label">Subida multimedia</span>
                                </Link>
                            </li>

                            <li>
                                <Link to="/nueva" className="menu-item no" onClick={handleLinkClick}>

                                    {/* El <br> debe estar cerrado correctamente en JSX */}
                                    <span className="label">Estado y aprobacion</span>
                                </Link>
                            </li>

                            <li>
                                <Link to="/nueva" className="menu-item no" onClick={handleLinkClick}>

                                    {/* El <br> debe estar cerrado correctamente en JSX */}
                                    <span className="label">Parrilla semanal</span>
                                </Link>
                            </li>
                            <li>
                                <Link to="/nueva" className="menu-item no" onClick={handleLinkClick}>

                                    {/* El <br> debe estar cerrado correctamente en JSX */}
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