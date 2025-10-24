// src/App.jsx

import './styles/App.css';
import { Routes, Route } from 'react-router-dom';
import Login from './pages/Login.jsx'; 
import { Header } from './components/layout/header.jsx';
import AppLayout from './components/layout/AppLayout.jsx'; 
import Perfil from './pages/perfil.jsx';
import SubidaMultimedia from './pages/SubidaMultimedia.jsx';
const DashboardPage = () => <h1>Página Principal (Dashboard)</h1>;
// ... define el resto de tus componentes de página ...

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
                    
        </Route>
      </Routes>
    </>
  );
}

export default App;
