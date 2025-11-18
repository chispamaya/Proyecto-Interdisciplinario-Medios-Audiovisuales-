import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../styles/pages/gestionMultimedia.css'; 
import { Video, Music, CheckCircle, AlertCircle, Clock, Trash2 } from 'lucide-react'; 

// Mapeo de estados a íconos y clases
const estadoInfo = {
    aprobado: { icon: CheckCircle, color: 'icon-aprobado', texto: 'Aprobado' },
    pendiente: { icon: Clock, color: 'icon-pendiente', texto: 'Pendiente' },
    rechazado: { icon: AlertCircle, color: 'icon-rechazado', texto: 'Rechazado' },
};

export default function GestionMultimedia() {
    // Estado para almacenar el contenido REAL de la API
    const [contenido, setContenido] = useState([]);
    const [loading, setLoading] = useState(true);
    const [tipoContenidoActivo, setTipoContenidoActivo] = useState('programa');
    const [nombreUsuario, setNombreUsuario] = useState('');
    
    // Función para obtener datos (se ejecutará al cargar y después de borrar)
    const cargarContenido = () => {
        setLoading(true);
        // Obtenemos el ID del usuario logueado del localStorage
        const idUsuario = localStorage.getItem('usuarioId');
        const nombre = localStorage.getItem('usuarioNombre');
        
        if (!idUsuario) {
            setLoading(false);
            console.warn("Usuario no logueado.");
            return;
        }

        setNombreUsuario(nombre); // Mostramos el nombre
        
        // 1. Llamada al endpoint de filtrado por usuario (UsuarioController)
        axios.get(`http://localhost:8080/api/usuarios/gestion/${idUsuario}`)
            .then(response => {
                // response.data ya es la lista de GestionProgramaDTO
                setContenido(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error al cargar la gestión:', error);
                setLoading(false);
            });
    };

    // Al cargar el componente, pedimos los datos
    useEffect(() => {
        cargarContenido();
    }, []);

    // Filtra el contenido basado en el tipo activo (solo se muestran Programas)
    // NOTA: Como tu backend solo devuelve PROGRAMAS (GestionProgramaDTO),
    // el filtro por tipo de contenido es solo una SIMULACIÓN.
    const contenidoFiltrado = contenido.filter(item =>
        // item.tipo === tipoContenidoActivo  <-- ESTO NO ESTÁ EN TU DTO, LO QUITO
        item.titulo !== '' // Filtro básico si necesitas uno
    );

    // Lógica real para eliminar (Llamando al DELETE del ProgramaController)
    const handleEliminar = async (idPrograma) => {
        if (window.confirm(`¿Seguro que quieres eliminar el Programa con ID ${idPrograma}?`)) {
            try {
                // Llama al DELETE /api/programas/{id}
                const response = await axios.delete(`http://localhost:8080/api/programas/${idPrograma}`);
                
                if (response.data === "Programa borrado con éxito.") {
                    alert(`Programa ${idPrograma} eliminado.`);
                    cargarContenido(); // Recargamos la lista
                } else {
                    alert(`No se pudo eliminar: ${response.data}`);
                }
            } catch (error) {
                console.error("Error al eliminar:", error);
                alert("Error de conexión al intentar eliminar.");
            }
        }
    };

    if (loading) return <div className="loading">Cargando tu contenido...</div>;

    return (
        <div className="gestion-container">
            {/* 1. Sección "Tu Contenido" */}
            <div className="tu-contenido-header">
                <h2>Tu contenido</h2>
                <div className="nombre-usuario-display">
                    {nombreUsuario || "Usuario Desconocido"} 
                </div>
            </div>

            {/* 2. Toggle Programa / Audio (Usamos solo la sección de programas ya que es lo que la API devuelve) */}
            <div className="toggle-tipo-contenido">
                {/* Dejamos solo un botón para simplificar, ya que la API solo devuelve programas */}
                <button
                    className={`toggle-btn active`}
                    onClick={() => setTipoContenidoActivo('programa')}
                >
                    <Video size={20} /> Programa
                </button>
            </div>

            {/* 3. Tabla de Contenidos */}
            <div className="tabla-gestion-wrapper">
                <table className="tabla-gestion">
                    <thead>
                        <tr>
                            <th>Título</th>
                            <th>Estado</th>
                            <th>Fecha Creación</th>
                            <th>Duración (min)</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {contenidoFiltrado.length > 0 ? (
                            contenidoFiltrado.map((item) => {
                                // Mapeamos el estado a la información visual
                                const info = estadoInfo[item.estadoAprobacion.toLowerCase()] || estadoInfo.pendiente;
                                const EstadoIcon = info.icon; 

                                return (
                                    <tr key={item.idPrograma}>
                                        <td data-label="Título">{item.titulo}</td>
                                        <td data-label="Estado">
                                            <span className={`estado-cell ${info.color}`}>
                                                <EstadoIcon size={18} />
                                                <span>{item.estadoAprobacion}</span>
                                            </span>
                                        </td>
                                        <td data-label="Fecha">
                                            {new Date(item.fechaCreacion).toLocaleDateString()}
                                        </td>
                                        <td data-label="Duración">{item.duracionEnMinutos} min</td>
                                        <td data-label="Acciones">
                                            <button
                                                className="btn-accion-tabla btn-eliminar"
                                                onClick={() => handleEliminar(item.idPrograma)}
                                                title="Eliminar Programa"
                                            >
                                                <Trash2 size={18} />
                                            </button>
                                        </td>
                                    </tr>
                                );
                            })
                        ) : (
                            <tr>
                                <td colSpan="5" className="no-data-cell">
                                    No has creado programas todavía.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}