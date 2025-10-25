// src/App.jsx

import './styles/App.css';
import { Routes, Route } from 'react-router-dom';
import Login from './pages/Login.jsx';
import { Header } from './components/layout/header.jsx';
import AppLayout from './components/layout/AppLayout.jsx';
import Perfil from './pages/perfil.jsx';
import SubidaMultimedia from './pages/SubidaMultimedia.jsx';
// 💥 AÑADIR ESTA LÍNEA: Importar el componente 💥
import EstadoAprobacion from './pages/EstadoAprobacion.jsx';
import ParrillaSemanal from './pages/ParrillaSemanal.jsx'; 
import GestionMultimedia from './pages/GestionMultimedia.jsx'; // Importa la nueva página

// ... (El resto del código como lo tienes) ...

function App() {
  return (
    <>
      <Header/>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route element={<AppLayout />}>
          <Route path="/perfil" element={<Perfil />} />
          <Route path="/subida" element={<SubidaMultimedia />} />
          {/* Ahora esta ruta usará el componente importado */}
          <Route path="/estado/:id" element={<EstadoAprobacion />} />
          <Route path="/parrilla" element={<ParrillaSemanal />} />
          <Route path="/gestion" element={<GestionMultimedia />} /> 

        </Route>
      </Routes>
    </>
  );
}

export default App;
