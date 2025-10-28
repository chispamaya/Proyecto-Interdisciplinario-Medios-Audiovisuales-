import React, { useState } from "react";
import { Link } from 'react-router-dom';
import "../../styles/layout/sidebar.css";

// 1. Importamos todos los iconos necesarios
import {
    Users,        // Perfil
    UploadCloud,  // Subida Multimedia
    CheckCircle,  // Estado Aprobacion
    CalendarDays, // Parrilla Semanal
    Folders,      // Gestion Multimedia
    Zap,          // Control de Emision
    AlertTriangle,// Errores
    Calendar,     // Armado de Parrilla
    UserCog,      // Gestión de Roles (ABM)
    BarChart      // Reportes Audiencia
} from 'lucide-react';

// --- Enlaces para Editor y Productor ---
// (Basado en tu código anterior, agrupados bajo "PANEL DE PRODUCTORES")
const EditorLinks = ({ onClick }) => (
    <>
        {/* Este es el título que se ve en el sidebar del Admin */}
        <p className="panel-titulo">Panel de productores y editores</p>
        <li>
            <Link to="/subida" className="menu-item" onClick={onClick}>
                <UploadCloud size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Subida multimedia</span>
            </Link>
        </li>
        <li>
            <Link to="/estado/1" className="menu-item" onClick={onClick}>
                <CheckCircle size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Estado y aprobacion</span>
            </Link>
        </li>
        <li>
            <Link to="/parrilla" className="menu-item" onClick={onClick}>
                <CalendarDays size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Parrilla semanal</span>
            </Link>
        </li>
        <li>
            <Link to="/gestion" className="menu-item" onClick={onClick}>
                <Folders size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Gestion multimedia</span>
            </Link>
        </li>
    </>
);

// --- Enlaces para el Programador ---
// (Basado en la imagen de Kevin Pintasso)
const ProgramadorLinks = ({ onClick }) => (
    <>
        {/* Título para el panel del Admin */}
        <p className="panel-titulo">Panel de programacion</p>
        <li>
            <Link to="/controlEmision" className="menu-item" onClick={onClick}>
                <Zap size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Control de Emisión</span>
            </Link>
        </li>
        <li>
            {/* ¡Nuevo link basado en tu imagen! */}
            <Link to="/errores" className="menu-item" onClick={onClick}>
                <AlertTriangle size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Errores</span>
            </Link>
        </li>
        <li>
            <Link to="/armadoParrilla" className="menu-item" onClick={onClick}>
                <Calendar size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Armado de Parrilla</span>
            </Link>
        </li>
    </>
);

// --- Enlaces solo para el Admin ---
// (Basado en la imagen de Facundo Bekar)
const AdminLinks = ({ onClick }) => (
    <>
        <p className="panel-titulo">ABM</p>
        <li>
            <Link to="/abm" className="menu-item" onClick={onClick}>
                <UserCog size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Gestión de Roles</span>
            </Link>
        </li>
        {/* Puedes añadir más links de ABM aquí, ej:
        <li>
            <Link to="/abm-empleados" className="menu-item" onClick={onClick}>
                <Users size={20} ... />
                <span className="label">ABM Empleados</span>
            </Link>
        </li>
        */}
        
        <p className="panel-titulo">REPORTES</p>
        <li>
            <Link to="/reportes" className="menu-item" onClick={onClick}>
                <BarChart size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Reportes Audiencia</span>
            </Link>
        </li>
    </>
);


// --- Componente Principal del SideBar ---
export default function SideBar({ idRol }) {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const menuClasses = `sub-header ${isMenuOpen ? 'visible' : ''}`;

    const handleLinkClick = () => {
        setIsMenuOpen(false);
    };

    // Función para renderizar los enlaces según el rol
    const renderRoleLinks = () => {
        // Asumiendo que idRol es un string: 'admin', 'programador', 'editor'
        switch (idRol) {
            
            // EL ADMIN VE TODO:
            case 'admin':
                return (
                    <>
                        {/* Incluye los enlaces de Admin, Editor y Programador */}
                        <AdminLinks onClick={handleLinkClick} />
                        <EditorLinks onClick={handleLinkClick} />
                        <ProgramadorLinks onClick={handleLinkClick} />
                    </>
                );
            
            case 'programador':
                return <ProgramadorLinks onClick={handleLinkClick} />;
            
            case 'editor':
            case 'productor':
            default:
                // Muestra los enlaces de editor por defecto
                return <EditorLinks onClick={handleLinkClick} />;
        }
    };

    return (
        <>
            <main className="main">
                {/* Botón de Menú Hamburguesa (sin cambios) */}
                {!isMenuOpen && (
                    <button className="menu" onClick={() => setIsMenuOpen(true)}>
                        <i className="bi bi-list"></i>
                    </button>
                )}
                
                {/* Sidebar */}
                <aside className="sideBar">
                    <nav className={menuClasses}>
                        {/* Botón de Cerrar en Móvil (sin cambios) */}
                        <button className="menu btn-cerrar-movil" onClick={() => setIsMenuOpen(false)}>
                            <i className="bi bi-x-lg"></i>
                        </button>

                        <ul className="lista">
                            
                            {/* Link de PERFIL (común para todos) */}
                            <li>
                                <Link to="/perfil" className="menu-item " onClick={handleLinkClick}>
                                    <Users size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Perfil</span>
                                </Link>
                            </li>

                            {/* Aquí se cargan los links según el rol */}
                            {renderRoleLinks()}
                            
                        </ul>
                    </nav>
                </aside>
            </main>
        </>
    );
}
