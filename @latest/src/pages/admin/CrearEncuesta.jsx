import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Trash2, UploadCloud } from 'lucide-react';
import axios from 'axios';
import '../../styles/pages/crearPublicacion.css';

export default function CrearPublicacion() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  
  // Estados Formulario
  const [tipoPublicacion, setTipoPublicacion] = useState('mensaje');
  const [texto, setTexto] = useState('');
  const [tags, setTags] = useState(''); 
  const [imagen, setImagen] = useState(null);
  const [fileName, setFileName] = useState('Ningún archivo seleccionado');

  // Encuesta
  const [tituloEncuesta, setTituloEncuesta] = useState('');
  const [opciones, setOpciones] = useState(['', '']);

  // Helper ID Usuario
  const obtenerIdUsuario = () => {
    const userStr = localStorage.getItem('usuario');
    if (userStr) {
      try { return JSON.parse(userStr).id; } catch (e) { return 1; }
    }
    return 1; 
  };

  const handleImageChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setImagen(e.target.files[0]);
      setFileName(e.target.files[0].name);
    } else {
      setImagen(null);
      setFileName('Ningún archivo seleccionado');
    }
  };

  // --- HANDLERS ENCUESTA (SIN LÍMITE) ---
  const handleAddOption = () => {
    // 💥 CAMBIO: Eliminado el límite de opciones
    setOpciones([...opciones, '']);
  };
  
  const handleRemoveOption = (index) => setOpciones(opciones.filter((_, i) => i !== index));
  
  const handleOptionChange = (index, value) => {
    const newOpciones = [...opciones];
    newOpciones[index] = value;
    setOpciones(newOpciones);
  };

  // --- SUBMIT GENERAL ---
  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    const idActual = obtenerIdUsuario();

    try {
      if (tipoPublicacion === 'mensaje') {
        if (!imagen) {
          alert("Debes seleccionar una imagen.");
          setIsLoading(false);
          return;
        }

        // 💥 VALIDACIÓN DE TAGS OBLIGATORIOS 💥
        if (!tags || tags.trim() === "") {
            alert("Debes ingresar al menos una etiqueta (tag) para el mensaje.");
            setIsLoading(false);
            return;
        }

        const formData = new FormData();
        formData.append('file', imagen);

        console.log("📤 Subiendo imagen...");
        // URL absoluta para evitar problemas de ruta relativa
        const uploadRes = await axios.post('http://localhost:8080/api/contenido/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        
        const rutaImagenFinal = uploadRes.data;

        const tagsArray = tags.split(',').map(t => t.trim()).filter(t => t !== "");

        const payloadContenido = {
          contenido: {
            texto: texto,
            rutaArchivo: rutaImagenFinal,
            formato: "image/png",
            idUsuario: idActual
          },
          tagsTexto: tagsArray,
          idUsuarioAuditoria: idActual
        };

        await axios.post('http://localhost:8080/api/contenido/crear', payloadContenido);
        
        alert("¡Mensaje publicado con éxito!");
        setTexto('');
        setTags('');
        setImagen(null);
        setFileName('Ningún archivo seleccionado');
      } 
      
      else if (tipoPublicacion === 'encuesta') {
        if (!tituloEncuesta.trim()) { alert('Falta la pregunta.'); setIsLoading(false); return; }
        const opcionesValidas = opciones.filter(op => op.trim() !== '');
        if (opcionesValidas.length < 2) { alert('Mínimo 2 opciones.'); setIsLoading(false); return; }

        const payloadEncuesta = {
          preguntar: tituloEncuesta,
          idUsuario: idActual,
          opciones: opcionesValidas.map(op => ({ opcion: op }))
        };

        await axios.post(`http://localhost:8080/api/encuestas?idUsuarioAuditoria=${idActual}`, payloadEncuesta);
        
        alert("¡Encuesta creada exitosamente!");
        setTituloEncuesta('');
        setOpciones(['', '']);
      }

    } catch (error) {
      console.error("🛑 Error:", error);
      alert("Ocurrió un error: " + (error.response?.data || error.message));
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="crear-publicacion-container">
      <h1>Crear Nueva Publicación</h1>

      <div className="tipo-publicacion-selector">
        <button
          type="button"
          className={`btn-tipo ${tipoPublicacion === 'mensaje' ? 'activo' : ''}`}
          onClick={() => setTipoPublicacion('mensaje')}
        >
          Mensaje (Imagen)
        </button>
        <button
          type="button"
          className={`btn-tipo ${tipoPublicacion === 'encuesta' ? 'activo' : ''}`}
          onClick={() => setTipoPublicacion('encuesta')}
        >
          Encuesta
        </button>
      </div>

      <form onSubmit={handleSubmit} className="crear-publicacion-form">

        {tipoPublicacion === 'mensaje' && (
          <>
            <div className="form-group">
              <label htmlFor="imagen" className="label-required">Imagen (Obligatoria)</label>
              <label htmlFor="imagen" className="file-upload-label">
                <UploadCloud size={18} />
                <span>{fileName}</span>
              </label>
              <input
                type="file"
                id="imagen"
                className="file-upload-input"
                onChange={handleImageChange}
                accept="image/png, image/jpeg, image/gif"
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="texto-mensaje">Texto (Opcional)</label>
              <textarea
                id="texto-mensaje"
                value={texto}
                onChange={(e) => setTexto(e.target.value)}
                placeholder="Añade un texto..."
                rows={4}
              />
            </div>
            <div className="form-group">
              {/* 💥 CAMBIO VISUAL: Label indica que es obligatorio */}
              <label htmlFor="tags" className="label-required">Tags (Separados por coma)</label>
              <input
                type="text"
                id="tags"
                value={tags}
                onChange={(e) => setTags(e.target.value)}
                placeholder="Ej: Deportes, En Vivo, Noticias"
                required 
              />
            </div>
          </>
        )}

        {tipoPublicacion === 'encuesta' && (
          <>
            <div className="form-group">
              <label htmlFor="titulo-encuesta" className="label-required">Pregunta</label>
              <input
                type="text"
                id="titulo-encuesta"
                value={tituloEncuesta}
                onChange={(e) => setTituloEncuesta(e.target.value)}
                placeholder="¿Qué quieres preguntar?"
                required
              />
            </div>

            <div className="opciones-encuesta-container">
              <label className="label-required">Opciones (Mínimo 2)</label>
              {opciones.map((opcion, index) => (
                <div key={index} className="opcion-input-group">
                  <input
                    type="text"
                    value={opcion}
                    onChange={(e) => handleOptionChange(index, e.target.value)}
                    placeholder={`Opción ${index + 1}`}
                    required={index < 2}
                  />
                  {opciones.length > 2 && (
                    <button
                      type="button"
                      className="btn-remove-opcion"
                      onClick={() => handleRemoveOption(index)}
                    >
                      <Trash2 size={18} />
                    </button>
                  )}
                </div>
              ))}
              
              {/* 💥 CAMBIO: Botón siempre visible (sin límite) */}
              <button
                type="button"
                className="btn-add-opcion"
                onClick={handleAddOption}
              >
                <Plus size={18} /> Añadir opción
              </button>
            </div>
          </>
        )}

        <div className="form-actions">
          <button type="submit" className="btn-submit-publicacion" disabled={isLoading}>
            {isLoading ? 'Publicando...' : 'Publicar'}
          </button>
        </div>
      </form>
    </div>
  );
}