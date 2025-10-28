// src/App.jsx - CORRECCIÓN FINAL DE IMPORTS

import './styles/App.css';
import { Routes, Route } from 'react-router-dom';



import { Header } from './components/layout/header.jsx';

import AppLayout from './components/layout/AppLayout.jsx';


import ReportesAudiencia from './pages/admin/ReportesAudiencia.jsx';

// --- Imports de Editor-Productor ---
import SubidaMultimedia from './pages/editor-productor/SubidaMultimedia.jsx';
import EstadoAprobacion from './pages/editor-productor/EstadoAprobacion.jsx';
import ParrillaSemanal from './pages/editor-productor/ParrillaSemanal.jsx';
import GestionMultimedia from './pages/editor-productor/GestionMultimedia.jsx';

// --- Imports de Programador ---
// (Asumiendo que están en la carpeta 'programador' como en tus prompts anteriores)
import ControlEmision from './pages/programador/ControlDeEmision.jsx';
import ArmadoParrilla from './pages/programador/ArmadoParrillaHoraria.jsx';
import errores from './pages/programador/errores.jsx';

// --- Imports de Pages (raíz) ---
import Login from './pages/Login.jsx';
import Perfil from './pages/Perfil.jsx';
import { ParrillaProvider } from './context/ParrillaContext';
// 💥 IMPORTS CORREGIDOS PARA LA CARPETA /admin/abm/ 💥
import ABMMenu from './pages/admin/abm/ABMMenu.jsx';
import ABMProgramas from './pages/admin/abm/ABMProgramas.jsx';
import ABMSegmentos from './pages/admin/abm/ABMSegmentos.jsx';
import ABMPlataformas from './pages/admin/abm/ABMPlataformas.jsx';
import ABMEmpleados from './pages/admin/abm/ABMEmpleados.jsx';
import Errores from './pages/programador/errores.jsx'

function App() {
  return (
    <>
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

            <Route path="/abm" element={<ABMMenu />} />
            <Route path="/abm/programas" element={<ABMProgramas />} />
            <Route path="/abm/segmentos" element={<ABMSegmentos />} />
            <Route path="/abm/plataformas" element={<ABMPlataformas />} />
            <Route path="/abm/empleados" element={<ABMEmpleados />} />
            <Route path="/armadoParrilla" element={<ArmadoParrilla />} />
            <Route path="/controlEmision" element={<ControlEmision />} />
            <Route path="/errores" element={<Errores />} />

          </Route>
        </Routes>
      </ParrillaProvider>

    </>
  );
}

export default App;