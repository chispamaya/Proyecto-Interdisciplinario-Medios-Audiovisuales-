// src/components/layout/sidebar.jsx

import React, { useState, useEffect } from "react"; // <-- 1. Importar useEffect
import { Link } from 'react-router-dom';
import "../../styles/layout/sidebar.css";

// Importamos todos los iconos necesarios
import {
    Users, UploadCloud, CheckCircle, CalendarDays, Folders, 
    Zap, AlertTriangle, Calendar, UserCog, BarChart, PlusSquare, History
} from 'lucide-react';

// --- CONSTANTES DE ROLES (Para traducir ID a String) ---
const ROLES_IDS = {
    ADMIN: [8], 
    EDITOR: [1, 2, 3, 4, 5, 6, 7], 
    PROGRAMADOR: [9, 10, 11],      
    ESPECTADOR: [12]
};

// --- Enlaces para Editor y Productor ---
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
const AdminLinks = ({ onClick }) => (
    <>
        <p className="panel-titulo">CONTENIDO</p>
        {/* ... tus links de admin ... */}
        <li>
            <Link to="/admin/crear-publicacion" className="menu-item" onClick={onClick}>
                <PlusSquare size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Crear Publicación</span>
            </Link>
        </li>
        <p className="panel-titulo">ABM</p>
        <li>
            <Link to="/admin/empleados" className="menu-item" onClick={onClick}>
                <UserCog size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                <span className="label">Gestión de Empleados</span>
            </Link>
        </li>
        {/* ... Agregá aquí los otros ABMs si querés (Programas, Plataformas, Segmentos) ... */}
        
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
export default function SideBar() { // <-- 2. Quitamos la prop 'idRol'
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [rolActual, setRolActual] = useState(null); // <-- 3. Estado para el rol

    // 4. EFECTO PARA LEER Y TRADUCIR EL ROL
    useEffect(() => {
        const storedId = localStorage.getItem('usuarioRol');
        
        if (storedId) {
            const id = parseInt(storedId);
            
            // Traducimos ID -> String para tu switch
            if (ROLES_IDS.ADMIN.includes(id)) {
                setRolActual('admin');
            } else if (ROLES_IDS.PROGRAMADOR.includes(id)) {
                setRolActual('programador');
            } else if (ROLES_IDS.EDITOR.includes(id)) {
                setRolActual('editor'); // o 'productor'
            } else {
                setRolActual('espectador');
            }
        }
    }, []);

    const menuClasses = `sub-header ${isMenuOpen ? 'visible' : ''}`;

    const handleLinkClick = () => {
        setIsMenuOpen(false);
    };

    const renderRoleLinks = () => {
        // 5. Usamos el estado 'rolActual'
        switch (rolActual) {
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
                return <EditorLinks onClick={handleLinkClick} />;
            default:
                return null; // Si no hay rol o es espectador, no muestra menú extra
        }
    };

    return (
        <>
            {!isMenuOpen && (
                <button className="menu" onClick={() => setIsMenuOpen(true)}>
                    {/* Usamos un ícono de Lucide en lugar de bi-list si querés, o dejalo así */}
                    <i className="bi bi-list"></i> 
                </button>
            )}
            
            <aside className="sideBar">
                <nav className={menuClasses}>
                    <button className="menu btn-cerrar-movil" onClick={() => setIsMenuOpen(false)}>
                        <i className="bi bi-x-lg"></i>
                    </button>

                    <ul className="lista">
                        {/* El perfil siempre visible */}
                        <li>
                            <Link to="/perfil" className="menu-item " onClick={handleLinkClick}>
                                <Users size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                <span className="label">Perfil</span>
                            </Link>
                        </li>
                        
                        {/* Los links dinámicos */}
                        {renderRoleLinks()}
                    </ul>
                </nav>
            </aside>
        </>
    );
}