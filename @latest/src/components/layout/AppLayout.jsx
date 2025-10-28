import React from 'react';
import { Outlet } from 'react-router-dom'; // 👈 Necesario para rutas anidadas
import SideBar from './sidebar.jsx'; // Importa tu SideBar

export default function AppLayout() {
  return (
    <div className="app-container-con-sidebar">
      
      <SideBar idRol="admin" />
      
      <div className="content-area">
        <Outlet /> 
      </div>
    </div>
  );
}

// ### ¿Qué es `Outlet`?
// `Outlet` es un componente de React Router. Cuando usas rutas anidadas, el `Outlet` marca dónde debe renderizarse el **componente de la ruta hija**. En este caso, si la ruta es `/inicio`, el `Outlet` se reemplaza por el contenido de la página de inicio.