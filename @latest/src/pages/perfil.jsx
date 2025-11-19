import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // Para redirigir al login
import logoImage from '../assets/logo.png'
import '../styles/pages/perfil.css'
import { ShieldCheck } from 'lucide-react';

export default function Perfil() { 
    const navigate = useNavigate();

    // Estados
    const [perfil, setPerfil] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    // Estado para el input de nueva contraseña
    const [nuevaPassword, setNuevaPassword] = useState("");

    // Estado para la imagen (Logo por defecto, Http Cat en respuesta)
    const [imagenPerfil, setImagenPerfil] = useState(logoImage);

    // ID del usuario logueado
    const [idUsuario, setIdUsuario] = useState(null);

    // 1. Al cargar, buscamos el ID en localStorage
    useEffect(() => {
        const storedId = localStorage.getItem('usuarioId');
        
        if (storedId) {
            setIdUsuario(storedId);
        } else {
            // Si no hay ID, no está logueado -> Redirigir al Login
            navigate('/'); 
        }
    }, [navigate]);

    // 2. Cuando tenemos el ID, buscamos los datos en la API
    useEffect(() => {
        if (idUsuario) {
            axios.get(`http://localhost:8080/api/usuarios/perfil/${idUsuario}`)
                .then(response => {
                    setPerfil(response.data);
                    setLoading(false);
                })
                .catch(err => {
                    console.error("Error al cargar el perfil:", err);
                    setError("No se pudo cargar el perfil.");
                    setLoading(false);
                });
        }
    }, [idUsuario]);

    // --- LÓGICA: CAMBIAR CONTRASEÑA ---
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!nuevaPassword) {
            alert("Por favor, ingrese una nueva contraseña.");
            return;
        }

        try {
            // Objeto que espera el backend (ajustar según tu DTO/Map)
            const payload = {
                idUsuario: idUsuario,
                nuevaPassword: nuevaPassword
            };

            // Llamada real a la API
            const response = await axios.put(`http://localhost:8080/api/usuarios/contrasenia`, payload);
            
            // ÉXITO: Gato 200
            console.log("Contraseña cambiada con éxito:", response.data);
            setImagenPerfil(`https://http.cat/${response.status}`);
            alert("Contraseña cambiada correctamente.");
            setNuevaPassword(""); 

        } catch (error) {
            console.error("Error al cambiar contraseña:", error);
            
            // ERROR: Gato con el status code del error (ej: 404, 500)
            const status = error.response ? error.response.status : 500;
            setImagenPerfil(`https://http.cat/${status}`);
            
            alert(`Error al cambiar la contraseña. Código: ${status}`);
        }
    }

    // --- LÓGICA: CERRAR SESIÓN ---
    const handleLogout = () => {
        if (window.confirm("¿Estás seguro de que querés cerrar sesión?")) {
            // 1. Limpiamos el almacenamiento
            localStorage.removeItem('usuarioId');
            localStorage.removeItem('usuarioNombre');
            localStorage.removeItem('usuarioRol');
            
            // 2. Redirigimos al Login
            navigate('/'); 
        }
    }

    // --- RENDERIZADO ---
    if (loading) return <div className="contenedor-principal"><h1>Cargando perfil...</h1></div>;
    if (error) return <div className="contenedor-principal"><h1>{error}</h1></div>;
    if (!perfil) return <div className="contenedor-principal"><h1>No se encontró el perfil.</h1></div>;

    return (
        <>
            <div className="contenedor-principal">
                <main className="contenido-perfil">

                    <div className="logo-contenedor">
                        {/* La imagen ahora es dinámica para mostrar el gato http */}
                        <img className="logo-perfil" src={imagenPerfil} alt="Logo o Status" />
                    </div>
                    
                    <div className="datos-perfil"> 
                        <h1>{perfil.nombreUsuario}</h1> 
                        <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                            <ShieldCheck size={20} color="var(--texto)" style={{ marginRight: '10px' }} />
                            <h2 className='adm'>{perfil.nombreRol.toUpperCase()}</h2> 
                        </div>
                    </div>

                    <form className="formulario-perfil" onSubmit={handleSubmit}>
                        <div className="campo-form">
                            <label htmlFor="password">Contraseña</label>
                            <input 
                                type="password" 
                                id="password" 
                                name="pass" 
                                placeholder="Nueva contraseña"
                                value={nuevaPassword}
                                onChange={(e) => setNuevaPassword(e.target.value)}
                            />
                        </div>

                        <div className="acciones-form-centrado">
                            <button type="button" className="btn-descartar" onClick={handleLogout}>
                                Cerrar Sesión
                            </button>
                            <button type="submit" className="btn-guardar">
                                Guardar
                            </button>
                        </div>
                    </form>

                </main>
            </div>
        </>
    )
}