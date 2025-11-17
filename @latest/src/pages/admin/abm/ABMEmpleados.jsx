import React, { useState, useEffect } from 'react'; // <-- 1. IMPORTAMOS useEffect
import axios from 'axios'; // <-- 2. IMPORTAMOS AXIOS

// --- Importaciones Corregidas (Sin cambios) ---
import ABMPageLayout from '../../../components/abm/ABMPageLayout.jsx'; 
import ABMEmpleadosAddForm from './ABMEmpleadosAddForm.jsx';
import ABMEmpleadosEditForm from './ABMEmpleadosEditForm.jsx';
import '../../../styles/components/abmForm.css';

// --- Datos de Ejemplo (¡YA NO SE USAN!) ---
// const empleadosData = [ ... ]; // (Borramos o comentamos esto)

// --- 3. COLUMNAS (MODIFICADAS) ---
// (Ajustamos las 'key' para que coincidan con tu DTO 'EmpleadoPermisosDTO')
const columnasEmpleados = [
  { key: 'id', header: 'ID' },
  { key: 'nombre', header: 'Empleado' }, // 'empleado' -> 'nombre'
  { key: 'email', header: 'Correo' },  // 'correo' -> 'email'
  { key: 'nombreRol', header: 'Cargo' }, // 'cargo' -> 'nombreRol'
  { 
        key: 'permisos', 
        header: 'Permisos',
        // Tu DTO devuelve una lista de strings,
        // la unimos con ".join()" para mostrarla
        render: (rowData) => rowData.permisos.join(', ') 
    },
];

export default function ABMEmpleados() {
  const [editingId, setEditingId] = useState(null); 
    
    // --- 4. ESTADOS NUEVOS ---
    // (Para guardar los datos de la API)
    const [empleados, setEmpleados] = useState([]); // Inicia como lista vacía
    const [loading, setLoading] = useState(true); // Para el mensaje de "Cargando..."

    // --- 5. USEEFFECT (LA LLAMADA A LA API) ---
    // (Esto se ejecuta 1 sola vez cuando la página carga)
    useEffect(() => {
        // Esta es la URL de tu UsuarioController (@GetMapping)
        const API_URL = 'http://localhost:8080/api/usuarios';

        console.log("Buscando empleados en la API...");

        axios.get(API_URL)
            .then(response => {
                // ¡ÉXITO! response.data es la List<EmpleadoPermisosDTO>
                console.log("Datos recibidos:", response.data);
                setEmpleados(response.data); // Guardamos los datos en el estado
                setLoading(false); // Dejamos de cargar
            })
            .catch(error => {
                // ¡ERROR!
                console.error('¡Error al cargar empleados!', error);
                // (Esto puede ser un error de CORS si te olvidaste
                // el @CrossOrigin en el Controller)
                setLoading(false);
            });
    }, []); // El [] vacío hace que solo se ejecute al inicio

  const handleAdd = () => {
    setEditingId(0); 
  };

  const handleEdit = (id) => {
    setEditingId(id);
  };

  const handleCancelOrSuccess = () => {
        // (Opcional: Volver a cargar los datos después de agregar/editar)
        // axios.get(API_URL).then(res => setEmpleados(res.data));
    setEditingId(null); 
  };
  
  // --- Renderizado Condicional (Modificado) ---

  // Estado 1: Creando (Sin cambios)
  if (editingId === 0) {
    return (
      <ABMEmpleadosAddForm 
        onCancel={handleCancelOrSuccess} 
        onSuccess={handleCancelOrSuccess}
      />
    );
  }

  // Estado 2: Editando (Modificado)
  if (editingId !== null) {
    return (
      <ABMEmpleadosEditForm 
        empleadoId={editingId} 
        onCancel={handleCancelOrSuccess} 
        onSuccess={handleCancelOrSuccess}
                // 6. Buscamos los datos iniciales en la lista de la API
        initialData={empleados.find(e => e.id === editingId)}
      />
    );
  }
  
  // Estado 3: Mostrando la lista (Modificado)
  return (
    <ABMPageLayout
      title="ABM de Empleados"
      columns={columnasEmpleados}
            // 7. Usamos 'loading' y los datos de la API
      data={empleados}
            isLoading={loading} // (Le pasamos el estado de carga al layout)
      onAdd={handleAdd} 
      onEdit={handleEdit} 
      onDelete={() => { console.log("La eliminación se maneja desde el formulario de edición."); }}
    />
  );
}