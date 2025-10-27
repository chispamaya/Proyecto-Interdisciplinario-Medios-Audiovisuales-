import React, { useState } from 'react';
import { Video, Music, CheckCircle, AlertCircle, Clock, Trash2 } from 'lucide-react'; // Íconos
import '../../styles/pages/gestionMultimedia.css'; // Asegúrate de crear este archivo

// --- Datos de Ejemplo ---
// Reemplaza esto con datos de tu API
const contenidoEjemplo = [
    { id: 1, titulo: "Entrevista Cliente 2024", tipo: 'programa', estado: 'aprobado', fecha: '25/10/2025', duracion: '12:34', tamano: '2.4 GB', usuario: 'Luis Enrique' },
    { id: 2, titulo: "Música de Fondo Vol. 3", tipo: 'audio', estado: 'pendiente', fecha: '24/10/2025', duracion: '05:15', tamano: '50 MB', usuario: 'Luis Enrique' },
    { id: 3, titulo: "Spot Publicitario Navidad", tipo: 'programa', estado: 'rechazado', fecha: '23/10/2025', duracion: '00:30', tamano: '150 MB', usuario: 'Otro Usuario' },
    { id: 4, titulo: "Podcast Episodio 10", tipo: 'audio', estado: 'aprobado', fecha: '22/10/2025', duracion: '45:10', tamano: '120 MB', usuario: 'Luis Enrique' },
    // Agrega más contenido aquí
];

// Mapeo de estados a íconos y clases
const estadoInfo = {
    aprobado: { icon: CheckCircle, color: 'icon-aprobado', texto: 'Aprobado' },
    pendiente: { icon: Clock, color: 'icon-pendiente', texto: 'Pendiente' },
    rechazado: { icon: AlertCircle, color: 'icon-rechazado', texto: 'Rechazado' },
};

export default function GestionMultimedia() {
    // Estado para controlar qué tipo de contenido se muestra ('programa' o 'audio')
    const [tipoContenidoActivo, setTipoContenidoActivo] = useState('programa');
    // Estado para almacenar el contenido (inicialmente con datos de ejemplo)
    const [contenido, setContenido] = useState(contenidoEjemplo);

    // Filtra el contenido basado en el tipo activo y el usuario (ej: Luis Enrique)
    // En una app real, el filtrado por usuario se haría en el backend
    const contenidoFiltrado = contenido.filter(item =>
        item.tipo === tipoContenidoActivo && item.usuario === 'Luis Enrique'
    );

    // Función para manejar la eliminación (simulada)
    const handleEliminar = (id) => {
        // En una app real, usarías confirm() o un modal, no window.confirm
        if (confirm(`¿Estás seguro de que quieres eliminar el contenido con ID ${id}?`)) {
            setContenido(prevContenido => prevContenido.filter(item => item.id !== id));
            console.log(`Contenido ${id} eliminado (simulado)`);
            // Aquí llamarías a la API para eliminar realmente el contenido
        }
    };

    return (
        <div className="gestion-container">
            {/* 1. Sección "Tu Contenido" */}
            <div className="tu-contenido-header">
                <h2>Tu contenido</h2>
                <div className="nombre-usuario-display">
                    Luis Enrique {/* Reemplaza foto por nombre */}
                </div>
            </div>

            {/* 2. Toggle Programa / Audio */}
            <div className="toggle-tipo-contenido">
                <button
                    className={`toggle-btn ${tipoContenidoActivo === 'programa' ? 'active' : ''}`}
                    onClick={() => setTipoContenidoActivo('programa')}
                >
                    <Video size={20} /> Programa
                </button>
                <button
                    className={`toggle-btn ${tipoContenidoActivo === 'audio' ? 'active' : ''}`}
                    onClick={() => setTipoContenidoActivo('audio')}
                >
                    <Music size={20} /> Audio
                </button>
            </div>

            {/* 3. Tabla de Contenidos */}
            <div className="tabla-gestion-wrapper">
                <table className="tabla-gestion">
                    <thead>
                        <tr>
                            <th>Título</th>
                            <th>Estado</th>
                            <th>Fecha</th>
                            <th>Duración</th>
                            <th>Tamaño</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {contenidoFiltrado.length > 0 ? (
                            contenidoFiltrado.map((item) => {
                                const info = estadoInfo[item.estado] || { icon: AlertCircle, color: '', texto: 'Desconocido' };
                                const EstadoIcon = info.icon; // Renombramos para usar como componente

                                return (
                                    <tr key={item.id}>
                                        {/* 💥 Añadido data-label para responsive 💥 */}
                                        <td data-label="Título">{item.titulo}</td>
                                        <td data-label="Estado">
                                            <span className={`estado-cell ${info.color}`}>
                                                <EstadoIcon size={18} />
                                                <span>{info.texto}</span>
                                            </span>
                                        </td>
                                        <td data-label="Fecha">{item.fecha}</td>
                                        <td data-label="Duración">{item.duracion}</td>
                                        <td data-label="Tamaño">{item.tamano}</td>
                                        <td data-label="Acciones"> {/* También a la celda de acciones */}
                                            <button
                                                className="btn-accion-tabla btn-eliminar"
                                                onClick={() => handleEliminar(item.id)}
                                                title="Eliminar"
                                            >
                                                <Trash2 size={18} />
                                            </button>
                                            {/* Puedes añadir botones de editar, ver, etc. aquí */}
                                        </td>
                                    </tr>
                                );
                            })
                        ) : (
                            <tr>
                                <td colSpan="6" className="no-data-cell">
                                    No hay {tipoContenidoActivo === 'programa' ? 'programas' : 'audios'} para mostrar.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

