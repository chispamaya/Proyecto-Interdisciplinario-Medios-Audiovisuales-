import React, { useState, useEffect } from 'react';
import axios from 'axios';
import CargaArchivos from '../../components/ui/CargaArchivos.jsx';
import DetallesEmision from '../../components/ui/DetallesEmision.jsx';
import '../../styles/pages/subidaMultimedia.css';

const api = axios.create({ baseURL: 'http://localhost:8080/api/programas' });

const INITIAL_STATE = {
    tituloPrograma: '',
    categoria: 'SERIE',
    horaEmision: '',
    horaFinalizacion: '',
    fechasEmision: [''],
    lugarTransmision: '', 
    archivo: null,
    informe: null
};

export default function SubidaMultimedia() {
    const [formData, setFormData] = useState(INITIAL_STATE);
    const [isUploading, setIsUploading] = useState(false);
    const [listaPlataformas, setListaPlataformas] = useState([]);

    useEffect(() => {
        const fetchPlataformas = async () => {
            try {
                const res = await axios.get('http://localhost:8080/api/plataformas'); // Ajusta puerto si es necesario
                setListaPlataformas(res.data);
            } catch (e) { 
                setListaPlataformas([{id: 1, nombre: "Plataforma Default"}]);
            }
        };
        fetchPlataformas();
    }, []);

    const handleChange = (name, value) => setFormData(prev => ({ ...prev, [name]: value }));
    
    const handleDateChange = (index, value) => {
        const newFechas = [...formData.fechasEmision];
        newFechas[index] = value;
        setFormData(prev => ({ ...prev, fechasEmision: newFechas }));
    };

    const addFecha = () => setFormData(prev => ({ ...prev, fechasEmision: [...prev.fechasEmision, ''] }));
    const removeFecha = (index) => setFormData(prev => ({ ...prev, fechasEmision: prev.fechasEmision.filter((_, i) => i !== index) }));

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsUploading(true);

        try {
            let nombreArchivoFinal = "";
            let nombreInformeFinal = "";

            // 1. Subir Archivos
            if (formData.archivo) {
                const d = new FormData(); d.append("file", formData.archivo);
                const r = await api.post("/upload", d, { headers: { "Content-Type": "multipart/form-data" }});
                nombreArchivoFinal = r.data.fileName;
            }
            if (formData.informe) {
                const d = new FormData(); d.append("file", formData.informe);
                const r = await api.post("/upload", d, { headers: { "Content-Type": "multipart/form-data" }});
                nombreInformeFinal = r.data.fileName;
            }

            // 2. Preparar DTO con Fechas
            // Convertimos ['2025-10-10'] -> [{dia: '2025-10-10'}]
            const diasParaGuardar = formData.fechasEmision
                .filter(f => f && f.trim() !== "") // Filtrar vacíos
                .map(fechaStr => ({ dia: fechaStr }));

            const payload = {
                nombre: formData.tituloPrograma,
                categoria: formData.categoria,
                horaInicio: formData.horaEmision ? formData.horaEmision + ":00" : null,
                horaFin: formData.horaFinalizacion ? formData.horaFinalizacion + ":00" : null,
                idPlataforma: parseInt(formData.lugarTransmision || 1),
                rutaArchivo: nombreArchivoFinal,
                rutaInforme: nombreInformeFinal,
                formatoArchivo: "MP4",
                formatoInforme: "PDF",
                estadoAprobacion: "En Revisión",
                dias: diasParaGuardar // <--- AQUÍ ENVIAMOS LAS FECHAS
            };

            await api.post("", payload);
            alert("✅ Programa creado exitosamente");
            setFormData(INITIAL_STATE);

        } catch (error) {
            console.error(error);
            alert("Error al crear programa");
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="subida-multimedia-container">
            <form className="formulario-subida" onSubmit={handleSubmit}>
                <CargaArchivos handleChange={handleChange} formData={formData} />
                
                <div className="titulo-programa-box" style={{marginTop:20}}>
                    <label>Título</label>
                    <input name="tituloPrograma" type="text" value={formData.tituloPrograma} onChange={(e)=>handleChange(e.target.name, e.target.value)} required />
                </div>

                <div className="titulo-programa-box" style={{marginTop:10}}>
                    <label>Categoría</label>
                    <select name="categoria" value={formData.categoria} onChange={(e)=>handleChange(e.target.name, e.target.value)} required style={{width:'100%', padding:10, borderRadius:5, border:'1px solid #ccc'}}>
                        <option value="SERIE">Serie</option>
                        <option value="PELICULA">Película</option>
                        <option value="NOTICIERO">Noticiero</option>
                    </select>
                </div>

                <DetallesEmision 
                    formData={formData} 
                    handleChange={handleChange} 
                    handleDateChange={handleDateChange} 
                    addFecha={addFecha} 
                    removeFecha={removeFecha} 
                    listaPlataformas={listaPlataformas}
                />

                <button type="submit" className="btn-subir-programa" disabled={isUploading}>
                    {isUploading ? "Guardando..." : "Crear Programa"}
                </button>
            </form>
        </div>
    );
}