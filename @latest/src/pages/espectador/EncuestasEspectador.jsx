import React, { useState } from 'react';
// 1. Importa el MISMO CSS de contenido
import './EnVivo.css'; 

// ❌ (Ya no tiene el 'EnVivoHeader', el layout lo maneja)

// --- Componente Principal de la Página Encuestas ---
export default function EncuestasEspectador() {
  const [selectedOption, setSelectedOption] = useState(null);

  const pollOptions = [
    { id: 'op1', text: 'Me encanta' },
    { id: 'op2', text: 'Me gusta' },
    { id: 'op3', text: 'No me gusta' },
    { id: 'op4', text: 'Lo odio' },
  ];

  const handleSubmit = (e) => {
    e.preventDefault();
    if (selectedOption) {
      alert(`Tu voto: "${selectedOption}" ha sido enviado. ¡Gracias!`);
    } else {
      alert("Por favor, selecciona una opción.");
    }
  };

  return (
    // Ya no necesita <div className="envivo-page-container">
    <main className="encuestas-main-content">
      <div className="poll-card-container">
        <img 
          src="https://i.imgur.com/gA3gCqC.png" // Placeholder de la imagen de Bendita
          alt="Logo del programa Bendita" 
          className="poll-program-image"
        />
        <form className="poll-form" onSubmit={handleSubmit}>
          <h2 className="poll-question">¿CUÁL ES TU OPINIÓN SOBRE BENDITA?</h2>
          <div className="poll-options-list">
            {pollOptions.map((option) => (
              <label key={option.id} className="poll-option">
                <span className="poll-option-text">{option.text}</span>
                <input 
                  type="radio" 
                  name="poll-bendita" 
                  value={option.text}
                  checked={selectedOption === option.text}
                  onChange={() => setSelectedOption(option.text)}
                />
                <span className="poll-custom-radio"></span>
              </label>
            ))}
          </div>
          <button type="submit" className="poll-submit-btn">
            Votar
          </button>
        </form>
      </div>
    </main>
  );
}