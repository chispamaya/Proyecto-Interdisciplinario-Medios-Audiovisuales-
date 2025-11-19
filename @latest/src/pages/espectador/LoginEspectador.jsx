import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../styles/pages/loginEspectador.css';
import logo from '../../assets/logo.png';
import InputIcono from '../../components/ui/InputIcono.jsx';

export default function LoginEspectador() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        
        localStorage.clear(); // Limpiar sesión previa

        try {
            const response = await fetch('http://localhost:8080/api/usuarios/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: email, password: password })
            });

            const textData = await response.text();
            
            if (!textData) {
                alert("Credenciales incorrectas.");
                return;
            }

            const usuario = JSON.parse(textData);

            if (usuario && usuario.id) {
                localStorage.setItem('usuario', JSON.stringify(usuario));
                navigate('/en-vivo'); 
            } else {
                alert("Error al iniciar sesión.");
            }

        } catch (error) {
            console.error("Error:", error);
            alert("Error de conexión.");
        }
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
                <div className="login-espectador-registro-link">
                    <p>¿No tenés cuenta? <Link to="/registro">Registrate acá</Link></p>
                </div>
            </div>
        </div>
    );
}