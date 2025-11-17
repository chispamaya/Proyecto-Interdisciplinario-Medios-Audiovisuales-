import React, { useState, useEffect } from 'react'; // <-- 1. IMPORTAMOS a useState y useEffect
import axios from 'axios'; // <-- 2. IMPORTAMOS AXIOS

import logoImage from '../assets/logo.png'
import '../styles/pages/perfil.css'
import { ShieldCheck } from 'lucide-react';

export default function Perfil({idUsuario}) { 

    // --- 3. CREAMOS ESTADOS ---
    // (Para guardar los datos que vienen de la API)
    const [perfil, setPerfil] = useState(null); // Para el DTO PerfilDTO
    const [loading, setLoading] = useState(true); // Para el mensaje "Cargando..."
    const [error, setError] = useState(null); // Para cualquier error

    // --- 4. USEEFFECT (LA LLAMADA A LA API) ---
    // (Esto se ejecuta 1 sola vez cuando la página carga)
    useEffect(() => {
        // TODO: Tenés que sacar el ID del usuario logueado de algún lado
        // Por ahora, usamos el 'idUsuario' que pasaste como prop.
        // Si 'idUsuario' no existe, usamos 1 como fallback.
        const idUsuarioLogueado = idUsuario || 1; 

        // Esta es la URL de tu UsuarioController (@GetMapping("/perfil/{id}"))
        const API_URL = `http://localhost:8080/api/usuarios/perfil/${idUsuarioLogueado}`;

        axios.get(API_URL)
            .then(response => {
                // ¡ÉXITO! response.data es tu PerfilDTO
                setPerfil(response.data);
                setLoading(false);
            })
            .catch(err => {
                // ¡ERROR!
                console.error("Error al cargar el perfil:", err);
                setError("No se pudo cargar el perfil.");
                setLoading(false);
            });
    }, [idUsuario]); // Se ejecuta cada vez que 'idUsuario' cambie

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Contraseña actualizada');
  }

  const handleLogout = () => {
    console.log('Cerrar Sesión');
  }

    // --- 5. RENDERIZADO CONDICIONAL ---
    // (Mostramos mensajes mientras la API responde)

    if (loading) {
        return <div className="contenedor-principal"><h1>Cargando perfil...</h1></div>;
    }

    if (error) {
        return <div className="contenedor-principal"><h1>{error}</h1></div>;
    }

    if (!perfil) {
        return <div className="contenedor-principal"><h1>No se encontró el perfil.</h1></div>;
    }

    // --- 6. RENDERIZADO CON DATOS REALES ---
    // (Una vez que 'perfil' tiene los datos de la API)
  return (
    <>
      <div className="contenedor-principal">
        <main className="contenido-perfil">

          <div className="logo-contenedor">
            <img className="logo-perfil" src={logoImage} alt="Logo" />
          </div>
          
          <div className="datos-perfil"> 
                        {/* 💥 CAMBIO HECHO AQUÍ 💥 */}
            <h1>{perfil.nombreUsuario}</h1> 
            <ShieldCheck size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                        {/* 💥 CAMBIO HECHO AQUÍ 💥 */}
            <h2 className='adm'>{perfil.nombreRol.toUpperCase()}</h2> 
          </div>

          <form className="formulario-perfil" onSubmit={handleSubmit}>
            <div className="campo-form">
              <label htmlFor="password">Contraseña</label>
              <input 
                type="password" 
                id="password" 
                name="pass" 
                placeholder="Cambiar contraseña"
              />
            </div>


            <div className="acciones-form-centrado">
              <button type="button" className="btn-descartar" onClick={handleLogout}>Cerrar Sesión</button>
              <button type="submit" className="btn-guardar">Guardar</button>
            </div>
          </form>

        </main>
      </div>
    </>
  )
}