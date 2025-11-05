import React, { useState } from 'react';
// 1. Importa el CSS
import './EnVivo.css'; 
// 2. Importa el logo
import logoPrograma from '../../assets/logo.png'; 
// 3. 💥 Importamos solo los íconos necesarios 💥
import { 
    ThumbsUp, 
    ThumbsDown, 
    BarChart2 
} from 'lucide-react';

// --- DATOS DE EJEMPLO ---

const ratingData = {
    id: 1,
    user: { name: 'Bendita', handle: '@BenditaTV', avatar: logoPrograma },
    text: '¡Gracias por vernos! ¿Qué te pareció el programa de hoy?',
    image: 'https://i.imgur.com/gA3gCqC.png',
    likes: 1200,
    dislikes: 345,
};

const pollData = {
    id: 2,
    user: { name: 'My ⚙️', handle: '@MyCanalOficial', avatar: logoPrograma },
    question: '¿Qué sección nueva te gustaría ver en el noticiero?',
    options: [
        { id: 'op1', text: 'Tecnología y Futuro' },
        { id: 'op2', text: 'Historias Inspiradoras' },
        { id: 'op3', text: 'Ecología y Medio Ambiente' },
    ],
};


// --- COMPONENTES DE POST ---

/**
 * 1. Componente para el Post de RATING (Like/Dislike)
 */
const RatingPost = ({ post }) => {
    const totalVotes = post.likes + post.dislikes;
    const likePercentage = (post.likes / totalVotes) * 100;

    return (
        <div className="post-card">
            {/* Header del Post */}
            <div className="post-header">
                <img src={post.user.avatar} alt={post.user.name} className="post-avatar" />
                <div className="post-user-info">
                    <strong>{post.user.name}</strong>
                    <span>{post.user.handle} · 2h</span>
                </div>
            </div>
            {/* Cuerpo del Post */}
            <div className="post-body">
                <p className="post-text">{post.text}</p>
                <img src={post.image} alt="Imagen del Post" className="post-main-image" />
                {/* Barra de Like/Dislike */}
                <div className="rating-bar">
                    <button className="rating-btn like" title="Me gusta">
                        <ThumbsUp size={18} /> <span>{post.likes}</span>
                    </button>
                    <div className="rating-progress-bar" title={`${likePercentage.toFixed(0)}% de aprobación`}>
                        <div className="progress" style={{ width: `${likePercentage}%` }}></div>
                    </div>
                    <button className="rating-btn dislike" title="No me gusta">
                        <ThumbsDown size={18} /> <span>{post.dislikes}</span>
                    </button>
                </div>
            </div>
            {/* 💥 ELIMINAMOS EL FOOTER POR COMPLETO 💥 */}
            {/* (La acción principal ya está en la barra de rating) */}
        </div>
    );
};

/**
 * 2. Componente para el Post de ENCUESTA
 */
const PollPost = ({ post }) => {
    const [selectedOption, setSelectedOption] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (selectedOption) {
            alert(`Votaste por: "${selectedOption}". ¡Gracias!`);
        } else {
            alert("Por favor, selecciona una opción.");
        }
    };

    return (
        <div className="post-card">
            {/* Header del Post */}
            <div className="post-header">
                <img src={post.user.avatar} alt={post.user.name} className="post-avatar" />
                <div className="post-user-info">
                    <strong>{post.user.name}</strong>
                    <span>{post.user.handle} · 5h</span>
                </div>
            </div>
            {/* Cuerpo del Post */}
            <div className="post-body">
                <p className="post-text">{post.question}</p>
                {/* Formulario de Encuesta (Estilo X) */}
                <form className="poll-form-x" onSubmit={handleSubmit}>
                    <div className="poll-options-list-x">
                        {post.options.map((option) => (
                            <label key={option.id} className="poll-option-x">
                                <input 
                                    type="radio" 
                                    name="poll-generic" 
                                    value={option.text}
                                    checked={selectedOption === option.text}
                                    onChange={() => setSelectedOption(option.text)}
                                />
                                <span className="poll-option-text-x">{option.text}</span>
                            </label>
                        ))}
                    </div>
                    <button type="submit" className="poll-submit-btn-x">
                        Votar
                    </button>
                </form>
            </div>
            {/* 💥 FOOTER SIMPLIFICADO 💥 */}
            <div className="post-footer">
                <button className="footer-icon" title="Ver resultados">
                    <BarChart2 size={18} />
                </button>
            </div>
        </div>
    );
};


// --- PÁGINA PRINCIPAL (EL FEED) ---
export default function EncuestasEspectador() {
  return (
    // Contenedor principal del Feed
    <main className="encuestas-feed-container">
        
        {/* Feed de Posts */}
        <RatingPost post={ratingData} />
        <PollPost post={pollData} />
        
    </main>
  );
}