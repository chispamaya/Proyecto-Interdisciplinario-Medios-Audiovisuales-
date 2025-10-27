// src/components/form/CargaArchivos.jsx
import React from 'react';
import { FileText, Video } from 'lucide-react'; // Íconos específicos

// Recibe la función de manejo de cambio y los datos actuales del padre
export default function CargaArchivos({ handleChange, formData }) {

    // Función para manejar inputs de tipo file
    const handleFileChange = (e) => {
        const { name, files } = e.target;
        handleChange(name, files ? files[0] : null);
    };

    return (
        <div className="area-carga-archivos">
            <div className="icon-placeholder">
                 {/* Icono más representativo */}
                <Video width="48" height="48" color="#a0a0a0" />
            </div>
            <h2>SUBA EL CONTENIDO DEL RESPECTIVO PROGRAMA Y EL INFORME DEL MISMO</h2>
            
            <div className="input-group-carga">
                 {/* Input de PROGRAMA */}
                 <label htmlFor="archivo-programa" className="btn-archivo">
                    {formData.archivo ? formData.archivo.name : 'Subir Archivo de Programa (.mp4, .mov, etc.)'}
                 </label>
                 <input 
                    id="archivo-programa"
                    name="archivo"
                    type="file"
                    onChange={handleFileChange} // Usamos la función local de archivos
                    accept="video/*" // Mejorar UX: Sugerir tipo de archivo
                    required
                 />
                 
                 {/* Input de INFORME */}
                 <label htmlFor="archivo-informe" className="btn-archivo">
                    {formData.informe ? formData.informe.name : 'Subir Informe (.pdf, .doc)'}
                 </label>
                 <input 
                    id="archivo-informe"
                    name="informe"
                    type="file"
                    onChange={handleFileChange}
                    accept=".pdf,.doc,.docx" // Mejorar UX: Sugerir tipo de archivo
                    required
                 />
            </div>
        </div>
    );
}