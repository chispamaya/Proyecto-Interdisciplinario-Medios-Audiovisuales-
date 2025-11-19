import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; 
import { Edit, Trash2, RefreshCw } from 'lucide-react'; // Agregué RefreshCw para carga
import axios from 'axios'; // 🟢 Importamos Axios
import ABMPageLayout from '../../../components/abm/ABMPageLayout.jsx';
import ABMProgramasForm from './ABMProgramasForm.jsx';

// --- Función Auxiliar: Calcular Duración ---
// Convierte "10:00:00" y "11:30:00" en "90 min"
const calcularDuracionEnMinutos = (inicio, fin) => {
    
    // 1. Verificación robusta contra nulls y formatos inválidos
    if (!inicio || !fin || !Array.isArray(inicio) || !Array.isArray(fin) || inicio.length < 2 || fin.length < 2) {
        return "N/A";
    }
    
    // 2. Desestructuramos directamente el array [H, M]
    const [h1, m1] = inicio;
    const [h2, m2] = fin;
    
    // 3. Lógica de cálculo (idéntica a la anterior)
    const minutosInicio = h1 * 60 + m1;
    const minutosFin = h2 * 60 + m2;
    
    let diferencia = minutosFin - minutosInicio;
    if (diferencia < 0) diferencia += 24 * 60; // Ajuste por si cruza la medianoche
    
    return `${diferencia} min`;
};

// Función que crea la definición de columnas
const getColumnasProgramas = (onEdit, onDelete) => [
    { key: 'id', header: 'ID' },
    { key: 'nombre', header: 'Nombre' },
    { key: 'duracion', header: 'Duración' }, // Ahora es calculado
    { key: 'categoria', header: 'Categoría' },
    { key: 'estado', header: 'Estado' }, // Mapeado de estadoAprobacion
    {
        key: 'editar',
        header: 'Editar',
        className: 'abm-columna-accion',
        render: (item) => (
            <button 
                onClick={() => onEdit(item.id)} 
                className="btn-accion btn-editar"
                aria-label={`Editar ${item.id}`}
            >
                <Edit size={18} />
            </button>
        )
    },
    {
        key: 'eliminar',
        header: 'Eliminar',
        className: 'abm-columna-accion',
        render: (item) => (
            <button 
                onClick={() => onDelete(item.id)} 
                className="btn-accion btn-eliminar"
                aria-label={`Eliminar ${item.id}`}
            >
                <Trash2 size={18} />
            </button>
        )
    }
];

export default function ABMProgramas() {
    const navigate = useNavigate(); 
    
    // --- Estados ---
    const [programas, setProgramas] = useState([]); // Datos reales
    const [loading, setLoading] = useState(false);
    const [editingId, setEditingId] = useState(null); 

    // --- 1. Cargar Programas (GET) ---
    const fetchProgramas = async () => {
        setLoading(true);
        try {
            const response = await axios.get('http://localhost:8080/api/programas');
            
            // Transformamos los datos del Backend para que encajen en la tabla
            const datosFormateados = response.data.map(prog => ({
                id: prog.id,
                nombre: prog.nombre,
                categoria: prog.categoria,
                // Calculamos duración basado en horaInicio y horaFin
                duracion: calcularDuracionEnMinutos(prog.horaInicio, prog.horaFin), 
                // Mapeamos el nombre del campo (Backend: estadoAprobacion -> Frontend: estado)
                estado: prog.estadoAprobacion 
            }));

            setProgramas(datosFormateados);
        } catch (error) {
            console.error("Error cargando programas:", error);
        } finally {
            setLoading(false);
        }
    };

    // Cargar al montar el componente
    useEffect(() => {
        fetchProgramas();
    }, []);

    // --- 2. Manejadores ---

    const handleEdit = (id) => {
        setEditingId(id); 
    };

    const handleDelete = async (id) => {
        if (window.confirm(`¿Seguro que deseas eliminar el programa ID: ${id}?`)) {
            try {
                // Llamada al Backend
                await axios.delete(`http://localhost:8080/api/programas/${id}`);
                // Si sale bien, recargamos la lista
                fetchProgramas();
            } catch (error) {
                console.error("Error al eliminar:", error);
                alert("No se pudo eliminar el programa.");
            }
        }
    };
    
    const handleAdd = () => {
        navigate('/subida'); 
    };

    // Se llama cuando el formulario termina (ya sea Guardar o Cancelar)
    const handleCancelOrSuccess = () => {
        setEditingId(null);
        fetchProgramas(); // Recargamos la lista para ver los cambios editados
    };
    
    // --- Renderizado del Formulario de Edición ---
    if (editingId !== null) {
        return (
            <ABMProgramasForm 
                programId={editingId} 
                onCancel={handleCancelOrSuccess} 
                onSuccess={handleCancelOrSuccess}
            />
        );
    }
    
    // Genera las columnas llamando a la función
    const columnas = getColumnasProgramas(handleEdit, handleDelete);
    
    return (
        <ABMPageLayout
            title="ABM de Programas"
            columns={columnas}
            // Si está cargando, podrías pasar un array vacío o manejarlo en el layout
            data={programas} 
            onAdd={handleAdd} 
        />
    );
}