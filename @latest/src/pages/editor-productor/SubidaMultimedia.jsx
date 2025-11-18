// src/pages/SubidaMultimedia.jsx
import React, { useState, useEffect } from 'react'; // Importamos useEffect
import axios from 'axios'; // Importamos axios
import CargaArchivos from '../../components/ui/CargaArchivos.jsx';
import DetallesEmision from '../../components/ui/DetallesEmision.jsx';
import '../../styles/pages/subidaMultimedia.css';

const INITIAL_FORM_STATE = {
    tituloPrograma: '',
    horaEmision: '',
    horaFinalizacion: '',
    fechasEmision: [''],
    lugarTransmision: '', // Esto guardará el ID de la plataforma seleccionada
    archivo: null,
    informe: null,
};

export default function SubidaMultimedia() {
    const [formData, setFormData] = useState(INITIAL_FORM_STATE);
    const [isUploading, setIsUploading] = useState(false);
    
    // 💥 ESTADO NUEVO: Para guardar la lista de plataformas de la API 💥
    const [listaPlataformas, setListaPlataformas] = useState([]);

    // 💥 EFECTO NUEVO: Cargar plataformas al iniciar el componente 💥
    useEffect(() => {
        const cargarPlataformas = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/plataformas');
                setListaPlataformas(response.data);
            } catch (error) {
                console.error("Error al cargar plataformas:", error);
                // Opcional: Mostrar un mensaje de error al usuario
            }
        };
        cargarPlataformas();
    }, []);


    const handleChange = (name, value) => {
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleDateChange = (index, value) => {
        setFormData(prev => {
            const newFechas = [...prev.fechasEmision];
            newFechas[index] = value;
            return { ...prev, fechasEmision: newFechas };
        });
    };

    const addFecha = () => {
        setFormData(prev => ({
            ...prev,
            fechasEmision: [...prev.fechasEmision, ''] 
        }));
    };

    const removeFecha = (index) => {
        setFormData(prev => ({
            ...prev,
            fechasEmision: prev.fechasEmision.filter((_, i) => i !== index)
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsUploading(true);
        console.log('Datos a subir:', formData);
        
        // TODO: Aquí iría la lógica real de subida del programa
        // Usando los datos de formData, incluyendo el ID de la plataforma (lugarTransmision)
        
        try {
            await new Promise(resolve => setTimeout(resolve, 2000)); 
            
            alert('Programa subido exitosamente!');
            setFormData(INITIAL_FORM_STATE); 
        } catch (error) {
            console.error('Error durante la subida:', error);
            alert('Error al subir el programa.');
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="subida-multimedia-container">
            <form className="formulario-subida" onSubmit={handleSubmit}>
                
                <CargaArchivos handleChange={handleChange} formData={formData} />
                
                <div className="titulo-programa-box">
                    <label htmlFor="tituloPrograma">Título del programa</label>
                    <input 
                        id="tituloPrograma"
                        name="tituloPrograma"
                        type="text"
                        placeholder="Ingrese el título"
                        value={formData.tituloPrograma}
                        onChange={(e) => handleChange(e.target.name, e.target.value)}
                        required
                    />
                </div>

                {/* 💥 PASO LA LISTA DE PLATAFORMAS COMO PROP 💥 */}
                <DetallesEmision 
                    formData={formData} 
                    handleChange={handleChange} 
                    handleDateChange={handleDateChange} 
                    addFecha={addFecha} 
                    removeFecha={removeFecha} 
                    listaPlataformas={listaPlataformas} // <--- AQUÍ
                />

                <button 
                    type="submit" 
                    className="btn-subir-programa"
                    disabled={isUploading} 
                >
                    {isUploading ? 'Subiendo...' : 'Subir Programa'}
                </button>
            </form>
        </div>
    );
}