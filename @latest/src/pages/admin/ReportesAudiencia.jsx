import React, { useState, useEffect } from 'react';
import axios from 'axios'; 
import '../../styles/pages/reportesAudiencia.css';
import { Tags, BarChart3, ThumbsUp, ThumbsDown, RefreshCw, AlertTriangle } from 'lucide-react';

// --- Sub-componente para Reporte de Tags ---
function TagsReport({ data, loading, error, onRetry }) {
    
    if (loading) {
        return (
            <div className="report-content-wrapper state-message-container">
                <RefreshCw size={40} className="spinner-icon" />
                <p>Cargando métricas de tags...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="report-content-wrapper state-message-container">
                <h3 className="error-title">Error al cargar los Tags ({error})</h3>
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

    if (!data || data.length === 0) {
        return (
            <div className="report-content-wrapper state-message-container">
                <AlertTriangle size={48} className="empty-state-icon" />
                <h3 className="empty-state-text">No hay datos de interacción en tags aún.</h3>
            </div>
        );
    }

    return (
        <div className="report-content-wrapper tags-report-container">
            {data.map((tag, index) => (
                <div key={index} className="report-card tag-card">
                    <h3>#{tag.tag}</h3> 
                    
                    <div className="tag-stats">
                        <div className="stat-item likes">
                            <ThumbsUp size={20} />
                            {/* Datos REALES calculados en el backend */}
                            <span>{(tag.likes || 0).toLocaleString()}</span>
                        </div>
                        <div className="stat-item dislikes">
                            <ThumbsDown size={20} />
                            {/* Datos REALES calculados en el backend */}
                            <span>{(tag.dislikes || 0).toLocaleString()}</span>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}

// --- Sub-componente para Reporte de Encuestas (Sin cambios mayores) ---
function PollsReport({ data, loading, error, onRetry }) {
    if (loading) {
        return (
            <div className="report-content-wrapper state-message-container">
                <RefreshCw size={40} className="spinner-icon" />
                <p>Cargando encuestas...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="report-content-wrapper state-message-container">
                <h3 className="error-title">Ocurrió un error al cargar los datos</h3>
                <div className="http-cat-wrapper">
                    <img src={`https://http.cat/${error}`} alt={`Error ${error}`} className="http-cat-image" />
                </div>
                <button className="retry-button" onClick={onRetry}>
                    <RefreshCw size={18} /> Reintentar
                </button>
            </div>
        );
    }

    if (!data || data.length === 0) {
        return (
            <div className="report-content-wrapper state-message-container">
                <AlertTriangle size={48} className="empty-state-icon" />
                <h3 className="empty-state-text">No hay encuestas disponibles.</h3>
            </div>
        );
    }

    const calculatePercentage = (votes, total) => total === 0 ? 0 : (votes / total) * 100;

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
                                            <div className="poll-option-bar-fill" style={{ width: `${percentage}%` }}></div>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                        <div className="poll-total-votes">Total votos: {totalVotes}</div>
                    </div>
                );
            })}
        </div>
    );
}

// --- Componente Principal ---
export default function ReportesAudiencia() {
    const [activeView, setActiveView] = useState('tags');
    
    // Estados Encuestas
    const [pollsData, setPollsData] = useState([]);
    const [loading, setLoading] = useState(false); 
    const [error, setError] = useState(null);

    // Estados Tags
    const [tagsData, setTagsData] = useState([]);
    const [tagsLoading, setTagsLoading] = useState(false);
    const [tagsError, setTagsError] = useState(null);

    // --- FETCH TAGS CON LIKES/DISLIKES ---
    const fetchTags = async () => {
        setTagsLoading(true);
        setTagsError(null);
        try {
            // Llamamos al nuevo endpoint del reporte
            const response = await axios.get('http://localhost:8080/api/tags/reporte');
            setTagsData(response.data); 
        } catch (err) {
            console.error("Error fetching tags report:", err);
            const status = err.response?.status || 503;
            setTagsError(status);
        } finally {
            setTagsLoading(false);
        }
    };

    // --- FETCH ENCUESTAS ---
    const fetchPolls = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get('http://localhost:8080/api/encuestas/reporte-completo');
            const rawData = response.data;
            const pollsMap = {};
            rawData.forEach(row => {
                if (!pollsMap[row.idEncuesta]) {
                    pollsMap[row.idEncuesta] = {
                        id: row.idEncuesta,
                        question: row.preguntar, 
                        options: []
                    };
                }
                if (row.idOpcion) {
                    pollsMap[row.idEncuesta].options.push({
                        id: row.idOpcion,
                        text: row.opcion,
                        votes: row.totalVotos || 0
                    });
                }
            });
            setPollsData(Object.values(pollsMap));
        } catch (err) {
            console.error("Error fetching polls:", err);
            const status = err.response?.status || 503; 
            setError(status); 
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (activeView === 'tags') fetchTags();
    }, [activeView]);

    useEffect(() => {
        if (activeView === 'polls') fetchPolls();
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
                    <TagsReport 
                        data={tagsData}
                        loading={tagsLoading}
                        error={tagsError}
                        onRetry={fetchTags}
                    />
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