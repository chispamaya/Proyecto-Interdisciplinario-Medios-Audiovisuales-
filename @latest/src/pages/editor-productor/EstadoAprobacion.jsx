import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { Download, Calendar, Clock, AlertCircle, User, FileText } from 'lucide-react';
import axios from 'axios';
import '../../styles/pages/estadoAprobacion.css';
import logoImage from '../../assets/logo.png';

const api = axios.create({ baseURL: 'http://localhost:8080/api/programas' });

export default function EstadoAprobacion() {
    const { id } = useParams();
    const navigate = useNavigate();
    const currentId = id ? parseInt(id, 10) : null;

    const [listaProgramas, setListaProgramas] = useState([]);
    const [programaDetalle, setProgramaDetalle] = useState(null);
    const [loading, setLoading] = useState(true);

    // 1. Cargar Lista
    useEffect(() => {
        const fetchLista = async () => {
            try {
                const response = await api.get('/aprobacion');
                setListaProgramas(response.data);
                
                // Si hay datos y no hay ID seleccionado, ir al primero
                if (!id && response.data.length > 0) {
                    const primerId = response.data[0].idPrograma || response.data[0].id;
                    navigate(`/estado/${primerId}`);
                }
            } catch (err) { console.error("Error lista:", err); }
        };
        fetchLista();
    }, [id, navigate]);

    // 2. Cargar Detalle
    useEffect(() => {
        if (!currentId) {
            setProgramaDetalle(null);
            setLoading(false);
            return;
        }
        const fetchDetalle = async () => {
            setLoading(true);
            try {
                const response = await api.get(`/${currentId}`);
                setProgramaDetalle(response.data);
            } catch (err) {
                console.error("Error detalle:", err);
                setProgramaDetalle(null);
            } finally { setLoading(false); }
        };
        fetchDetalle();
    }, [currentId]);

    const getCleanFileName = (ruta) => {
        if (!ruta || ruta === "null" || ruta === "") return null;
        return ruta.split(/[/\\]/).pop();
    };

    const getNombreUsuario = () => {
        if (programaDetalle?.usuarioNombre) return programaDetalle.usuarioNombre;
        const item = listaProgramas.find(p => (p.idPrograma === currentId || p.id === currentId));
        return item ? item.propuestaDe : "Usuario Desconocido";
    };

    const handleDownload = () => {
        if (!programaDetalle) return;

        // Array de descargas
        const targets = [
            { ruta: programaDetalle.rutaArchivo, tipo: "contenido" },
            { ruta: programaDetalle.rutaInforme, tipo: "informe" }
        ];

        let descargasIniciadas = 0;

        targets.forEach((target) => {
            const nombreLimpio = getCleanFileName(target.ruta);
            
            // Solo descargar si hay nombre y no es una cadena vacía
            if (nombreLimpio && nombreLimpio.length > 3) { 
                const url = `http://localhost:8080/api/programas/download/${nombreLimpio}`;
                console.log(`Descargando ${target.tipo}: ${url}`);
                
                setTimeout(() => {
                    window.open(url, '_blank');
                }, descargasIniciadas * 800); // Aumenté el delay a 800ms para seguridad
                
                descargasIniciadas++;
            }
        });

        if (descargasIniciadas === 0) {
            alert("⚠️ No se encontraron archivos válidos para descargar.");
        }
    };

    const handleEstado = async (estado) => {
        if (!currentId) return;
        try {
            await api.put(`/${currentId}/estado`, { estado });
            alert(`Programa ${estado}.`);
            // Forzar recarga completa para actualizar lista
            window.location.href = "/estado"; 
        } catch (e) { alert("Error al actualizar estado"); }
    };

    if (loading && currentId) return <div className="loading-container">Cargando...</div>;

    // 🚨 ESTADO VACÍO: Si no hay programas en la lista
    if (listaProgramas.length === 0) {
        return (
            <div className="detalle-propuesta-container" style={{justifyContent:'center', alignItems:'center', height:'100vh'}}>
                 <div className="empty-state" style={{textAlign:'center'}}>
                    <AlertCircle size={64} color="#ccc" style={{marginBottom:20}}/>
                    <h2>No hay contenidos pendientes de revisión</h2>
                    <p>Todos los programas han sido aprobados o rechazados.</p>
                </div>
            </div>
        );
    }

    return (
        <div className="detalle-propuesta-container">
            {/* SIDEBAR */}
            <div className="propuesta-sidebar">
                {listaProgramas.map((p) => {
                    const pId = p.idPrograma || p.id;
                    return (
                        <Link 
                            to={`/estado/${pId}`} 
                            key={pId} 
                            className="propuesta-link"
                            style={{ textDecoration: 'none', color: 'inherit', display: 'block' }}
                        >
                            <div className={`propuesta-item ${pId === currentId ? 'active' : ''}`}>
                                <img className="propuesta-logo-sm" src={logoImage} alt="Logo" />
                                <div className="propuesta-info">
                                    <p className="propuesta-titulo">{p.tituloPrograma || p.nombre}</p>
                                    <p className="propuesta-proponente">De: {p.propuestaDe || p.usuario}</p>
                                </div>
                            </div>
                        </Link>
                    );
                })}
            </div>

            {/* MAIN */}
            <main className="propuesta-main-content">
                {programaDetalle ? (
                    <>
                        <div className="propuesta-header">
                            <img className="propuesta-logo-lg" src={logoImage} alt="Logo" />
                            <div className="propuesta-titulo-box">
                                <h1 className="propuesta-titulo-lg">{programaDetalle.nombre}</h1>
                                <p className="propuesta-proponente-lg">
                                    <User size={18} style={{marginRight:5, verticalAlign:'middle'}}/>
                                    Propuesta de: <strong>{getNombreUsuario()}</strong>
                                </p>
                            </div>
                        </div>

                        <div className="detalles-grid">
                            {/* CARD FECHAS */}
                            <div className="card-detalle fecha-card">
                                <h2 className='card-title'>DÍAS DE EMISIÓN</h2>
                                <div className="fechas-list-wrapper">
                                    {programaDetalle.dias && programaDetalle.dias.length > 0 ? (
                                        programaDetalle.dias.map((d, i) => (
                                            <div key={i} className="detalle-item">
                                                <Calendar size={20} className="detalle-icon"/>
                                                <p className="detalle-text">{d.dia || d}</p>
                                            </div>
                                        ))
                                    ) : (
                                        <p className="no-data">Sin días asignados</p>
                                    )}
                                </div>
                            </div>

                            {/* CARD HORARIOS */}
                            <div className="horarios-card">
                                <div className="card-detalle horario-emision-card">
                                    <h2 className='card-title'>INICIO</h2>
                                    <div className="detalle-item">
                                        <Clock size={24} className="detalle-icon"/>
                                        <p className="detalle-text">{programaDetalle.horaInicio || '--:--'}</p>
                                    </div>
                                </div>
                                <div className="card-detalle horario-finalizacion-card">
                                    <h2 className='card-title'>FIN</h2>
                                    <div className="detalle-item">
                                        <Clock size={24} className="detalle-icon"/>
                                        <p className="detalle-text">{programaDetalle.horaFin || '--:--'}</p>
                                    </div>
                                </div>
                            </div>

                            {/* CARD DESCARGA */}
                            <div className="descargar-card-wrapper">
                                <button className="btn-descargar" onClick={handleDownload}>
                                    <Download size={24} />
                                    <span>Descargar Contenido e Informe</span>
                                </button>
                                <div style={{marginTop:10, fontSize:'0.85rem', color:'#555', display:'flex', gap:10, justifyContent:'center'}}>
                                    {getCleanFileName(programaDetalle.rutaArchivo) && (
                                        <span style={{display:'flex', alignItems:'center'}}><FileText size={14} style={{marginRight:3}}/> Video</span>
                                    )}
                                    {getCleanFileName(programaDetalle.rutaInforme) && (
                                        <span style={{display:'flex', alignItems:'center'}}><FileText size={14} style={{marginRight:3}}/> Informe</span>
                                    )}
                                </div>
                            </div>
                        </div>

                        <div className="accion-buttons">
                            <button className="btn-accion btn-no-publicar" onClick={() => handleEstado("Rechazado")}>
                                Rechazar
                            </button>
                            <button className="btn-accion btn-publicar" onClick={() => handleEstado("Aprobado")}>
                                Aprobar
                            </button>
                        </div>
                    </>
                ) : (
                    <div className="empty-state">
                        <AlertCircle size={48} color="#ccc"/>
                        <p>Selecciona un programa para ver detalles.</p>
                    </div>
                )}
            </main>
        </div>
    );
}