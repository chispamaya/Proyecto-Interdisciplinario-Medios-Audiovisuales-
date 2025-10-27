import React, { useState } from 'react';
import {
  format, addMonths, subMonths, startOfMonth, endOfMonth,
  startOfWeek, endOfWeek, eachDayOfInterval, isSameMonth
} from 'date-fns';
import { es } from 'date-fns/locale';
import '../../styles/pages/armadoParrilla.css'; // (Usamos el mismo CSS)

// --- 1. Importaciones de DND-Kit ---
import {
  DndContext,
  DragOverlay, // Para clonar el ítem al arrastrar
  useDraggable,
  useDroppable,
} from '@dnd-kit/core';

// --- Datos de Ejemplo ---
const programasDisponibles = [
  { id: 'prog-1', nombre: 'La Parrilla' },
  { id: 'prog-2', nombre: 'Deportes Hoy' },
  { id: 'prog-3', nombre: 'Cine Clásico' },
  { id: 'prog-4', nombre: 'Música' },
  { id: 'prog-5', nombre: 'Noticias' },
  { id: 'prog-6', nombre: 'Infantiles' },
];

const diasSemana = ["DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB"];

// --- 2. Componente "Programa Arrastrable" ---
// Separamos el chip en su propio componente
function DraggablePrograma({ programa }) {
  const { attributes, listeners, setNodeRef } = useDraggable({
    id: programa.id,
    data: programa, // Pasamos el objeto completo del programa
  });

  return (
    <div
      ref={setNodeRef}
      {...listeners}
      {...attributes}
      className="programa-chip"
    >
      {programa.nombre}
    </div>
  );
}

// --- 3. Componente "Celda del Día" (Receptora) ---
// Separamos la celda en su propio componente
function DroppableDia({ day, isOtherMonth, children }) {
  const { setNodeRef, isOver } = useDroppable({
    id: format(day, 'yyyy-MM-dd'), // Usamos la fecha como ID
  });

  // Damos un estilo diferente si un programa está encima
  const style = isOver ? { backgroundColor: '#415a77', transform: 'scale(1.02)' } : {};

  return (
    <div
      ref={setNodeRef}
      style={style}
      className={`dia-cell ${isOtherMonth ? 'dia-otro-mes' : ''}`}
    >
      <span className="dia-numero">{format(day, 'dd')}</span>
      {/* Aquí mostramos los programas asignados */}
      <div className="programas-asignados-lista">
        {children}
      </div>
    </div>
  );
}

// --- Componente Principal de la Página ---
export default function ArmadoParrilla() {
  const [currentMonth, setCurrentMonth] = useState(new Date());

  // --- 4. Estado para las asignaciones y el ítem activo ---
  // Guardará { '2025-09-22': [prog-1, prog-3], '2025-09-23': [prog-2] }
  const [asignaciones, setAsignaciones] = useState({});
  const [activeDragItem, setActiveDragItem] = useState(null);

  // --- Lógica del Calendario (sin cambios) ---
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

  // --- 5. Lógica de Drag and Drop ---
  
  // Se dispara cuando empezamos a arrastrar
  function handleDragStart(event) {
    // Guardamos los datos del programa que estamos arrastrando
    setActiveDragItem(event.active.data.current);
  }

  // Se dispara cuando soltamos el ítem
  function handleDragEnd(event) {
    const { active, over } = event;
    setActiveDragItem(null); // Limpiamos el ítem activo

    // Si no lo soltamos sobre una celda válida (over), no hacemos nada
    if (!over) {
      return;
    }

    const diaId = over.id; // ej: '2025-09-22'
    const programa = active.data.current; // ej: { id: 'prog-1', nombre: 'La Parrilla' }

    // Actualizamos el estado de asignaciones
    setAsignaciones(prev => {
      // Obtenemos la lista actual de programas para ese día, o un array vacío
      const programasDelDia = prev[diaId] || [];
      
      // Evitamos duplicados
      if (programasDelDia.find(p => p.id === programa.id)) {
        return prev;
      }

      // Creamos la nueva lista para ese día
      const nuevaListaDia = [...programasDelDia, programa];

      // Devolvemos el estado actualizado
      return {
        ...prev,
        [diaId]: nuevaListaDia,
      };
    });
  }

  return (
    // --- 6. Envolvemos TODO en DndContext ---
    <DndContext onDragStart={handleDragStart} onDragEnd={handleDragEnd}>
      <div className="armado-parrilla-container">
        
        {/* 1. Lista de Programas (Ahora usa el componente Draggable) */}
        <div className="programas-disponibles-bar">
          <h3>Programas disponibles</h3>
          <div className="programas-lista">
            {programasDisponibles.map(prog => (
              <DraggablePrograma key={prog.id} programa={prog} />
            ))}
            <span className="programas-mas">+10 más</span>
          </div>
        </div>

        {/* 2. Contenedor del Calendario (sin cambios) */}
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

          {/* 3. Grilla de Días (Ahora usa el componente Droppable) */}
          <div className="calendario-grid-body">
            {calendarDays.map(day => {
              const diaId = format(day, 'yyyy-MM-dd');
              const programasDelDia = asignaciones[diaId] || []; // Obtenemos los programas de ese día

              return (
                <DroppableDia
                  key={diaId}
                  day={day}
                  isOtherMonth={!isSameMonth(day, currentMonth)}
                >
                  {/* Renderizamos los programas asignados dentro de la celda */}
                  {programasDelDia.map(prog => (
                    <div key={prog.id} className="programa-chip-asignado">
                      {prog.nombre}
                      {/* Opcional: Botón para borrarlo */}
                      {/* <button onClick={() => borrarPrograma(diaId, prog.id)}>X</button> */}
                    </div>
                  ))}
                </DroppableDia>
              );
            })}
          </div>
        </div>
      </div>

      {/* --- 7. El "Clon" que se muestra al arrastrar --- */}
      <DragOverlay>
        {activeDragItem ? (
          <div className="programa-chip chip-en-overlay">
            {activeDragItem.nombre}
          </div>
        ) : null}
      </DragOverlay>
    </DndContext>
  );
}