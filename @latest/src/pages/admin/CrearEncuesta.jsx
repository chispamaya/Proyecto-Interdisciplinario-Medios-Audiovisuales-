import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Trash2, UploadCloud } from 'lucide-react';
import '../../styles/pages/crearPublicacion.css'; 

// Lo llamamos 'CrearPublicacion' porque ahora hace más que solo encuestas
export default function CrearPublicacion() {
  const navigate = useNavigate();
  
  // --- Estados del Formulario ---
  const [tipoPublicacion, setTipoPublicacion] = useState('mensaje'); // 'mensaje' o 'encuesta'
  
  // Campos Comunes
  const [texto, setTexto] = useState(''); // Opcional para ambos
  const [tags, setTags] = useState(''); // Opcional para ambos

  // Campos de Mensaje
  const [imagen, setImagen] = useState(null); // Obligatoria para mensaje
  const [fileName, setFileName] = useState('Ningún archivo seleccionado');

  // Campos de Encuesta
  const [tituloEncuesta, setTituloEncuesta] = useState(''); // Obligatorio para encuesta
  const [opciones, setOpciones] = useState(['', '']); 

  // --- Lógica de Encuesta ---
  const handleAddOption = () => {
    if (opciones.length < 4) {
      setOpciones([...opciones, '']);
    }
  };

  const handleRemoveOption = (index) => {
    const newOpciones = opciones.filter((_, i) => i !== index);
    setOpciones(newOpciones);
  };

  const handleOptionChange = (index, value) => {
    const newOpciones = [...opciones];
    newOpciones[index] = value;
    setOpciones(newOpciones);
  };

  // --- Lógica de Archivo ---
  const handleImageChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setImagen(e.target.files[0]);
      setFileName(e.target.files[0].name);
    } else {
      setImagen(null);
      setFileName('Ningún archivo seleccionado');
    }
  };

  // --- Lógica de Envío ---
  const handleSubmit = (e) => {
    e.preventDefault();
    
    // 1. Parsear Tags (común para ambos)
    const tagsArray = tags.split(',')
      .map(tag => tag.trim()) 
      .filter(tag => tag.length > 0 && tag.startsWith('#'));

    // 2. Enviar según el tipo
    if (tipoPublicacion === 'mensaje') {
      // Validar Mensaje (Imagen es obligatoria)
      if (!imagen) {
        alert('Por favor, sube una imagen para el mensaje.');
        return;
      }
      
      // Se usa FormData para enviar archivos
      const formData = new FormData();
      formData.append('tipo', 'mensaje');
      formData.append('texto', texto);
      formData.append('imagen', imagen);
      formData.append('tags', JSON.stringify(tagsArray));

      console.log('Enviando Mensaje (FormData):', Object.fromEntries(formData));
      // Lógica de fetch con FormData...

    } else if (tipoPublicacion === 'encuesta') {
      // Validar Encuesta (Título y 2 opciones son obligatorios)
      const opcionesValidas = opciones.filter(op => op.trim() !== '');
      if (opcionesValidas.length < 2) {
        alert('La encuesta debe tener al menos 2 opciones válidas.');
        return;
      }
      
      // Se puede enviar como JSON
      const publicacionJSON = {
        tipo: 'encuesta',
        titulo: tituloEncuesta,
        texto: texto, // El texto/pregunta es opcional
        opciones: opcionesValidas,
        tags: tagsArray
      };

      console.log('Enviando Encuesta (JSON):', publicacionJSON);
      // Lógica de fetch con JSON...
    }
    
    // Limpiar formulario y navegar
    // navigate('/admin/dashboard'); 
  };

  // --- Renderizado del Formulario ---
  return (
    <div className="crear-publicacion-container">
      <h1>Crear Nueva Publicación</h1>
      
      <div className="tipo-publicacion-selector">
        <button 
          className={`btn-tipo ${tipoPublicacion === 'mensaje' ? 'activo' : ''}`}
          onClick={() => setTipoPublicacion('mensaje')}
        >
          Mensaje (Imagen)
        </button>
        <button 
          className={`btn-tipo ${tipoPublicacion === 'encuesta' ? 'activo' : ''}`}
          onClick={() => setTipoPublicacion('encuesta')}
        >
          Encuesta
        </button>
      </div>

      <form onSubmit={handleSubmit} className="crear-publicacion-form">
        
        {/* --- FORMULARIO PARA MENSAJE --- */}
        {tipoPublicacion === 'mensaje' && (
          <>
            <div className="form-group">
              <label htmlFor="imagen" className="label-required">
                Imagen (Obligatoria)
              </label>
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
          </>
        )}

        {/* --- FORMULARIO PARA ENCUESTA --- */}
        {tipoPublicacion === 'encuesta' && (
          <>
            <div className="form-group">
              <label htmlFor="titulo-encuesta" className="label-required">
                Título de la Encuesta (Obligatorio)
              </label>
              <input
                type="text"
                id="titulo-encuesta"
                value={tituloEncuesta}
                onChange={(e) => setTituloEncuesta(e.target.value)}
                placeholder="¿Sobre qué quieres preguntar?"
                required
              />
            </div>
            
            <div className="form-group">
              <label htmlFor="texto-encuesta">Pregunta / Descripción (Opcional)</label>
              <textarea 
                id="texto-encuesta"
                value={texto}
                onChange={(e) => setTexto(e.target.value)}
                placeholder="Añade un contexto o pregunta si lo deseas..."
                rows={3}
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
                    required={index < 2} // Las primeras 2 son obligatorias
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

        {/* --- CAMPO COMÚN: TAGS --- */}
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

        <div className="form-actions">
          <button type="submit" className="btn-submit-publicacion">
            Publicar
          </button>
        </div>
      </form>
    </div>
  );
}