// src/pages/espectador/Registro.jsx (CORREGIDO)

import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../styles/pages/registro.css'; 
import logo from '../../assets/logo.png';
import InputIcono from '../../components/ui/InputIcono.jsx'; // 💥 RUTA CORREGIDA

export default function Registro() {
    const [nombre, setNombre] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleRegistro = (e) => {
        e.preventDefault();
        // Lógica de registro...
        console.log("Registrando usuario:", nombre, email, password);

        // Simulación de registro exitoso
        alert("¡Registro exitoso! Ahora inicia sesión.");
        navigate('/login-espectador'); // Lo mandamos al nuevo login de espectador
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