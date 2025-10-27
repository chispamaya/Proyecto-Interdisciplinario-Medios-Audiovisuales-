// src/App.jsx

import './styles/App.css';
import { Routes, Route } from 'react-router-dom';

import Login from './pages/Login.jsx';

import { Header } from './components/layout/header.jsx';
import AppLayout from './components/layout/AppLayout.jsx';
// --- Imports de Admin ---
import Perfil from './pages/perfil.jsx';
// --- Imports de Editor-Productor ---
import ReportesAudiencia from './pages/admin/ReportesAudiencia.jsx';
import SubidaMultimedia from './pages/editor-productor/SubidaMultimedia.jsx';
import EstadoAprobacion from './pages/editor-productor/EstadoAprobacion.jsx';
import ParrillaSemanal from './pages/editor-productor/ParrillaSemanal.jsx';
import GestionMultimedia from './pages/editor-productor/GestionMultimedia.jsx';
// --- Imports de Programador ---
import ControlEmision from './pages/programador/ControlDeEmision.jsx';
import ArmadoParrilla from './pages/programador/ArmadoParrillaHoraria.jsx'; // (Asegúrate que el nombre y ruta sean correctos)

import { ParrillaProvider } from './context/ParrillaContext';

function App() {
  return (
    // 2. Envuelve toda la aplicación con el Provider
    <ParrillaProvider>
      <Header />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route element={<AppLayout />}>
          <Route path="/perfil" element={<Perfil />} />
          <Route path="/subida" element={<SubidaMultimedia />} />
          <Route path="/estado/:id" element={<EstadoAprobacion />} />
          <Route path="/parrilla" element={<ParrillaSemanal />} />
          <Route path="/gestion" element={<GestionMultimedia />} />
          <Route path="/reportes" element={<ReportesAudiencia />} />
          <Route path="/controlEmision" element={<ControlEmision />} />
          <Route path="/armadoParrilla" element={<ArmadoParrilla />} />
          {/* 3. ¡ELIMINA el bloque que causaba el error! */}

        </Route>
      </Routes>
    </ParrillaProvider>
  );
}

export default App;