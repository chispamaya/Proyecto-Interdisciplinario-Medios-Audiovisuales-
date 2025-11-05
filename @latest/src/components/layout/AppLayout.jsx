import React from 'react';
import { Outlet } from 'react-router-dom'; 
import SideBar from './sidebar.jsx'; 
import { Header } from './header.jsx'; // 1. Importa el Header

export default function AppLayout() {
  return (
    <div className="app-container-con-sidebar">
      
      {/* 2. La Sidebar es un item flex (ya no es 'fixed') */}
      <SideBar idRol="admin" />
      
      {/* 3. El content-area es el otro item flex (ocupa el resto) */}
      <div className="content-area">
        
        {/* 4. El Header está DENTRO de content-area */}
        <Header /> 

        {/* 5. El Outlet (tu página) está en un 'main' con fondo gris */}
        <main className="page-content-wrapper">
          <Outlet /> 
        </main>
      </div>
    </div>
  );
}