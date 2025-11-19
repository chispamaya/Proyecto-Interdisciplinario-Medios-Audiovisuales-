import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../styles/pages/registro.css'; 
import logo from '../../assets/logo.png';
import InputIcono from '../../components/ui/InputIcono.jsx';

export default function Registro() {
    const [nombre, setNombre] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleRegistro = async (e) => {
        e.preventDefault();
        
        // Objeto que espera el Backend (Usuario.java)
        const nuevoUsuario = {
            nombre: nombre,
            email: email,
            contrasenia: password, // En Java se llama 'contrasenia'
            idRol: 12 // ID del Rol Espectador según tu DB.sql
        };

        try {
            const response = await fetch('http://localhost:8080/api/usuarios', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(nuevoUsuario)
            });

            // Tu backend devuelve un String plano (mensaje), no un JSON complejo
            const mensaje = await response.text();

            if (response.ok) {
                if (mensaje.startsWith("Error")) {
                    // Si el backend devuelve un mensaje de error controlado (ej: email duplicado)
                    alert(mensaje);
                } else {
                    // Éxito
                    alert("¡Registro exitoso! Ahora inicia sesión.");
                    navigate('/login-espectador'); 
                }
            } else {
                alert("Hubo un error en el servidor.");
            }

        } catch (error) {
            console.error("Error de conexión:", error);
            alert("No se pudo conectar con el servidor.");
        }
    };

    return (
        <div className="registro-container">
            <div className="registro-box">
                <div className="registro-logo">
                    <img src={logo} alt="Logo SofTLUTION" />
                </div>
                <h2>Crear Cuenta</h2>
                <form onSubmit={handleRegistro}>
                    <InputIcono
                        icono="bi bi-person-fill"
                        type="text"
                        placeholder="Nombre completo"
                        value={nombre}
                        onChange={(e) => setNombre(e.target.value)}
                        required
                    />
                    <InputIcono
                        icono="bi bi-envelope-fill"
                        type="email"
                        placeholder="Correo electrónico (Gmail)"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <InputIcono
                        icono="bi bi-lock-fill"
                        type="password"
                        placeholder="Contraseña"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <button type="submit" className="registro-btn">
                        Registrarse
                    </button>
                </form>

                <div className="registro-login-link">
                    <p>
                        ¿Ya tenés cuenta? <Link to="/login-espectador">Iniciá sesión acá</Link>
                    </p>
                </div>

            </div>
        </div>
    );
}