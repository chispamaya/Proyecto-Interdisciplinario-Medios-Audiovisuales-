import React, { useState, useEffect } from 'react';
// Importamos los estilos para esta página
import '../../styles/pages/reportesAudiencia.css';
// Importamos íconos de Lucide
import { Tags, BarChart3, ThumbsUp, ThumbsDown, RefreshCw, AlertTriangle } from 'lucide-react';

// --- Datos de Ejemplo (Mock Data) - SOLO PARA TAGS (Se mantiene igual) ---
const MOCK_TAGS_DATA = [
    { id: 1, name: 'Noticias', likes: 1820, dislikes: 150 },
    { id: 2, name: 'Deportes', likes: 2500, dislikes: 300 },
    { id: 3, name: 'Entretenimiento', likes: 5300, dislikes: 420 },
    { id: 4, name: 'Música', likes: 1200, dislikes: 80 },
    { id: 5, name: 'Política', likes: 450, dislikes: 900 },
    { id: 6, name: 'Cultura', likes: 780, dislikes: 50 },
    { id: 7, name: 'Gaming', likes: 3100, dislikes: 210 },
];

// --- Sub-componente para Reporte de Tags (Estático) ---
function TagsReport() {
    return (
        <div className="report-content-wrapper tags-report-container">
            {MOCK_TAGS_DATA.map(tag => (
                <div key={tag.id} className="report-card tag-card">
                    <h3>{tag.name}</h3>
                    <div className="tag-stats">
                        <div className="stat-item likes">
                            <ThumbsUp size={20} />
                            <span>{tag.likes.toLocaleString()}</span>
                        </div>
                        <div className="stat-item dislikes">
                            <ThumbsDown size={20} />
                            <span>{tag.dislikes.toLocaleString()}</span>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}

// --- Sub-componente para Reporte de Encuestas (Conectado al Backend) ---
function PollsReport({ data, loading, error, onRetry }) {
    
    // 1. Estado de Carga
    if (loading) {
        return (
            <div className="report-content-wrapper state-message-container">
                <RefreshCw size={40} className="spinner-icon" />
                <p>Cargando encuestas...</p>
            </div>
        );
    }

    // 2. Estado de Error (Con HTTP Cats)
    if (error) {
        return (
            <div className="report-content-wrapper state-message-container">
                <h3 className="error-title">Ocurrió un error al cargar los datos</h3>
                <div className="http-cat-wrapper">
                    <img 
                        src={`https://http.cat/${error}`} 
                        alt={`Error ${error}`} 
                        className="http-cat-image"
                    />
                </div>
                <button className="retry-button" onClick={onRetry}>
                    <RefreshCw size={18} /> Reintentar
                </button>
            </div>
        );
    }

    // 3. Estado Vacío (No hay encuestas)
    if (!data || data.length === 0) {
        return (
            <div className="report-content-wrapper state-message-container">
                <AlertTriangle size={48} className="empty-state-icon" />
                <h3 className="empty-state-text">No hay encuestas disponibles en este momento.</h3>
            </div>
        );
    }

    // 4. Renderizado de Datos
    const calculatePercentage = (votes, total) => {
        if (total === 0) return 0;
        return (votes / total) * 100;
    };

    return (
        <div className="report-content-wrapper polls-report-container">
            {data.map(poll => {
                const totalVotes = poll.options.reduce((sum, option) => sum + option.votes, 0);

                return (
                    <div key={poll.id} className="report-card poll-card">
                        <h4>{poll.question}</h4>
                        <div className="poll-options-list">
                            {poll.options.map(option => {
                                const percentage = calculatePercentage(option.votes, totalVotes);
                                return (
                                    <div key={option.id} className="poll-option">
                                        <div className="poll-option-info">
                                            <span className="option-text">{option.text}</span>
                                            <span className="option-votes">{option.votes.toLocaleString()} Votos</span>
                                        </div>
                                        <div className="poll-option-bar-bg">
                                            <div 
                                                className="poll-option-bar-fill" 
                                                style={{ width: `${percentage}%` }}
                                            ></div>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                        <div className="poll-total-votes">
                            Total votos: {totalVotes}
                        </div>
                    </div>
                );
            })}
        </div>
    );
}

// --- Componente Principal ---
export default function ReportesAudiencia() {
    const [activeView, setActiveView] = useState('tags');
    
    // Estados del Backend
    const [pollsData, setPollsData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchPolls = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch('http://localhost:8080/api/encuestas/reporte-completo');
            
            if (!response.ok) {
                // Lanzamos el status para que lo capture el catch y muestre el gato correspondiente
                throw { status: response.status };
            }

            // Recibimos una lista plana de filas (DTO EncuestaResultado)
            const rawData = await response.json();

            // --- Lógica de Agrupación ---
            // El backend devuelve filas individuales por cada opción.
            // Necesitamos agruparlas por 'idEncuesta'.
            const pollsMap = {};

            rawData.forEach(row => {
                // Si la encuesta no existe aún en el mapa, la creamos
                if (!pollsMap[row.idEncuesta]) {
                    pollsMap[row.idEncuesta] = {
                        id: row.idEncuesta,
                        // Usamos el campo 'preguntar' del DTO Java
                        question: row.preguntar, 
                        options: []
                    };
                }

                // Agregamos la opción actual a la encuesta correspondiente
                // Verificamos que idOpcion no sea nulo (por si acaso)
                if (row.idOpcion) {
                    pollsMap[row.idEncuesta].options.push({
                        id: row.idOpcion,
                        // Usamos el campo 'opcion' del DTO Java
                        text: row.opcion,
                        // Usamos el campo 'totalVotos' del DTO Java
                        votes: row.totalVotos || 0
                    });
                }
            });

            // Convertimos el mapa de objetos de nuevo a un array
            const mappedData = Object.values(pollsMap);

            setPollsData(mappedData);

        } catch (err) {
            console.error("Error fetching polls:", err);
            // Si el error tiene status (ej: 404, 500) úsalo, si no (ej: red caída) usa 503
            setError(err.status || 503); 
        } finally {
            setLoading(false);
        }
    };

    // Cargar datos al cambiar a la pestaña 'polls'
    useEffect(() => {
        if (activeView === 'polls') {
            fetchPolls();
        }
    }, [activeView]);

    return (
        <div className="reportes-container">
            <aside className="reportes-sidebar">
                <button 
                    className={`reportes-toggle-btn ${activeView === 'tags' ? 'active' : ''}`}
                    onClick={() => setActiveView('tags')}
                >
                    <Tags size={20} />
                    <span>Reporte por Tags</span>
                </button>
                <button 
                    className={`reportes-toggle-btn ${activeView === 'polls' ? 'active' : ''}`}
                    onClick={() => setActiveView('polls')}
                >
                    <BarChart3 size={20} />
                    <span>Reporte de Encuestas</span>
                </button>
            </aside>

            <main className="reportes-content">
                <h2 className="reportes-title">Reportes de Audiencia</h2>
                
                {activeView === 'tags' ? (
                    <TagsReport />
                ) : (
                    <PollsReport 
                        data={pollsData} 
                        loading={loading} 
                        error={error} 
                        onRetry={fetchPolls}
                    />
                )}
            </main>
        </div>
    );
}