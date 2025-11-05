// src/App.jsx

import './styles/App.css';
import { Routes, Route } from 'react-router-dom';

// --- Layouts ---
import AppLayout from './components/layout/AppLayout.jsx';
import EspectadorLayout from './components/layout/EspectadorLayout.jsx'; 

// --- Pages ---
import Login from './pages/Login.jsx';
import Perfil from './pages/Perfil.jsx'; // 
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

// --- Pages Espectador ---
import EnVivo from './pages/espectador/EnVivo.jsx';
import EncuestasEspectador from './pages/espectador/EncuestasEspectador.jsx';

// --- 徴 1. IMPORTAR NUEVAS COSAS 徴 ---
import { ParrillaProvider } from './context/ParrillaContext';
// import { FeedProvider } from './context/FeedContext.jsx'; // <--- No lo usamos por ahora
// import CrearPublicacion from './pages/admin/CrearPublicacion.jsx'; // <--- ELIMINADO
import CrearEncuesta from './pages/admin/CrearEncuesta.jsx'; // 💥 AÑADIDO 💥

function App() {
  return (
    <>
      {/* 徴 2. SACAMOS EL FEEDPROVIDER POR AHORA 徴 */}
      {/* <FeedProvider> */}
        <ParrillaProvider>
          <Routes>
            {/* --- Ruta de Login (Sin layout) --- */}
            <Route path="/" element={<Login />} />

            {/* --- Rutas de Espectador --- */}
            <Route element={<EspectadorLayout />}>
              <Route path="/en-vivo" element={<EnVivo />} />
              <Route path="/encuestas-espectador" element={<EncuestasEspectador />} />
            </Route>

            {/* --- Rutas de Admin --- */}
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

              {/* 徴 3. RUTA AÑADIDA 徴 */}
              {/* <Route path="/admin/crear-publicacion" element={<CrearPublicacion />} /> */}
              <Route path="/admin/crear-publicacion" element={<CrearEncuesta />} /> {/* 💥 AÑADIDO 💥 */}
            </Route>

          </Routes>
        </ParrillaProvider>
      {/* </FeedProvider> */}
    </>
  );
}

export default App;