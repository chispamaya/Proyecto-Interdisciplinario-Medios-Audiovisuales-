import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './EnVivo.css';

export default function EnVivo() {
    // Estado para verificar si el backend responde
    const [status, setStatus] = useState('loading'); // 'ok', 'error'
    const [errorCode, setErrorCode] = useState(null);

    useEffect(() => {
        // Hacemos un ping ligero al backend para ver si está vivo
        // Usamos /api/feed porque sabemos que existe
        fetch('http://localhost:8080/api/feed')
            .then(res => {
                if (res.ok) {
                    setStatus('ok');
                } else {
                    setStatus('error');
                    setErrorCode(res.status);
                }
            })
            .catch(() => {
                setStatus('error');
                setErrorCode(503); // Service Unavailable
            });
    }, []);

    if (status === 'loading') {
        return (
            <main className="envivo-main-content">
                 <div className="loading-spinner">Cargando transmisión...</div>
            </main>
        );
    }

    // SI HAY ERROR -> MOSTRAMOS HTTP CAT
    if (status === 'error') {
        return (
            <main className="envivo-main-content">
                <div className="error-container">
                    <h2>¡Ups! Problemas técnicos ({errorCode})</h2>
                    <p>No podemos conectar con la señal en vivo.</p>
                    <img 
                        src={`https://http.cat/${errorCode}`} 
                        alt={`Error ${errorCode}`} 
                        className="http-cat-img" 
                    />
                    <div style={{marginTop: '20px'}}>
                        <Link to="/encuestas-espectador" className="btn-encuestas-vivo">
                            Ir al Foro (Si funciona)
                        </Link>
                    </div>
                </div>
            </main>
        );
    }

    // SI TODO ESTÁ OK -> MOSTRAMOS EL VIDEO
    return (
        <main className="envivo-main-content">
            <div className="envivo-header-area">
                <h1 className="envivo-title">En Vivo</h1>
                <Link to="/encuestas-espectador" className="btn-encuestas-vivo">
                    Ir a Encuestas y Foro
                </Link>
            </div>

            <div className="envivo-container-white">
                <div className="envivo-video-wrapper">
                    <iframe
                        className="envivo-iframe"
                        src="https://www.youtube.com/embed/live_stream?channel=UCxxxxxxxxxxxx" 
                        title="Transmisión En Vivo"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                        allowFullScreen
                    ></iframe>
                </div>
            </div>
        </main>
    );
}