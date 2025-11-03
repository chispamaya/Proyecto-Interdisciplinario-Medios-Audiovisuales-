// src/App.jsx - VERSIÓN CORREGIDA

import './styles/App.css';
import { Routes, Route } from 'react-router-dom';

// ❌ NO importamos el Header global aquí

// --- Layouts ---
import AppLayout from './components/layout/AppLayout.jsx';
import EspectadorLayout from './components/layout/EspectadorLayout.jsx'; // 1. Importa el nuevo layout

// --- Pages ---
import Login from './pages/Login.jsx';
import Perfil from './pages/Perfil.jsx';
import ReportesAudiencia from './pages/admin/ReportesAudiencia.jsx';
import SubidaMultimedia from './pages/editor-productor/SubidaMultimedia.jsx';
import EstadoAprobacion from './pages/editor-productor/EstadoAprobacion.jsx';
import ParrillaSemanal from './pages/editor-productor/ParrillaSemanal.jsx';
import GestionMultimedia from './pages/editor-productor/GestionMultimedia.jsx';
import ControlEmision from './pages/programador/ControlDeEmision.jsx';
import ArmadoParrilla from './pages/programador/ArmadoParrillaHoraria.jsx';
import ABMMenu from './pages/admin/abm/ABMMenu.jsx';
import ABMProgramas from './pages/admin/abm/ABMProgramas.jsx';
import ABMSegmentos from './pages/admin/abm/ABMSegmentos.jsx';
import ABMPlataformas from './pages/admin/abm/ABMPlataformas.jsx';
import ABMEmpleados from './pages/admin/abm/ABMEmpleados.jsx';
import Errores from './pages/programador/errores.jsx';

// --- Pages Espectador (Nuevas) ---
import EnVivo from './pages/espectador/EnVivo.jsx';
import EncuestasEspectador from './pages/espectador/EncuestasEspectador.jsx';

// --- Context ---
import { ParrillaProvider } from './context/ParrillaContext';


function App() {
  return (
    <>
      <ParrillaProvider>
        {/* ❌ El <Header /> global se elimina de aquí */}

        <Routes>
          {/* --- Ruta de Login (Sin layout) --- */}
          <Route path="/" element={<Login />} />

          {/* --- 2. RUTAS DE ESPECTADOR --- */}
          {/* Usan el EspectadorLayout (header negro) */}
          <Route element={<EspectadorLayout />}>
            <Route path="/en-vivo" element={<EnVivo />} />
            <Route path="/encuestas-espectador" element={<EncuestasEspectador />} />
          </Route>

          {/* --- 3. RUTAS DE ADMIN --- */}
          {/* Usan el AppLayout (header azul + sidebar) */}
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