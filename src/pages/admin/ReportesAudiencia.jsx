import React, { useState } from 'react';
// Importamos los estilos para esta página
import '../../styles/pages/reportesAudiencia.css';
// Importamos íconos de Lucide, que ya usas en el proyecto
import { Tags, BarChart3, ThumbsUp, ThumbsDown } from 'lucide-react';

// --- Datos de Ejemplo (Mock Data) ---

// Datos para la sección de Tags
const MOCK_TAGS_DATA = [
    { id: 1, name: 'Noticias', likes: 1820, dislikes: 150 },
    { id: 2, name: 'Deportes', likes: 2500, dislikes: 300 },
    { id: 3, name: 'Entretenimiento', likes: 5300, dislikes: 420 },
    { id: 4, name: 'Música', likes: 1200, dislikes: 80 },
    { id: 5, name: 'Política', likes: 450, dislikes: 900 },
    { id: 6, name: 'Cultura', likes: 780, dislikes: 50 },
    { id: 7, name: 'Gaming', likes: 3100, dislikes: 210 },
];

// Datos para la sección de Encuestas
const MOCK_POLLS_DATA = [
    {
        id: 1,
        question: '¿Qué tipo de contenido prefieres ver los fines de semana?',
        options: [
            { id: 'a', text: 'Películas y Series', votes: 480 },
            { id: 'b', text: 'Documentales', votes: 210 },
            { id: 'c', text: 'Programas en vivo', votes: 350 },
            { id: 'd', text: 'Repeticiones de la semana', votes: 90 },
        ],
    },
    {
        id: 2,
        question: '¿Cuál es tu plataforma principal para ver nuestro contenido?',
        options: [
            { id: 'a', text: 'Canal de TV (Aire)', votes: 620 },
            { id: 'b', text: 'Sitio Web (Streaming)', votes: 710 },
            { id: 'c', text: 'App Móvil', votes: 310 },
        ],
    },
    {
        id: 3,
        question: '¿Con qué frecuencia ves nuestro noticiero?',
        options: [
            { id: 'a', text: 'Todos los días', votes: 850 },
            { id: 'b', text: '2-3 veces por semana', votes: 420 },
            { id: 'c', text: 'Rara vez', votes: 110 },
        ],
    },
];

// --- Sub-componente para Reporte de Tags ---
function TagsReport() {
    return (
        <div className="report-content-wrapper tags-report-container">
            {/* Mapeamos los datos de ejemplo */}
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

// --- Sub-componente para Reporte de Encuestas ---
function PollsReport() {
    
    const calculatePercentage = (votes, total) => {
        if (total === 0) return 0;
        return (votes / total) * 100;
    };

    return (
        <div className="report-content-wrapper polls-report-container">
            {/* Mapeamos cada encuesta */}
            {MOCK_POLLS_DATA.map(poll => {
                // Calculamos el total de votos POR encuesta para la barra de %
                const totalVotes = poll.options.reduce((sum, option) => sum + option.votes, 0);

                return (
                    <div key={poll.id} className="report-card poll-card">
                        <h4>{poll.question}</h4>
                        <div className="poll-options-list">
                            {/* Mapeamos las opciones de esa encuesta */}
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
                    </div>
                );
            })}
        </div>
    );
}


// --- Componente Principal de la Página ---
export default function ReportesAudiencia() {
    // Estado para saber qué vista mostrar: 'tags' o 'polls'
    const [activeView, setActiveView] = useState('tags');

    return (
        <div className="reportes-container">
            
            {/* 1. Sidebar de Toggles (Botones) */}
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

            {/* 2. Área de Contenido Principal */}
            <main className="reportes-content">
                <h2 className="reportes-title">Reportes de Audiencia</h2>
                
                {/* Renderizado condicional basado en el estado */}
                {activeView === 'tags' ? <TagsReport /> : <PollsReport />}
            </main>

        </div>
    );
}