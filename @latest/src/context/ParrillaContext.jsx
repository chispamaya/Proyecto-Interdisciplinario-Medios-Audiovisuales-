// src/context/ParrillaContext.jsx
import React, { createContext, useState, useContext } from 'react';

// 1. Definimos la ESTRUCTURA DE DATOS que queremos
const estadoInicial = {
  "LUNES": [],
  "MARTES": [],
  "MIERCOLES": [],
  "JUEVES": [],
  "VIERNES": [],
  "SABADO": [],
  "DOMINGO": []
};

// 2. Creamos el contexto
const ParrillaContext = createContext();

// 3. Creamos el "Proveedor" que manejará el estado
export function ParrillaProvider({ children }) {
  const [parrilla, setParrilla] = useState(estadoInicial);

  // setParrilla será usado por la página de Drag&Drop
  // parrilla será usado por la página que muestra la grilla

  return (
    <ParrillaContext.Provider value={{ parrilla, setParrilla }}>
      {children}
    </ParrillaContext.Provider>
  );
}

// 4. Creamos un "Hook" para que sea fácil usarlo
export function useParrilla() {
  return useContext(ParrillaContext);
}