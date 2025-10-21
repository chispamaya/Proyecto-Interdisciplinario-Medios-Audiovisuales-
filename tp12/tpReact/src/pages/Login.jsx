import React, { useState } from 'react';
import './styles/Login.css';
export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  return (
    <>
        <div className="header">
        <img
        className="logo"
        src="/logo/logo.png"/>
    </div>
    <div className="registrarse-dark">     
        <form className="form" action="/php/inicioSesion.php" method="post">
        <div className="top">
            <h1>INICIAR SESION</h1>
        </div>
        <hr /> {/* Etiqueta <hr> cerrada */}

        <label className="label" htmlFor="email">Ingrese su email</label> {/* htmlFor en lugar de for */}
        <input className="inputForm" type="email" name="email" required /> {/* Input cerrado */}

        <label className="label" htmlFor="pasword">Ingrese su contraseña</label>
        <input className="inputForm" name="password" type="password" required /> {/* Input cerrado */}

        <button type="submit">Iniciar sesion</button>
        
        <p>No tenes cuenta? <a href="/InicioSesionYregistro/registro/registro.html">Regístrate acá</a></p>
        </form>
    </div>
    </>
    
  )
}

