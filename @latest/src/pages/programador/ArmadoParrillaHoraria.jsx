import React, { useState, useEffect } from 'react';
import axios from 'axios'; // Importamos Axios
import {
  format, addMonths, subMonths, startOfMonth, endOfMonth,
  startOfWeek, endOfWeek, eachDayOfInterval, isSameMonth
} from 'date-fns';
import { es } from 'date-fns/locale';
import '../../styles/pages/armadoParrilla.css'; 

// --- Importaciones de DND-Kit ---
import {
  DndContext,
  DragOverlay,
  useDraggable,
  useDroppable,
} from '@dnd-kit/core';

import { X } from 'lucide-react';

const diasSemana = ["DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB"];

// --- 2. Componente "Programa Arrastrable" ---
function DraggablePrograma({ programa }) {
  const { attributes, listeners, setNodeRef } = useDraggable({
    id: `prog-source-${programa.id}`, // ID único para el drag source
    data: { ...programa, type: 'source' }, 
  });

  return (
    <div
      ref={setNodeRef}
      {...listeners}
      {...attributes}
      className="programa-chip"
    >
      {programa.titulo || programa.nombre} {/* Soporte para 'titulo' (DTO) o 'nombre' */}
    </div>
  );
}

// --- 3. Componente "Celda del Día" (Receptora) ---
function DroppableDia({ day, isOtherMonth, children, onOpenModal }) {
  const { setNodeRef, isOver } = useDroppable({
    id: format(day, 'yyyy-MM-dd'), 
  });

  const style = isOver ? { backgroundColor: '#415a77', transform: 'scale(1.02)' } : {};

  return (
    <div
      ref={setNodeRef}
      style={style}
      className={`dia-cell ${isOtherMonth ? 'dia-otro-mes' : ''}`}
      onMouseUp={onOpenModal} // Mantenemos tu lógica original del modal
    >
      <span className="dia-numero">{format(day, 'dd')}</span>
      <div className="programas-asignados-lista">
        {children}
      </div>
    </div>
  );
}

// --- Componente del Modal ---
const DayDetailModal = ({ dayId, programs, onClose }) => {
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close-btn" onClick={onClose}>
          <X size={24} />
        </button>
        <h3>Programas para el {dayId}</h3>
        <div className="modal-program-list">
          {programs.length > 0 ? (
            programs.map((prog, index) => (
              <div key={`${prog.id}-${index}`} className="modal-program-item">
                {prog.nombre}
              </div>
            ))
          ) : (
            <p>No hay programas asignados para este día.</p>
          )}
        </div>
      </div>
    </div>
  );
};

// --- Componente Principal de la Página ---
export default function ArmadoParrilla() {
  const [currentMonth, setCurrentMonth] = useState(new Date());
  
  // Estados de Datos Reales
  const [programasDisponibles, setProgramasDisponibles] = useState([]); // Lista lateral
  const [asignaciones, setAsignaciones] = useState({}); // Mapa: "YYYY-MM-DD" -> [Array de programas]
  
  const [activeDragItem, setActiveDragItem] = useState(null);
  const [modalDay, setModalDay] = useState(null); 

  // --- 1. Cargar Datos Iniciales (Programas y Parrilla) ---
  useEffect(() => {
    cargarDatos();
  }, [currentMonth]); // Recargar si cambia el mes (opcional, por si filtras por mes en backend)

  const cargarDatos = async () => {
    try {
      // A. Traer lista de programas (para la barra lateral)
      // Ajusta el endpoint si tu controller de programas tiene otra ruta
      const resProgramas = await axios.get('http://localhost:8080/api/programas/todos'); 
      setProgramasDisponibles(resProgramas.data);

      // B. Traer parrilla completa (asignaciones)
      const resParrilla = await axios.get('http://localhost:8080/api/dias/todos');
      
      // C. Transformar la lista plana del backend al formato del frontend
      // Backend: [{ id: 1, dia: "2025-11-18", idPrograma: 5 }, ...]
      // Frontend espera: { "2025-11-18": [ { id: 5, nombre: "Noticias" } ] }
      
      const mapaAsignaciones = {};
      
      resParrilla.data.forEach(item => {
        const fechaStr = item.dia; // "YYYY-MM-DD"
        
        // Buscar el nombre del programa usando el ID
        const programaEncontrado = resProgramas.data.find(p => p.id === item.idPrograma);
        
        const objetoPrograma = {
          id: item.idPrograma,
          idAsignacion: item.id, // Guardamos el ID de la tabla 'Dia' por si queremos borrar luego
          nombre: programaEncontrado ? (programaEncontrado.titulo || programaEncontrado.nombre) : "Programa Desconocido"
        };

        if (!mapaAsignaciones[fechaStr]) {
          mapaAsignaciones[fechaStr] = [];
        }
        mapaAsignaciones[fechaStr].push(objetoPrograma);
      });

      setAsignaciones(mapaAsignaciones);

    } catch (error) {
      console.error("Error cargando datos:", error);
    }
  };

  // --- Lógica del Calendario ---
  const generateCalendarDays = () => {
    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart, { locale: es });
    const endDate = endOfWeek(monthEnd, { locale: es });
    return eachDayOfInterval({ start: startDate, end: endDate });
  };

  const calendarDays = generateCalendarDays();
  const goToNextMonth = () => setCurrentMonth(addMonths(currentMonth, 1));
  const goToPrevMonth = () => setCurrentMonth(subMonths(currentMonth, 1));

  // --- Modal Handlers ---
  const handleOpenModal = (dayId) => {
    // Solo abrimos si no estamos arrastrando (para evitar conflicto con mouseUp)
    if (!activeDragItem) setModalDay(dayId);
  };
  const handleCloseModal = () => {
    setModalDay(null);
  };

  // --- Drag & Drop Handlers ---
  function handleDragStart(event) {
    setActiveDragItem(event.active.data.current);
  }

  async function handleDragEnd(event) {
    const { active, over } = event;
    setActiveDragItem(null);

    if (!over) return;

    const diaId = over.id; // "YYYY-MM-DD"
    const programa = active.data.current; // Objeto programa completo
    const usuarioId = localStorage.getItem('usuarioId');

    // Validar login
    if (!usuarioId) {
      alert("Debes iniciar sesión para modificar la parrilla.");
      return;
    }

    try {
      // 1. Enviar al Backend
      const nuevoDiaDTO = {
        dia: diaId,
        idPrograma: programa.id
      };

      // POST: Crear la asignación
      await axios.post('http://localhost:8080/api/dias', nuevoDiaDTO, {
        params: { idUsuarioAuditoria: usuarioId }
      });

      // 2. Actualizar estado local (Optimista o recargando)
      // Aquí optamos por recargar para obtener el ID real de la asignación y estar sincronizados
      cargarDatos();

    } catch (error) {
      console.error("Error al asignar programa:", error);
      alert("Error al guardar en la parrilla: " + (error.response?.data || error.message));
    }
  }

  return (
    <DndContext onDragStart={handleDragStart} onDragEnd={handleDragEnd}>
      <div className="armado-parrilla-container">

        {/* Barra de Programas */}
        <div className="programas-disponibles-bar">
          <h3>Programas disponibles</h3>
          <div className="programas-lista">
            {programasDisponibles.map(prog => (
              <DraggablePrograma key={prog.id} programa={prog} />
            ))}
            {/* Indicador simple si hay muchos */}
            <span className="programas-mas">
               {programasDisponibles.length > 0 ? "" : "Cargando..."}
            </span>
          </div>
        </div>

        {/* Contenedor del Calendario */}
        <div className="calendario-container">
          <div className="calendario-header">
            <button className="flecha" onClick={goToPrevMonth}>&lt;</button>
            <h2>
              {format(currentMonth, "MMMM yyyy", { locale: es }).toUpperCase()}
            </h2>
            <button className="flecha" onClick={goToNextMonth}>&gt;</button>
          </div>

          <div className="calendario-grid-header">
            {diasSemana.map(dia => (
              <div key={dia} className="dia-header-cell">{dia}</div>
            ))}
          </div>

          <div className="calendario-grid-body">
            {calendarDays.map(day => {
              const diaId = format(day, 'yyyy-MM-dd');
              const programasDelDia = asignaciones[diaId] || [];

              return (
                <DroppableDia
                  key={diaId}
                  day={day}
                  isOtherMonth={!isSameMonth(day, currentMonth)}
                  onOpenModal={() => handleOpenModal(diaId)}
                >
                  {programasDelDia.map((prog, idx) => (
                    <div key={`${prog.idAsignacion}-${idx}`} className="programa-chip-asignado">
                      {prog.nombre}
                    </div>
                  ))}
                </DroppableDia>
              );
            })}
          </div>
        </div>
      </div>

      {/* Modal Detalle */}
      {modalDay && (
        <DayDetailModal
          dayId={modalDay}
          programs={asignaciones[modalDay] || []}
          onClose={handleCloseModal}
        />
      )}

      {/* Drag Overlay */}
      <DragOverlay>
        {activeDragItem ? (
          <div className="programa-chip chip-en-overlay">
            {activeDragItem.titulo || activeDragItem.nombre}
          </div>
        ) : null}
      </DragOverlay>
    </DndContext>
  );
}