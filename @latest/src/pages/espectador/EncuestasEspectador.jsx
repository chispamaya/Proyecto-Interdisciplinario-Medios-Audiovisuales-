import React, { useState } from 'react';
// 1. Importamos Link y AÑADIMOS useNavigate
import { Link, useNavigate } from 'react-router-dom'; 
import './EnVivo.css'; 

// --- Componente PostCard ---
// 2. Recibe 'isLoggedIn' como prop
const PostCard = ({ post, isLoggedIn }) => {
  const [selectedOption, setSelectedOption] = useState(null);
  
  // 3. Estado para mostrar el modal de inicio de sesión
  const [showLoginPrompt, setShowLoginPrompt] = useState(false);
  const navigate = useNavigate();

  // 4. Lógica de Votación (Actualizada con Auth)
  const handleVote = (e) => {
    e.preventDefault();
    
    // Si no está logueado, muestra el prompt y detiene
    if (!isLoggedIn) {
      setShowLoginPrompt(true);
      return;
    }
    
    if (selectedOption) {
      console.log(`Votado por opción: ${selectedOption} en post: ${post.id}`);
    } else {
      console.log("No se seleccionó opción");
    }
  };

  // 5. Función para redirigir al login
  const handleGoToLogin = () => {
    // Asumimos que la ruta de login del espectador es esta
    navigate('/login-espectador'); 
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
        
        {/* Título (Solo para Encuestas) */}
        {post.tipo === 'encuesta' && post.titulo && (
          <h3 className="post-titulo-encuesta">{post.titulo}</h3>
        )}

        {/* Texto (Para ambos, si existe) */}
        {post.texto && (
          <p className="post-text">{post.texto}</p>
        )}

        {/* Imagen (Solo para Mensajes) */}
        {post.tipo === 'mensaje' && post.imageUrl && (
          <img src={post.imageUrl} alt="Contenido" className="post-main-image" />
        )}
        
        {/* Encuesta (Solo para Encuestas) */}
        {post.tipo === 'encuesta' && (
          <form className="poll-form-x" onSubmit={handleVote}>
            <div className="poll-options-list-x">
              {post.options.map((option) => (
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

        {/* Tags (SOLO PARA MENSAJES, si existen) */}
        {post.tipo === 'mensaje' && post.tags && post.tags.length > 0 && (
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
        {/* Dejado vacío como en tu CSS */}
      </div>

      {/* 7. NUEVO: Modal de Inicio de Sesión (CON DOS BOTONES, SIN 'X') */}
      {showLoginPrompt && (
        <div className="login-prompt-overlay">
          <div className="login-prompt-box">
            {/* 'X' ELIMINADA */}
            <p>ups.. no has iniciado sesion ,inicia para disfrutar de todas las funciones del usuario</p>
            
            {/* NUEVO CONTENEDOR DE BOTONES */}
            <div className="login-prompt-actions">
              <button 
                className="login-prompt-btn-later" 
                onClick={() => setShowLoginPrompt(false)}
              >
                Más Tarde
              </button>
              <button 
                className="login-prompt-btn-login" 
                onClick={handleGoToLogin}
              >
                Iniciar Sesión
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};


// --- Componente Principal de la Página de Encuestas ---
export default function EncuestasEspectador() {
  
  // 8. LÓGICA DE AUTH:
  // ¡IMPORTANTE! Reemplaza esto con tu lógica de autenticación real
  // (Por ejemplo, de tu Contexto de Auth: const { user } = useAuth(); const isLoggedIn = !!user;)
  const isLoggedIn = false; 
  // (Pone 'true' o 'false' acá para probar)

  
  // Datos dummy
  const dummyPosts = [
    { 
      id: 1, 
      tipo: 'encuesta', 
      user: 'Canal', 
      username: 'canal_oficial',
      avatar: 'https://via.placeholder.com/48/FFA500/000000?text=C',
      titulo: 'Debate Caliente: ¿Quién tiene razón?', // TÍTULO NUEVO
      texto: 'Vimos el informe y la discusión en el piso, pero queremos saber tu opinión.', // Texto/Pregunta Opcional
      tags: ['#debate', '#noticias', '#vivo'], // Estos tags ya no se mostrarán
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
      tags: ['#backstage', '#invitado'], // Estos tags SÍ se mostrarán
      imageUrl: 'https://via.placeholder.com/600x400' // Imagen ObligatorIA
    },
    { 
      id: 3, 
      tipo: 'mensaje', 
      user: 'Canal', 
      username: 'canal_oficial',
      avatar: 'https://via.placeholder.com/48/FFA500/000000?text=C',
      texto: '¡No se olviden que mañana arrancamos 10am!', // Solo texto (backend debe permitirlo)
      tags: ['#programacion', '#importante'], // Estos tags SÍ se mostrarán
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
          // 9. Pasamos el estado de login al PostCard
          <PostCard 
            key={post.id} 
            post={post} 
            isLoggedIn={isLoggedIn} 
          />
        ))}
      </div>
    </main>
  );
}