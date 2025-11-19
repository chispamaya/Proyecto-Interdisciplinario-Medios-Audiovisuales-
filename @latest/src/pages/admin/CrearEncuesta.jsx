import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Trash2, UploadCloud } from 'lucide-react';
import axios from 'axios';
// Asegúrate de que la ruta al CSS sea correcta según tu estructura
import '../../styles/pages/crearPublicacion.css';

export default function CrearPublicacion() {
  const navigate = useNavigate();

  // --- Estados de Conexión ---
  const [isLoading, setIsLoading] = useState(false);
  const [httpError, setHttpError] = useState(null);

  // --- Estados del Formulario ---
  const [tipoPublicacion, setTipoPublicacion] = useState('mensaje');

  // Mensaje (Contenido)
  const [texto, setTexto] = useState('');
  const [tags, setTags] = useState('');
  const [imagen, setImagen] = useState(null);
  const [fileName, setFileName] = useState('Ningún archivo seleccionado');

  // Encuesta
  const [tituloEncuesta, setTituloEncuesta] = useState('');
  const [opciones, setOpciones] = useState(['', '']);

  // --- Helper para obtener ID Usuario ---
  const obtenerIdUsuario = () => {
    const guardado = localStorage.getItem('usuarioId');
    return guardado ? parseInt(guardado) : null;
  };

  // --- Funciones Auxiliares ---
  const handleAddOption = () => {
    if (opciones.length < 4) setOpciones([...opciones, '']);
  };

  const handleRemoveOption = (index) => {
    setOpciones(opciones.filter((_, i) => i !== index));
  };

  const handleOptionChange = (index, value) => {
    const newOpciones = [...opciones];
    newOpciones[index] = value;
    setOpciones(newOpciones);
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

  const handleRetry = () => {
    setHttpError(null);
    setIsLoading(false);
  };

  // --- Envío del Formulario ---
  const handleSubmit = async (e) => {
    e.preventDefault();
    setHttpError(null);
    setIsLoading(true);

    const idActual = obtenerIdUsuario();
    console.log("👤 [DEBUG] ID Usuario obtenido:", idActual);

    if (!idActual) {
        alert("Error: No has iniciado sesión.");
        setIsLoading(false);
        return;
    }

    try {
        if (tipoPublicacion === 'mensaje') {
            // --- LÓGICA MENSAJE (CONTENIDO) ---
            
            console.log("🚀 [DEBUG] Iniciando subida de MENSAJE...");

            if (!fileName || fileName === 'Ningún archivo seleccionado') {
                alert("Debes seleccionar una imagen.");
                setIsLoading(false);
                return;
            }

            // Convertir tags
            const listaIdsTags = tags.split(',')
                .map(t => parseInt(t.trim()))
                .filter(t => !isNaN(t));

            console.log("🏷️ [DEBUG] Tags procesados:", listaIdsTags);

            // Armar el objeto a enviar
            const contenidoPayload = {
                contenido: {
                    // 🔴 ANTES: titulo: texto || "Mensaje...",
                    // 🟢 AHORA (Nombre correcto en BD):
                    texto: texto || "Mensaje con Imagen", 
                    
                    // 🔴 ANTES: ruta: "/uploads/" + fileName,
                    // 🟢 AHORA (Nombre correcto en BD):
                    rutaArchivo: "/uploads/" + fileName, 
                    
                    formato: "IMAGEN",
                    idUsuario: idActual
                },
                listaIdsTags: listaIdsTags,
                idUsuarioAuditoria: idActual
            };

            // IMPRIMIR EL JSON EXACTO QUE SE ENVÍA
            console.log("📦 [DEBUG] JSON a enviar al Backend:", JSON.stringify(contenidoPayload, null, 2));

            // Enviar POST
            const response = await axios.post('http://localhost:8080/api/contenido/crear', contenidoPayload);
            
            // IMPRIMIR LA RESPUESTA DEL SERVIDOR
            console.log("✅ [DEBUG] ¡Éxito! Respuesta completa del servidor:", response);
            console.log("✅ [DEBUG] Datos recibidos (response.data):", response.data);
            console.log("✅ [DEBUG] Código de estado (response.status):", response.status);
            
            alert("¡Mensaje publicado con éxito!");
            
            // Limpiar
            setTexto("");
            setTags("");
            setImagen(null);
            setFileName('Ningún archivo seleccionado');

        } else if (tipoPublicacion === 'encuesta') {
            // --- LÓGICA ENCUESTA ---
            console.log("📊 [DEBUG] Iniciando creación de ENCUESTA...");

            if (tituloEncuesta.trim() === '') {
                alert('Por favor, ingresa una pregunta.');
                setIsLoading(false);
                return;
            }
            const opcionesValidas = opciones.filter(op => op.trim() !== '');
            if (opcionesValidas.length < 2) {
                alert('Mínimo 2 opciones.');
                setIsLoading(false);
                return;
            }

            const encuestaPayload = {
                preguntar: tituloEncuesta,
                idUsuario: idActual,
                opciones: opcionesValidas.map(op => ({ opcion: op }))
            };

            console.log("📦 [DEBUG] Payload Encuesta:", JSON.stringify(encuestaPayload, null, 2));

            const response = await axios.post(`http://localhost:8080/api/encuestas?idUsuarioAuditoria=${idActual}`, encuestaPayload);

            console.log("✅ [DEBUG] Respuesta Encuesta:", response.data);

            alert("¡Encuesta creada exitosamente!");
            setTituloEncuesta('');
            setOpciones(['', '']);
        }

    } catch (error) {
        console.error("❌ [DEBUG] OCURRIÓ UN ERROR:", error);
        
        let status = 500;
        let msg = "Error desconocido";

        if (error.response) {
            // El servidor respondió con un código de error (4xx, 5xx)
            console.error("❌ [DEBUG] Datos de error del servidor (response.data):", error.response.data);
            console.error("❌ [DEBUG] Status code:", error.response.status);
            console.error("❌ [DEBUG] Headers:", error.response.headers);

            status = error.response.status;
            msg = typeof error.response.data === 'string' 
                  ? error.response.data 
                  : (error.response.data?.message || "Error en el servidor");
        } else if (error.request) {
            // La petición se hizo pero no hubo respuesta
            console.error("❌ [DEBUG] No hubo respuesta del servidor (posiblemente apagado o CORS):", error.request);
            status = 503;
            msg = "No se pudo conectar con el servidor (localhost:8080).";
        } else {
             console.error("❌ [DEBUG] Error al configurar la petición:", error.message);
        }

        setHttpError({ status, message: msg });
    } finally {
        setIsLoading(false);
    }
  };
  // --- Renderizado de Error ---
  if (httpError) {
    return (
      <div className="http-cat-container">
        <h2 className="http-cat-title">¡Ups! Error {httpError.status}</h2>
        <p className="http-cat-message">{httpError.message}</p>
        <img
          src={`https://http.cat/${httpError.status}`}
          alt={`Error ${httpError.status}`}
          className="http-cat-image"
        />
        <button onClick={handleRetry} className="http-cat-button">
          Intentar de nuevo
        </button>
      </div>
    );
  }

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

        {/* --- SECCIÓN MENSAJE --- */}
        {tipoPublicacion === 'mensaje' && (
          <>
            <div className="form-group">
              <label htmlFor="imagen" className="label-required">Imagen (Simulación)</label>
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
                // required // Lo validamos manualmente en el submit
              />
            </div>
            <div className="form-group">
              <label htmlFor="texto-mensaje">Texto (Se usará como Título)</label>
              <textarea
                id="texto-mensaje"
                value={texto}
                onChange={(e) => setTexto(e.target.value)}
                placeholder="Escribe el texto de tu publicación..."
                rows={4}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="tags">IDs de Tags (separados por coma)</label>
              <input
                type="text"
                id="tags"
                value={tags}
                onChange={(e) => setTags(e.target.value)}
                placeholder="Ej: 1, 3, 5 (Debe coincidir con IDs existentes)"
              />
            </div>
          </>
        )}

        {/* --- SECCIÓN ENCUESTA --- */}
        {tipoPublicacion === 'encuesta' && (
          <>
            <div className="form-group">
              <label htmlFor="titulo-encuesta" className="label-required">
                Pregunta de la Encuesta
              </label>
              <input
                type="text"
                id="titulo-encuesta"
                value={tituloEncuesta}
                onChange={(e) => setTituloEncuesta(e.target.value)}
                placeholder="¿Qué quieres preguntar a la audiencia?"
                required={tipoPublicacion === 'encuesta'}
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
                    maxLength={50}
                    required={tipoPublicacion === 'encuesta' && index < 2}
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
              {opciones.length < 4 && (
                <button
                  type="button"
                  className="btn-add-opcion"
                  onClick={handleAddOption}
                >
                  <Plus size={18} /> Añadir opción
                </button>
              )}
            </div>
          </>
        )}

        <div className="form-actions">
          <button
            type="submit"
            className="btn-submit-publicacion"
            disabled={isLoading}
          >
            {isLoading ? 'Publicando...' : 'Publicar'}
          </button>
        </div>
      </form>
    </div>
  );
}