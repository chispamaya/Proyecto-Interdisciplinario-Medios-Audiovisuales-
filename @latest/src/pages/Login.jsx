import React, { useState } from 'react';
import axios from 'axios'; 
import logoImage from '../assets/logo.png';
import "../styles/pages/login.css";

export default function Login() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault(); 
        setError(null); 

        axios.post('http://localhost:8080/api/usuarios/login', {
            email: email,       
            password: password  
        })
        .then(response => {
            const usuario = response.data;

            if (usuario) {
                console.log("Login exitoso:", usuario);
                
                // --- ¡AQUÍ ESTABA EL FALTANTE! ---
                localStorage.setItem('usuarioId', usuario.id);
                localStorage.setItem('usuarioNombre', usuario.nombre);
                
                // 💥 ¡AGREGÁ ESTA LÍNEA! 💥
                // Sin esto, el Sidebar no sabe qué opciones mostrarte.
                localStorage.setItem('usuarioRol', usuario.idRol); 
                
                // Redirigimos (Usamos window.location para forzar que el Sidebar se recargue)
                window.location.href = '/perfil'; 
            } else {
                setError("Email o contraseña incorrectos.");
            }
        })
        .catch(err => {
            console.error("Error en el login:", err);
            setError("Error al conectar con el servidor.");
        });
    }

    return (
        <>
            <div className="registrarse-dark">     
                <form className="form" onSubmit={handleSubmit}>
                    <div className="top">
                        <h1>INICIAR SESION</h1>
                    </div>
                    <hr />

                    <label className="label" htmlFor="email">Ingrese su email</label>
                    <input 
                        className="inputForm" 
                        type="email" 
                        name="email" 
                        required 
                        value={email} 
                        onChange={e => setEmail(e.target.value)} 
                    /> 

                    <label className="label" htmlFor="pasword">Ingrese su contraseña</label>
                    <input 
                        className="inputForm" 
                        name="password" 
                        type="password" 
                        required 
                        value={password} 
                        onChange={e => setPassword(e.target.value)} 
                    /> 

                    {error && <p className="error-login" style={{color:'red', marginTop:'10px'}}>{error}</p>}

                    <button type="submit">Iniciar sesion</button>
                
                </form>
            </div>
        </>
    )
}