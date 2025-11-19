import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ThumbsUp, ThumbsDown } from 'lucide-react'; 
import './EnVivo.css';

const AuthorInfo = ({ userId }) => {
  const [nombre, setNombre] = useState("Usuario");
  useEffect(() => {
    if (!userId) return;
    fetch(`http://localhost:8080/api/usuarios/${userId}`)
      .then(res => res.ok ? res.json() : null)
      .then(data => { if(data) setNombre(data.nombre || `Usuario ${userId}`); })
      .catch(console.error);
  }, [userId]);

  return (
    <div className="post-user-info">
      <strong>{nombre}</strong>
    </div>
  );
};

const PostCard = ({ post, tagsMap }) => {
  const [selectedOption, setSelectedOption] = useState(post.userVotedOptionId || null);
  const [showLoginPrompt, setShowLoginPrompt] = useState(false);
  const navigate = useNavigate();

  const initialReaction = post.userReaction === true ? 'like' : (post.userReaction === false ? 'dislike' : null);
  const [userReaction, setUserReaction] = useState(initialReaction);
  
  const [likes, setLikes] = useState(0); 
  const [dislikes, setDislikes] = useState(0);

  const esEncuesta = post.tipo === 'ENCUESTA';
  const esContenido = post.tipo === 'CONTENIDO';
  const detalle = post.detalle || {};
  const creadorId = esEncuesta ? detalle.idCreador : detalle.idUsuario;
  const tagIds = detalle.tags || [];
  const mediaUrl = detalle.rutaArchivo ? `http://localhost:8080${detalle.rutaArchivo}` : null;

  const getAuthData = () => {
    const userStr = localStorage.getItem('usuario');
    return userStr ? JSON.parse(userStr) : null;
  };

  const handleVoteClick = async (idOpcion) => {
    const user = getAuthData();
    if (!user) { setShowLoginPrompt(true); return; }

    const esMismaOpcion = selectedOption === idOpcion;
    setSelectedOption(esMismaOpcion ? null : idOpcion);
    
    try {
        await fetch(`http://localhost:8080/api/encuestas/votar?idUsuarioAuditoria=${user.id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                idOpcion: idOpcion,
                idUsuario: user.id,
                idEncuesta: post.idReal
            })
        });
    } catch (error) {
        console.error("Error votando:", error);
        setSelectedOption(selectedOption); 
    }
  };

  const handleReaction = async (tipoClickeado) => {
    const user = getAuthData();
    if (!user) { setShowLoginPrompt(true); return; }

    const idContenido = post.idReal;
    let endpoint = "";
    let body = {};

    if (userReaction === tipoClickeado) {
        if (tipoClickeado === 'like') setLikes(l => Math.max(0, l - 1));
        else setDislikes(d => Math.max(0, d - 1));
        setUserReaction(null);
        
        endpoint = "borrarValoracion";
        body = { idContenido, idUsuario: user.id };
    } else {
        if (tipoClickeado === 'like') {
            setLikes(l => l + 1);
            if (userReaction === 'dislike') setDislikes(d => Math.max(0, d - 1));
        } else {
            setDislikes(d => d + 1);
            if (userReaction === 'like') setLikes(l => Math.max(0, l - 1));
        }
        setUserReaction(tipoClickeado);

        endpoint = "valorar";
        body = { idContenido, idUsuario: user.id, esLike: (tipoClickeado === 'like') };
    }

    try {
        await fetch(`http://localhost:8080/api/contenido/${endpoint}?idUsuarioAuditoria=${user.id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
    } catch (e) {
        console.error("Error reacción:", e);
    }
  };

  const handleGoToLogin = () => navigate('/login-espectador');

  return (
    <div className="post-card">
      <div className="post-header">
        <AuthorInfo userId={creadorId} />
      </div>

      <div className="post-body">
        {esEncuesta && post.detalle.preguntar && <h3 className="post-titulo-encuesta">{post.detalle.preguntar}</h3>}
        {detalle.texto && <p className="post-text">{detalle.texto}</p>}
        
        {mediaUrl && esContenido && (
            <img src={mediaUrl} alt="Contenido" className="post-main-image" />
        )}
        
        {esEncuesta && post.opciones && (
          <div className="poll-form-x">
            <div className="poll-options-list-x">
              {post.opciones.map((opcion) => (
                <div 
                  key={opcion.idOpcion} 
                  className={`poll-option-x ${selectedOption === opcion.idOpcion ? 'selected' : ''}`}
                  onClick={() => handleVoteClick(opcion.idOpcion)}
                >
                  <input type="radio" readOnly checked={selectedOption === opcion.idOpcion} />
                  <span className="custom-radio"></span> 
                  <span className="poll-option-text-x">{opcion.texto}</span>
                </div>
              ))}
            </div>
          </div>
        )}

        {esContenido && tagIds.length > 0 && (
            <div className="post-tags-container">
                {tagIds.map((tagId, idx) => (
                    <span key={idx} className="post-tag">#{tagsMap[Number(tagId)] || tagId}</span>
                ))}
            </div>
        )}
      </div>

      {esContenido && (
        <div className="post-footer">
          <button 
            className={`reaction-btn ${userReaction === 'like' ? 'liked' : ''}`} 
            onClick={() => handleReaction('like')}
            title="Me gusta"
          >
              <ThumbsUp size={20} /> 
          </button>
          <button 
            className={`reaction-btn ${userReaction === 'dislike' ? 'disliked' : ''}`} 
            onClick={() => handleReaction('dislike')}
            title="No me gusta"
          >
              <ThumbsDown size={20} />
          </button>
        </div>
      )}

      {showLoginPrompt && (
        <div className="login-prompt-overlay">
          <div className="login-prompt-box">
            <p>Inicia sesión para interactuar.</p>
            <div className="login-prompt-actions">
              <button className="login-prompt-btn-later" onClick={() => setShowLoginPrompt(false)}>Cancelar</button>
              <button className="login-prompt-btn-login" onClick={handleGoToLogin}>Login</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default function EncuestasEspectador() {
  const [posts, setPosts] = useState([]);
  const [tagsMap, setTagsMap] = useState({});
  const [errorStatus, setErrorStatus] = useState(null);

  useEffect(() => {
    const loadData = async () => {
        try {
            const userStr = localStorage.getItem('usuario');
            const user = userStr ? JSON.parse(userStr) : null;
            const userIdParam = user ? `?idUsuario=${user.id}` : '';

            const tagsRes = await fetch('http://localhost:8080/api/tags');
            if (tagsRes.ok) {
                const tagsData = await tagsRes.json();
                const mapa = {};
                tagsData.forEach(t => mapa[Number(t.id)] = t.tag);
                setTagsMap(mapa);
            }

            const feedRes = await fetch(`http://localhost:8080/api/feed${userIdParam}`);
            if (!feedRes.ok) throw new Error(feedRes.status);
            const rawFeed = await feedRes.json();
            setPosts(procesarFeed(rawFeed));
        } catch (error) {
            console.error(error);
            setErrorStatus(503); // Asumimos error de conexión si falla
        }
    };
    loadData();
  }, []);

  const procesarFeed = (data) => {
    const encuestasMap = new Map();
    const contenidos = [];

    data.forEach(item => {
        if (item.tipo === 'CONTENIDO') {
            contenidos.push({
                uniqueId: `content-${item.id}`,
                idReal: item.detalle.id,
                tipo: 'CONTENIDO',
                fecha: item.fechaCreacion,
                detalle: item.detalle,
                userReaction: item.detalle.miReaccion 
            });
        } else if (item.tipo === 'ENCUESTA') {
            const idEncuesta = item.detalle.idEncuesta;
            if (!encuestasMap.has(idEncuesta)) {
                encuestasMap.set(idEncuesta, {
                    uniqueId: `poll-${idEncuesta}`,
                    idReal: idEncuesta,
                    tipo: 'ENCUESTA',
                    fecha: item.fechaCreacion,
                    detalle: item.detalle,
                    opciones: [],
                    userVotedOptionId: null 
                });
            }
            const encuesta = encuestasMap.get(idEncuesta);
            
            if (item.detalle.votadaPorMi) {
                encuesta.userVotedOptionId = item.detalle.idOpcion;
            }

            if (!encuesta.opciones.some(o => o.idOpcion === item.detalle.idOpcion)) {
                encuesta.opciones.push({
                    idOpcion: item.detalle.idOpcion,
                    texto: item.detalle.opcion,
                    votos: item.detalle.totalVotos
                });
            }
        }
    });
    Array.from(encuestasMap.values()).forEach(e => e.opciones.sort((a, b) => a.idOpcion - b.idOpcion));
    const todos = [...contenidos, ...Array.from(encuestasMap.values())];
    return todos.sort((a, b) => b.fecha - a.fecha);
  };

  // IMPLEMENTACIÓN HTTP CATS PARA ERROR DE CARGA
  if (errorStatus) {
      return (
        <main className="envivo-main-content">
            <div className="error-container">
                <h2>¡Ups! Algo salió mal ({errorStatus})</h2>
                <p>No pudimos cargar las publicaciones.</p>
                <img 
                    src={`https://http.cat/${errorStatus}`} 
                    alt={`Error ${errorStatus}`} 
                    className="http-cat-img" 
                />
            </div>
        </main>
      );
  }

  return (
    <main className="envivo-main-content">
      <div className="encuestas-feed-container">
        {posts.map(post => (
          <PostCard key={post.uniqueId} post={post} tagsMap={tagsMap} />
        ))}
      </div>
    </main>
  );
}