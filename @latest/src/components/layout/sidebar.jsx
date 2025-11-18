// src/components/layout/sidebar.jsx

import React, { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import axios from 'axios'; 
import "../../styles/layout/sidebar.css";

import {
    Users, UploadCloud, CheckCircle, CalendarDays, Folders, 
    Zap, AlertTriangle, Calendar, UserCog, BarChart, PlusSquare, FileSearch
} from 'lucide-react';

// --- IDs de permisos en Base de Datos ---
const PERMISOS = {
    SUBIR_CONTENIDO: 1,
    ESTADO_APROBACION: 2,
    VER_PARRILLA: 3,
    ADMIN_TOTAL: 4,      
    ARMAR_PARRILLA: 5,
    CONTROLAR_EMISION: 6,
    GESTION_MULTIMEDIA: 1 
};

export default function SideBar() {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    
    const [permisosUsuario, setPermisosUsuario] = useState([]); 
    const [esAdmin, setEsAdmin] = useState(false);
    const [cargando, setCargando] = useState(true);

    useEffect(() => {
        // 1. Obtenemos SOLO el ID del usuario guardado al loguearse
        const idUsuarioLogueado = localStorage.getItem('usuarioId'); 

        if (idUsuarioLogueado) {
            console.log("🔍 Usuario ID detectado:", idUsuarioLogueado);

            // 2. PRIMERA CONSULTA: Averiguar el ROL de este usuario
            axios.get(`http://localhost:8080/api/usuarios/${idUsuarioLogueado}`)
                .then(respuestaUsuario => {
                    const usuario = respuestaUsuario.data;
                    const rolDelUsuario = usuario.idRol; // <--- Aquí obtenemos el rol fresco de la DB
                    
                    console.log("👤 El usuario tiene el Rol ID:", rolDelUsuario);

                    // 3. SEGUNDA CONSULTA: Averiguar qué PERMISOS tiene ese rol
                    return axios.get('http://localhost:8080/api/roles/asignaciones')
                        .then(respuestaAsignaciones => {
                            const todasLasAsignaciones = respuestaAsignaciones.data;

                            // Filtramos los permisos que coinciden con el rol del usuario
                            const misPermisos = todasLasAsignaciones
                                .filter(a => a.idRol === rolDelUsuario)
                                .map(a => a.idPermiso);

                            console.log("✅ Permisos calculados:", misPermisos);
                            
                            setPermisosUsuario(misPermisos);

                            if (misPermisos.includes(PERMISOS.ADMIN_TOTAL)) {
                                setEsAdmin(true);
                            }
                            setCargando(false);
                        });
                })
                .catch(err => {
                    console.error("❌ Error obteniendo datos del usuario o roles:", err);
                    setCargando(false);
                });
        } else {
            // No hay usuario logueado
            setCargando(false);
        }
    }, []);

    const menuClasses = `sub-header ${isMenuOpen ? 'visible' : ''}`;
    const handleLinkClick = () => setIsMenuOpen(false);

    const puedeVer = (idPermisoRequerido) => {
        return esAdmin || permisosUsuario.includes(idPermisoRequerido);
    };

    if (cargando) return null; 

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
                        {/* Perfil siempre visible */}
                        <li>
                            <Link to="/perfil" className="menu-item " onClick={handleLinkClick}>
                                <Users size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                <span className="label">Perfil</span>
                            </Link>
                        </li>

                        {/* --- ADMINISTRADOR --- */}
                        {esAdmin && (
                            <>
                                <p className="panel-titulo">ADMINISTRACIÓN</p>
                                <li>
                                    <Link to="/admin/crear-publicacion" className="menu-item" onClick={handleLinkClick}>
                                        <PlusSquare size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                        <span className="label">Crear Publicación</span>
                                    </Link>
                                </li>
                                <li>
                                    <Link to="/abm" className="menu-item" onClick={handleLinkClick}>
                                        <UserCog size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                        <span className="label">Menú ABM</span>
                                    </Link>
                                </li>
                                <li>
                                    <Link to="/admin/auditoria" className="menu-item" onClick={handleLinkClick}>
                                        <FileSearch size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                        <span className="label">Auditoria</span>
                                    </Link>
                                </li>
                            </>
                        )}

                        {/* --- PRODUCCIÓN --- */}
                        {(puedeVer(PERMISOS.SUBIR_CONTENIDO) || puedeVer(PERMISOS.ESTADO_APROBACION)) && (
                            <p className="panel-titulo">PRODUCCIÓN</p>
                        )}

                        {puedeVer(PERMISOS.SUBIR_CONTENIDO) && (
                            <li>
                                <Link to="/subida" className="menu-item" onClick={handleLinkClick}>
                                    <UploadCloud size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Subida multimedia</span>
                                </Link>
                            </li>
                        )}

                        {puedeVer(PERMISOS.ESTADO_APROBACION) && (
                            <li>
                                <Link to="/estado/1" className="menu-item" onClick={handleLinkClick}>
                                    <CheckCircle size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Estado y aprobación</span>
                                </Link>
                            </li>
                        )}

                        {puedeVer(PERMISOS.VER_PARRILLA) && (
                            <li>
                                <Link to="/parrilla" className="menu-item" onClick={handleLinkClick}>
                                    <CalendarDays size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Parrilla semanal</span>
                                </Link>
                            </li>
                        )}

                        {puedeVer(PERMISOS.GESTION_MULTIMEDIA) && (
                            <li>
                                <Link to="/gestion" className="menu-item" onClick={handleLinkClick}>
                                    <Folders size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Gestión multimedia</span>
                                </Link>
                            </li>
                        )}

                        {/* REPORTES */}
                        {(puedeVer(PERMISOS.ADMIN_TOTAL) || esAdmin) && (
                            <>
                                <p className="panel-titulo">REPORTES</p>
                                <li>
                                    <Link to="/reportes" className="menu-item" onClick={handleLinkClick}>
                                        <BarChart size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                        <span className="label">Reportes Audiencia</span>
                                    </Link>
                                </li>
                            </>
                        )}

                        {/* --- PROGRAMACIÓN --- */}
                        {(puedeVer(PERMISOS.ARMAR_PARRILLA) || puedeVer(PERMISOS.CONTROLAR_EMISION)) && (
                            <p className="panel-titulo">PROGRAMACIÓN</p>
                        )}

                        {puedeVer(PERMISOS.CONTROLAR_EMISION) && (
                            <>
                                <li>
                                    <Link to="/controlEmision" className="menu-item" onClick={handleLinkClick}>
                                        <Zap size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                        <span className="label">Control de Emisión</span>
                                    </Link>
                                </li>
                                <li>
                                    <Link to="/errores" className="menu-item" onClick={handleLinkClick}>
                                        <AlertTriangle size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                        <span className="label">Errores</span>
                                    </Link>
                                </li>
                            </>
                        )}

                        {puedeVer(PERMISOS.ARMAR_PARRILLA) && (
                            <li>
                                <Link to="/armadoParrilla" className="menu-item" onClick={handleLinkClick}>
                                    <Calendar size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                                    <span className="label">Armado de Parrilla</span>
                                </Link>
                            </li>
                        )}

                    </ul>
                </nav>
            </aside>
        </>
    );
}