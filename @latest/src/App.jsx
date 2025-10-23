// src/App.jsx

import './styles/App.css';
import { Routes, Route } from 'react-router-dom';
import Login from './pages/Login.jsx'; 
import { Header } from './components/layout/header.jsx';
import AppLayout from './components/layout/AppLayout.jsx'; 

// Componentes de ejemplo (usados para las rutas con Sidebar)
const DashboardPage = () => <h1>Página Principal (Dashboard)</h1>;
// ... define el resto de tus componentes de página ...

function App() {
  return ( 
    <>
      <Header/> 
      
      <Routes>
        
        <Route path="/" element={<Login />} />
        
        <Route element={<AppLayout />}>
          
          <Route path="/inicio" element={<DashboardPage />} /> 
                    
        </Route>
      </Routes>
    </>
  );
}

export default App;