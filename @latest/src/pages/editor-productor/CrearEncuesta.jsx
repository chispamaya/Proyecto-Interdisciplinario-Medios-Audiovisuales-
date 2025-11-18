import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Trash2, UploadCloud } from 'lucide-react';
import axios from 'axios';
import '../../styles/pages/crearPublicacion.css';

export default function CrearPublicacion() {
  const navigate = useNavigate();

  // --- Estados de Conexión ---
  const [isLoading, setIsLoading] = useState(false);
  const [httpError, setHttpError] = useState(null);

  // --- Estados del Formulario ---
  const [tipoPublicacion, setTipoPublicacion] = useState('mensaje');

  // Mensaje
  const [texto, setTexto] = useState('');
  const [tags, setTags] = useState('');
  const [imagen, setImagen] = useState(null);
  const [fileName, setFileName] = useState('Ningún archivo seleccionado');

  // Encuesta
  const [tituloEncuesta, setTituloEncuesta] = useState('');
  const [opciones, setOpciones] = useState(['', '']);

  // --- Helper para obtener ID Usuario ---
  const obtenerIdUsuario = () => {
    // CORRECCIÓN: Usar 'usuarioId' que es como lo guardaste en el Login
    const guardado = localStorage.getItem('usuarioId');

    // Si no hay nada, devolvemos null (o 1 si quieres forzar un admin por defecto para pruebas)
    return guardado ? parseInt(guardado) : 1;
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

    if (tipoPublicacion === 'mensaje') {
      // Lógica de mensaje (No implementada aún)
      alert("Funcionalidad de subir Mensaje no conectada aún.");

    } else if (tipoPublicacion === 'encuesta') {

      // 1. Validación Frontend
      if (tituloEncuesta.trim() === '') {
        alert('Por favor, ingresa una pregunta para la encuesta.');
        return;
      }
      const opcionesValidas = opciones.filter(op => op.trim() !== '');
      if (opcionesValidas.length < 2) {
        alert('La encuesta debe tener al menos 2 opciones válidas.');
        return;
      }

      setIsLoading(true);

      // 2. Obtener ID real
      const idActual = obtenerIdUsuario();

      // 3. Preparar JSON - CORRECCIÓN CRÍTICA DE ESTRUCTURA Y NOMBRE DE CAMPO
      const publicacionJSON = {
        // ESTRUCTURA PLANA: Eliminar el objeto "encuesta" y colocar todo en el nivel superior
        preguntar: tituloEncuesta, // <--- CORRECCIÓN 1: VUELVE a 'preguntar' (coincide con Encuesta.java)
        idUsuario: idActual,
        opciones: opcionesValidas.map(op => ({ opcion: op }))
      };

      const API_URL = `http://localhost:8080/api/encuestas?idUsuarioAuditoria=${idActual}`;

     


      try {
        // 4. Llamada al Backend usando Axios
        const response = await axios.post(API_URL, publicacionJSON, {
          headers: {
            "Content-Type": "application/json"
          }
        });

        alert("¡Encuesta creada exitosamente!");

        // Resetear formulario
        setTituloEncuesta('');
        setOpciones(['', '']);

      } catch (error) {

        // 🟢 DEBUGGING: Mostrar el error exacto del backend
        console.error("🛑 Error al crear encuesta (Objeto completo de Axios):", error);
        if (error.response) {
          console.error("🛑 Cuerpo de la respuesta de error (response.data):", error.response.data);
        }

        let errorStatus = 503; // Default
        let errorMessage = "No se pudo conectar con el servidor";

        if (error.response) {
          errorStatus = error.response.status;
          errorMessage = error.response.data?.message || error.response.data?.error || (typeof error.response.data === 'string' ? error.response.data : error.response.statusText);
        } else if (error.request) {
          errorMessage = "El servidor no respondió. Asegúrate de que esté corriendo en http://localhost:8080.";
        }

        setHttpError({
          status: errorStatus,
          message: errorMessage
        });
      } finally {
        setIsLoading(false);
      }
    }
  };

  // --- Vista Error (HTTP Cats) ---
  if (httpError) {
    return (
      <div className="http-cat-container">
        <h2 className="http-cat-title">¡Miau! Error {httpError.status}</h2>
        <p className="http-cat-message">{httpError.message}</p>
        <img
          src={`https://http.cat/${httpError.status}`}
          alt={`Error ${httpError.status}`}
          className="http-cat-image"
        />
        <button onClick={handleRetry} className="http-cat-button">
          Volver al formulario
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
                placeholder="Añade un texto si lo deseas..."
                rows={4}
              />
            </div>
            <div className="form-group">
              <label htmlFor="tags">Tags (Opcional)</label>
              <input
                type="text"
                id="tags"
                value={tags}
                onChange={(e) => setTags(e.target.value)}
                placeholder="Ej: #debate, #noticias, #vivo"
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
                    maxLength={50}
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