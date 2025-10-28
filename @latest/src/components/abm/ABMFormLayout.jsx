import React from 'react';

/**
 * Componente de layout reutilizable para los formularios de ABM (Editar/Añadir).
 * Aplica el estilo de tarjeta oscura y los botones de acción estándar.
 * @param {string} title - El título del formulario (ej: "Programas").
 * @param {function} onSubmit - Función a ejecutar al guardar (se pasa al form).
 * @param {function} onDiscard - Función a ejecutar al descartar/cancelar.
 * @param {JSX.Element} children - Contenido del formulario (los campos específicos).
 */
export default function ABMFormLayout({ title, onSubmit, onDiscard, children }) {

    return (
        <div className="abm-form-page-content">
            <h1 className="form-page-title"><span>{title}</span></h1>
            
            <form className="abm-form-card" onSubmit={onSubmit}>
                {/* Contenido dinámico (campos del formulario) */}
                <div className="form-fields-wrapper">
                    {children}
                </div>
                
                {/* Botones de acción */}
                <div className="form-action-buttons">
                    <button type="button" className="btn-descartar-form" onClick={onDiscard}>
                        Descartar cambios
                    </button>
                    <button type="submit" className="btn-guardar-form">
                        Guardar cambios
                    </button>
                </div>
            </form>
        </div>
    );
}