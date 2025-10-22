import React, { useState } from 'react';
import logoImage from '../assets/logo.png';
import "../styles/pages/login.css";

export default function Login() {

  return (
    <>

        <div className="registrarse-dark">     
            <form className="form" action="/php/inicioSesion.php" method="post">
            <div className="top">
                <h1>INICIAR SESION</h1>
            </div>
            <hr />

            <label className="label" htmlFor="email">Ingrese su email</label> {/* htmlFor en lugar de for */}
            <input className="inputForm" type="email" name="email" required /> {/* Input cerrado */}

            <label className="label" htmlFor="pasword">Ingrese su contraseña</label>
            <input className="inputForm" name="password" type="password" required /> {/* Input cerrado */}

            <button type="submit">Iniciar sesion</button>
            
            </form>
        </div>
    </>
    
  )
}

