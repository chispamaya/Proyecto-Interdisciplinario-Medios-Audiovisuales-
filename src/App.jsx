// src/App.jsx - CORRECCIÓN FINAL DE IMPORTS

import './styles/App.css';
import { Routes, Route } from 'react-router-dom';
import Login from './pages/Login.jsx';
import { Header } from './components/layout/header.jsx';
import AppLayout from './components/layout/AppLayout.jsx';
import Perfil from './pages/perfil.jsx';
import SubidaMultimedia from './pages/SubidaMultimedia.jsx';
import EstadoAprobacion from './pages/EstadoAprobacion.jsx';
import ParrillaSemanal from './pages/ParrillaSemanal.jsx'; 
import GestionMultimedia from './pages/GestionMultimedia.jsx'; 
import ReportesAudiencia from './pages/ReportesAudiencia.jsx';

// 💥 IMPORTS CORREGIDOS PARA LA CARPETA /admin/abm/ 💥
import ABMMenu from './pages/admin/abm/ABMMenu.jsx';
import ABMProgramas from './pages/admin/abm/ABMProgramas.jsx';
import ABMSegmentos from './pages/admin/abm/ABMSegmentos.jsx';
import ABMPlataformas from './pages/admin/abm/ABMPlataformas.jsx';
import ABMEmpleados from './pages/admin/abm/ABMEmpleados.jsx'; 

function App() {
  return (
    <>
      <Header/>
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
        
        </Route>
      </Routes>
    </>
  );
}

export default App;