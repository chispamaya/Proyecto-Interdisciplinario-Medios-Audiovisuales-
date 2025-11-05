// src/pages/admin/CrearPublicacion.jsx

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

// 1. Importamos los estilos del ABM (para el layout) y los nuevos
import '../../styles/components/abmForm.css'; 
import '../../styles/pages/crearPublicacion.css'; // <--- Este es el próximo archivo

// 2. Importamos íconos para la UI
import { Plus, X } from 'lucide-react';
import logoAdmin from '../../assets/logo.png'; // Avatar por defecto

export default function CrearPublicacion() {
    const navigate = useNavigate();

    // Estado para los 3 componentes del post
    const [text, setText] = useState('');
    const [imageUrl, setImageUrl] = useState('');
    const [showPoll, setShowPoll] = useState(false); // Checkbox para mostrar la encuesta

    // Estado solo para las opciones de la encuesta
    const [pollOptions, setPollOptions] = useState([
        { id: 1, text: '' },
        { id: 2, text: '' },
    ]);

    // --- Lógica para manejar opciones de encuesta ---
    const handlePollOptionChange = (id, value) => {
        setPollOptions(options =>
            options.map(opt => (opt.id === id ? { ...opt, text: value } : opt))
        );
    };

    const addPollOption = (e) => {
        e.preventDefault(); // Evita que el form se envíe
        setPollOptions(options => [
            ...options,
            { id: Date.now(), text: '' } // Añade nueva opción con id único
        ]);
    };

    const removePollOption = (e, id) => {
        e.preventDefault(); // Evita que el form se envíe
        if (pollOptions.length <= 2) {
             alert('Debe haber al menos 2 opciones.');
             return;
        }
        setPollOptions(options => options.filter(opt => opt.id !== id));
    };

    // --- Lógica de Envío (Simulación) ---
    const handleSubmit = (e) => {
        e.preventDefault();

        // 1. Validar que no esté vacío
        if (!text.trim() && !imageUrl.trim() && !showPoll) {
            alert('No se puede crear una publicación vacía. Añade texto, una imagen o una encuesta.');
            return;
        }

        // 2. Construir el objeto del post
        const newPost = {
            id: Date.now(),
            user: { name: 'Admin', handle: '@MyCanalOficial', avatar: logoAdmin },
            time: 'recién',
            likes: 0,
            dislikes: 0,
        };

        if (text.trim()) {
            newPost.text = text.trim();
        }
        if (imageUrl.trim()) {
            newPost.image = imageUrl.trim();
        }
        if (showPoll) {
            const validOptions = pollOptions.filter(opt => opt.text.trim() !== '');
            if (validOptions.length < 2) {
                alert('Las encuestas deben tener al menos 2 opciones válidas.');
                return;
            }
            // Si la encuesta es válida, se añade al post
            newPost.options = validOptions;
            // Si no hay texto, la pregunta de la encuesta es el texto principal
            if (!newPost.text && validOptions[0]) {
                 newPost.text = "¡Vota en nuestra nueva encuesta!"; // Texto genérico
            }
        }

        // 3. Mostrar simulación (sin backend)
        console.log("NUEVO POST CREADO:", newPost);
        alert('Simulación: ¡Publicación creada con éxito!\nRevisa la consola (F12) para ver el objeto del post.');
        
        // 4. Redirigir de vuelta
        navigate('/perfil'); // Volvemos al perfil (o donde quieras)
    };

    return (
        <div className="abm-form-page-content"> {/* Reutilizamos layout ABM */ }
            <h1 className="form-page-title"><span>Crear Nueva Publicación</span></h1>
            
            <form className="abm-form-card" onSubmit={handleSubmit}>
                
                {/* 1. CAMPO DE TEXTO (Opcional) */}
                <div className="form-group">
                    <label htmlFor="text">Texto de la Publicación</label>
                    <textarea 
                        id="text" 
                        value={text} 
                        onChange={(e) => setText(e.target.value)}
                        rows="4"
                        placeholder="¿Qué está pasando? (Opcional si subes foto o encuesta)"
                        className="poll-textarea" // Reutilizamos este estilo
                    ></textarea>
                </div>

                {/* 2. CAMPO DE FOTO (Opcional) */}
                <div className="form-group">
                    <label htmlFor="imageUrl">URL de la Imagen (Opcional)</label>
                    <input 
                        type="text" 
                        id="imageUrl" 
                        value={imageUrl}
                        onChange={(e) => setImageUrl(e.target.value)}
                        placeholder="https://.../imagen-del-programa.png" 
                    />
                </div>

                {/* 3. CAMPO DE ENCUESTA (Opcional) */}
                <div className="form-group form-group-checkbox">
                    <input 
                        type="checkbox" 
                        id="showPoll"
                        checked={showPoll}
                        onChange={(e) => setShowPoll(e.target.checked)}
                    />
                    <label htmlFor="showPoll">Añadir Encuesta</label>
                </div>

                {/* --- Opciones de Encuesta (Condicional) --- */}
                {showPoll && (
                    <div className="poll-creator-box">
                        <label className="poll-creator-label">Opciones de la Encuesta</label>
                        <div className="poll-options-creator">
                            {pollOptions.map((option, index) => (
                                <div key={option.id} className="poll-option-input-group">
                                    <input
                                        type="text"
                                        value={option.text}
                                        onChange={(e) => handlePollOptionChange(option.id, e.target.value)}
                                        placeholder={`Opción ${index + 1}`}
                                        required
                                    />
                                    <button 
                                        className="btn-remove-option" 
                                        onClick={(e) => removePollOption(e, option.id)}
                                        disabled={pollOptions.length <= 2}
                                        title="Eliminar opción"
                                    >
                                        <X size={16} />
                                    </button>
                                </div>
                            ))}
                        </div>
                        <button className="btn-add-option" onClick={addPollOption}>
                            <Plus size={16} style={{ marginRight: '8px' }} />
                            Añadir Opción
                        </button>
                    </div>
                )}


                {/* 4. Botones de Acción */}
                <div className="form-action-buttons">
                    <button type="button" className="btn-descartar-form" onClick={() => navigate('/perfil')}>
                        Descartar
                    </button>
                    <button type="submit" className="btn-guardar-form">
                        Publicar
                    </button>
                </div>
            </form>
        </div>
    );
}