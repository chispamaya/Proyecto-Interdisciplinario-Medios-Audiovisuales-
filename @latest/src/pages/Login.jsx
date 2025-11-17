import React, { useState } from 'react';
import axios from 'axios'; // <-- 1. IMPORTAR AXIOS
import logoImage from '../assets/logo.png';
import "../styles/pages/login.css";

export default function Login() {

    // 2. Creamos "estados" para guardar lo que el usuario escribe
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null); // Para mensajes de error

    // 3. Esta función maneja la llamada a la API
    const handleSubmit = (e) => {
        // Prevenimos que el formulario HTML se envíe (ya no usamos 'action')
        e.preventDefault(); 
        setError(null); // Limpiamos errores viejos

        // 4. ¡LLAMAMOS A TU API DE SPRING BOOT!
        axios.post('http://localhost:8080/api/usuarios/login', {
            email: email,       // Pasamos el email del estado
            password: password  // Pasamos el password del estado
        })
        .then(response => {
            // 5. ¡ÉXITO! El backend nos devolvió el objeto Usuario
            const usuario = response.data;

            if (usuario) {
                console.log("Login exitoso:", usuario);
                
                // --- ¡AQUÍ ESTÁ LA MAGIA! ---
                // Guardamos el ID del usuario en el "almacenamiento"
                // del navegador para usarlo en las otras páginas.
                localStorage.setItem('usuarioId', usuario.id);
                localStorage.setItem('usuarioNombre', usuario.nombre);
                
                // Redirigimos al usuario a la página de perfil
                window.location.href = '/perfil'; 
            } else {
                // Esto pasa si el Service devolvió null (contraseña/email incorrecto)
                setError("Email o contraseña incorrectos.");
            }
        })
        .catch(err => {
            // Esto pasa si el backend está apagado o hay un error 500
            console.error("Error en el login:", err);
            setError("Error al conectar con el servidor.");
        });
    }

    return (
        <>
            <div className="registrarse-dark">     
            
                {/* 6. Cambiamos <form> para que llame a handleSubmit */}
            <form className="form" onSubmit={handleSubmit}>
            <div className="top">
                <h1>INICIAR SESION</h1>
            </div>
            <hr />

                {/* 7. Conectamos los inputs a los "estados" */}
            <label className="label" htmlFor="email">Ingrese su email</label>
            <input 
                className="inputForm" 
                type="email" 
                name="email" 
                required 
                value={email} // El valor es el del estado
                onChange={e => setEmail(e.target.value)} // Actualiza el estado
            /> 

            <label className="label" htmlFor="pasword">Ingrese su contraseña</label>
            <input 
                className="inputForm" 
                name="password" 
                type="password" 
                required 
                value={password} // El valor es el del estado
                onChange={e => setPassword(e.target.value)} // Actualiza el estado
            /> 

                {/* Mostramos un error si existe */}
                {error && <p className="error-login">{error}</p>}

            <button type="submit">Iniciar sesion</button>
            
            </form>
        </div>
    </>
    
  )
}