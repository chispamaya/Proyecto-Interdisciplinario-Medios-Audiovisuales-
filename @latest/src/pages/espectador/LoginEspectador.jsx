// src/pages/espectador/LoginEspectador.jsx (NUEVO)

import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../styles/pages/loginEspectador.css'; // CSS para esta página
import logo from '../../assets/logo.png';
import InputIcono from '../../components/ui/InputIcono.jsx'; // 💥 RUTA CORREGIDA

export default function LoginEspectador() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = (e) => {
        e.preventDefault();
        // Lógica de autenticación de *espectador*...
        console.log("Intentando iniciar sesión de ESPECTADOR con:", email, password);

        // Simulación de login exitoso
        navigate('/en-vivo'); 
    };

    return (
        <div className="login-espectador-container">
            <div className="login-espectador-box">
                <div className="login-espectador-logo">
                    <img src={logo} alt="Logo Canal" />
                </div>
                <h2>Bienvenido</h2>
                <p className="login-espectador-subtitulo">Inicia sesión para continuar</p>
                <form onSubmit={handleLogin}>
                    <InputIcono
                        icono="bi bi-envelope-fill"
                        type="email"
                        placeholder="Correo electrónico"
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
                    <button type="submit" className="login-espectador-btn">
                        Ingresar
                    </button>
                </form>

                {/* Link a la página de Registro */}
                <div className="login-espectador-registro-link">
                    <p>
                        ¿No tenés cuenta? <Link to="/registro">Registrate acá</Link>
                    </p>
                </div>

            </div>
        </div>
    );
}