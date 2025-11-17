import React, { useState } from 'react';
// 1. Importamos Link y useState DE VUELTA
import { Link } from 'react-router-dom'; 
import './EnVivo.css'; 

// --- Componente PostCard (ACTUALIZADO CON CLASES DINÁMICAS) ---
const PostCard = ({ post }) => {
  const [selectedOption, setSelectedOption] = useState(null);

  const handleVote = (e) => {
    e.preventDefault();
    if (selectedOption) {
      console.log(`Votado por opción: ${selectedOption} en post: ${post.id}`);
    } else {
      console.log("No se seleccionó opción");
    }
  };

  return (
    <div className="post-card">
      <div className="post-header">
        <img src={post.avatar || "https://via.placeholder.com/48"} alt="avatar" className="post-avatar" />
        <div className="post-user-info">
          <strong>{post.user}</strong>
          <span>@{post.username}</span>
        </div>
      </div>

      <div className="post-body">
        
        {/* 1. Título (Solo para Encuestas) */}
        {post.tipo === 'encuesta' && post.titulo && (
          <h3 className="post-titulo-encuesta">{post.titulo}</h3>
        )}

        {/* 2. Texto (Para ambos, si existe) */}
        {post.texto && (
          <p className="post-text">{post.texto}</p>
        )}

        {/* 3. Imagen (Solo para Mensajes) */}
        {post.tipo === 'mensaje' && post.imageUrl && (
          <img src={post.imageUrl} alt="Contenido" className="post-main-image" />
        )}
        
        {/* 4. Encuesta (Solo para Encuestas) */}
        {post.tipo === 'encuesta' && (
          <form className="poll-form-x" onSubmit={handleVote}>
            <div className="poll-options-list-x">
              {post.options.map((option) => (
                
                /* =======================================
                 * 👇 CAMBIOS ACÁ: CLASE DINÁMICA Y SPAN NUEVO 👇
                 * =======================================
                 */
                <label 
                  key={option.id} 
                  className={`poll-option-x ${selectedOption === option.id ? 'selected' : ''}`}
                >
                  <input 
                    type="radio" 
                    name={`poll-${post.id}`} 
                    value={option.id}
                    onChange={() => setSelectedOption(option.id)}
                    checked={selectedOption === option.id}
                  />
                  {/* Este es el círculo customizado */}
                  <span className="custom-radio"></span> 
                  
                  <span className="poll-option-text-x">{option.text}</span>
                </label>
              ))}
            </div>
            <button 
              type="submit" 
              className="poll-submit-btn-x" 
              disabled={!selectedOption}
            >
              Votar
            </button>
          </form>
        )}

        {/* 5. Tags (Para ambos, si existen) */}
        {post.tags && post.tags.length > 0 && (
          <div className="post-tags-container">
            {post.tags.map((tag, index) => (
              <span key={index} className="post-tag">
                {tag}
              </span>
            ))}
          </div>
        )}

      </div>

      <div className="post-footer">
        {/* Iconos... */}
      </div>
    </div>
  );
};


// --- Componente Principal de la Página de Encuestas ---
export default function EncuestasEspectador() {
  
  // Datos dummy (ACTUALIZADOS A LA NUEVA ESTRUCTURA)
  const dummyPosts = [
    { 
      id: 1, 
      tipo: 'encuesta', 
      user: 'Canal', 
      username: 'canal_oficial',
      avatar: 'https://via.placeholder.com/48/FFA500/000000?text=C',
      titulo: 'Debate Caliente: ¿Quién tiene razón?', // TÍTULO NUEVO
      texto: 'Vimos el informe y la discusión en el piso, pero queremos saber tu opinión.', // Texto/Pregunta Opcional
      tags: ['#debate', '#noticias', '#vivo'], 
      options: [
        { id: 'a', text: 'El analista A' },
        { id: 'b', text: 'La conductora B' },
        { id: 'c', text: 'Ninguno, ambos exageran' }
      ]
    },
    { 
      id: 2, 
      tipo: 'mensaje', // TIPO MENSAJE
      user: 'Canal', 
      username: 'canal_oficial',
      avatar: 'https://via.placeholder.com/48/FFA500/000000?text=C',
      texto: '¡Tremenda foto del backstage! Miren quién nos visitó hoy.', // Texto Opcional
      tags: ['#backstage', '#invitado'], 
      imageUrl: 'https://via.placeholder.com/600x400' // Imagen Obligatoria
    },
    { 
      id: 3, 
      tipo: 'mensaje', 
      user: 'Canal', 
      username: 'canal_oficial',
      avatar: 'https://via.placeholder.com/48/FFA500/000000?text=C',
      texto: '¡No se olviden que mañana arrancamos 10am!', // Solo texto (backend debe permitirlo)
      tags: ['#programacion', '#importante'], 
      imageUrl: null // Asumimos que si el tipo es 'mensaje' la imagen es obligatoria
    }
  ];

  return (
    // Wrapper principal
    <main className="envivo-main-content">
      
      {/* Botón "¡Ver En Vivo!" (como estaba antes) */}
      <div className="encuestas-header-area">
        <Link to="/en-vivo" className="btn-ver-en-vivo">
          <span className="live-dot-pulse"></span>
          ¡Ver En Vivo!
        </Link>
      </div>

      {/* Este es tu feed de encuestas original */}
      <div className="encuestas-feed-container">
        {/* <NewPostForm /> */}
        
        {dummyPosts.map(post => (
          <PostCard key={post.id} post={post} />
        ))}
      </div>
    </main>
  );
}