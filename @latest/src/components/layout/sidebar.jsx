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
    BarChart     // Reportes Audiencia
    // 💥 PlusSquare ELIMINADO 💥
} from 'lucide-react';

// --- Enlaces para Editor y Productor ---
// (Sin cambios)
const EditorLinks = ({ onClick }) => (
    <>
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
// (Sin cambios)
const ProgramadorLinks = ({ onClick }) => (
    <>
        <p className="panel-titulo">Panel de programacion</p>
        <li>
            <Link to="/controlEmision" className="menu-item" onClick={onClick}>
                <Zap size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Control de Emisión</span>
            </Link>
        </li>
        <li>
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
// (💥 MODIFICADO 💥)
const AdminLinks = ({ onClick }) => (
    <>
        <p className="panel-titulo">ABM</p>
        <li>
            <Link to="/abm" className="menu-item" onClick={onClick}>
                <UserCog size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Gestión de Roles</span>
            </Link>
        </li>
        
        <p className="panel-titulo">REPORTES</p>
        <li>
            <Link to="/reportes" className="menu-item" onClick={onClick}>
                <BarChart size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Reportes Audiencia</span>
            </Link>
        </li>

        {/* 💥 LINK ELIMINADO 💥 */}
    </>
);


// --- Componente Principal del SideBar ---
// (Sin cambios en la lógica)
export default function SideBar({ idRol }) {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const menuClasses = `sub-header ${isMenuOpen ? 'visible' : ''}`;

    const handleLinkClick = () => {
        setIsMenuOpen(false);
    };

    const renderRoleLinks = () => {
        switch (idRol) {
            case 'admin':
                return (
                    <>
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
                return <EditorLinks onClick={handleLinkClick} />;
        }
    };

    return (
        <>
            {!isMenuOpen && (
                <button className="menu" onClick={() => setIsMenuOpen(true)}>
                    <i className="bi bi-list"></i>
                </button>
            )}
            
            <aside className="sideBar">
                <nav className={menuClasses}>
                    <button className="menu btn-cerrar-movil" onClick={() => setIsMenuOpen(false)}>
                        <i className="bi bi-x-lg"></i>
                    </button>

                    <ul className="lista">
                        <li>
                            <Link to="/perfil" className="menu-item " onClick={handleLinkClick}>
                                <Users size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                <span className="label">Perfil</span>
                            </Link>
                        </li>
                        {renderRoleLinks()}
                    </ul>
                </nav>
            </aside>
        </>
    );
}