// src/main.jsx (Separado y corregido)

import React, { StrictMode } from 'react'; // 1. Importar React si es necesario, y StrictMode.
import ReactDOM, { createRoot } from 'react-dom/client'; // 2. Importar ReactDOM y createRoot.
import { BrowserRouter } from 'react-router-dom'; // 3. Importar BrowserRouter
import './styles/index.css';
import App from './App.jsx';

// Asegúrate de que usas createRoot
const rootElement = document.getElementById('root');
if (rootElement) {
    ReactDOM.createRoot(rootElement).render(
        <StrictMode>
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </StrictMode>
    );
}