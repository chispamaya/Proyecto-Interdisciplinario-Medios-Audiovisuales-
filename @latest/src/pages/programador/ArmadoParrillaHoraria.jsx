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

// --- AÑADIDO ---
import { X } from 'lucide-react'; // Importamos el ícono para cerrar el modal

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
// (Sin cambios)
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
// --- MODIFICADO ---
// Ahora recibe 'onOpenModal'
function DroppableDia({ day, isOtherMonth, children, onOpenModal }) {
  const { setNodeRef, isOver } = useDroppable({
    id: format(day, 'yyyy-MM-dd'), // Usamos la fecha como ID
  });

  const style = isOver ? { backgroundColor: '#415a77', transform: 'scale(1.02)' } : {};

  return (
    <div
      ref={setNodeRef}
      style={style}
      className={`dia-cell ${isOtherMonth ? 'dia-otro-mes' : ''}`}
      onMouseUp={onOpenModal}
    >
      <span className="dia-numero">{format(day, 'dd')}</span>
      <div className="programas-asignados-lista">
        {children}
      </div>
    </div>
  );
}

// --- AÑADIDO: Componente del Modal ---
const DayDetailModal = ({ dayId, programs, onClose }) => {
  return (
    // El fondo oscuro
    <div className="modal-overlay" onClick={onClose}>
      {/* El contenido del modal */}
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close-btn" onClick={onClose}>
          <X size={24} />
        </button>
        <h3>Programas para el {dayId}</h3>
        <div className="modal-program-list">
          {programs.length > 0 ? (
            programs.map(prog => (
              <div key={prog.id} className="modal-program-item">
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
  const [asignaciones, setAsignaciones] = useState({});
  const [activeDragItem, setActiveDragItem] = useState(null);

  // --- AÑADIDO: Estado para el modal ---
  const [modalDay, setModalDay] = useState(null); // null = cerrado

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

  // --- AÑADIDO: Funciones para manejar el modal ---
  const handleOpenModal = (dayId) => {
    setModalDay(dayId);
  };
  const handleCloseModal = () => {
    setModalDay(null);
  };

  // --- Lógica de Drag and Drop (sin cambios) ---
  function handleDragStart(event) {
    setActiveDragItem(event.active.data.current);
  }

  function handleDragEnd(event) {
    const { active, over } = event;
    setActiveDragItem(null);

    if (!over) {
      return;
    }

    const diaId = over.id;
    const programa = active.data.current;

    setAsignaciones(prev => {
      const programasDelDia = prev[diaId] || [];

      if (programasDelDia.find(p => p.id === programa.id)) {
        return prev;
      }

      const nuevaListaDia = [...programasDelDia, programa];

      return {
        ...prev,
        [diaId]: nuevaListaDia,
      };
    });
  }

  return (
    <DndContext onDragStart={handleDragStart} onDragEnd={handleDragEnd}>
      <div className="armado-parrilla-container">

        {/* Barra de Programas (sin cambios) */}
        <div className="programas-disponibles-bar">
          <h3>Programas disponibles</h3>
          <div className="programas-lista">
            {programasDisponibles.map(prog => (
              <DraggablePrograma key={prog.id} programa={prog} />
            ))}
            <span className="programas-mas">+10 más</span>
          </div>
        </div>

        {/* Contenedor del Calendario (sin cambios) */}
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

          {/* Grilla de Días (MODIFICADO) */}
          <div className="calendario-grid-body">
            {calendarDays.map(day => {
              const diaId = format(day, 'yyyy-MM-dd');
              const programasDelDia = asignaciones[diaId] || [];

              return (
                <DroppableDia
                  key={diaId}
                  day={day}
                  isOtherMonth={!isSameMonth(day, currentMonth)}
                  // --- MODIFICADO: Pasamos la función al DroppableDia ---
                  onOpenModal={() => handleOpenModal(diaId)}
                >
                  {/* Renderizado de los chips (sin cambios) */}
                  {programasDelDia.map(prog => (
                    <div key={prog.id} className="programa-chip-asignado">
                      {prog.nombre}
                    </div>
                  ))}
                </DroppableDia>
              );
            })}
          </div>
        </div>
      </div>

      {/* --- AÑADIDO: Renderizado condicional del modal --- */}
      {modalDay && (
        <DayDetailModal
          dayId={modalDay}
          programs={asignaciones[modalDay] || []}
          onClose={handleCloseModal}
        />
      )}

      {/* Drag Overlay (sin cambios) */}
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