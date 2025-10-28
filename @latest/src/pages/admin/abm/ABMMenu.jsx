import React from 'react';
import { Link } from 'react-router-dom';
import '../../../styles/pages/abm_menu.css';

export default function ABMMenu() {
    return (
        <div className="abm-menu-page-content">
            <div className="abm-card">
                <h2 className="abm-card-title">ABM</h2>
                <ul className="abm-options-list">
                    <li><Link to="/abm/programas">Programas</Link></li>
                    <li><Link to="/abm/segmentos">Segmentos</Link></li>
                    <li><Link to="/abm/plataformas">Plataformas</Link></li>
                    <li><Link to="/abm/empleados">Empleados</Link></li>
                </ul>
            </div>
        </div>
    );
}