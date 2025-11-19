import React, { useState, useEffect } from 'react';
import {
  format, addMonths, subMonths, startOfMonth, endOfMonth,
  startOfWeek, endOfWeek, eachDayOfInterval, isSameMonth
} from 'date-fns';
import { es } from 'date-fns/locale';
import '../../styles/pages/armadoParrilla.css'; 

// --- 1. Importaciones de DND-Kit ---
import {
  DndContext,
  DragOverlay, 
  useDraggable,
  useDroppable,
} from '@dnd-kit/core';

import { X, Save } from 'lucide-react'; 

const diasSemana = ["DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB"];

// --- 2. Componente "Programa Arrastrable" ---
function DraggablePrograma({ programa }) {
  const { attributes, listeners, setNodeRef } = useDraggable({
    id: String(programa.id), 
    data: programa, 
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
      onMouseUp={onOpenModal} 
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
  
  // Estado para datos del Backend
  const [programasDisponibles, setProgramasDisponibles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [guardando, setGuardando] = useState(false); 

  const [modalDay, setModalDay] = useState(null); 

  // --- 1. CARGA INICIAL (Programas + Asignaciones Previas) ---
  useEffect(() => {
    const cargarTodo = async () => {
        try {
            setLoading(true);
            
            // A. Cargar lista de programas disponibles
            const resProgramas = await fetch('http://localhost:8080/api/programas');
            if (!resProgramas.ok) throw new Error("Error al cargar programas");
            const dataProgramas = await resProgramas.json();
            setProgramasDisponibles(dataProgramas);

            // B. Cargar asignaciones guardadas (para pintar el calendario)
            const resDias = await fetch('http://localhost:8080/api/programas/dias');
            if (resDias.ok) {
                const dataDias = await resDias.json();
                
                // Transformamos la lista plana del backend a un objeto { "fecha": [prog1, prog2] }
                const mapaAsignaciones = {};
                
                dataDias.forEach(asignacion => {
                    const fecha = asignacion.dia; // "yyyy-MM-dd"
                    // Buscamos los datos completos del programa usando su ID
                    const programaInfo = dataProgramas.find(p => p.id === asignacion.idPrograma);
                    
                    if (programaInfo) {
                        if (!mapaAsignaciones[fecha]) {
                            mapaAsignaciones[fecha] = [];
                        }
                        // Evitamos duplicados visuales
                        if (!mapaAsignaciones[fecha].find(p => p.id === programaInfo.id)) {
                            mapaAsignaciones[fecha].push(programaInfo);
                        }
                    }
                });

                setAsignaciones(mapaAsignaciones);
            }

        } catch (error) {
            console.error("Error cargando datos iniciales:", error);
        } finally {
            setLoading(false);
        }
    };
    cargarTodo();
  }, []);

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

  // --- Funciones del Modal ---
  const handleOpenModal = (dayId) => {
    setModalDay(dayId);
  };
  const handleCloseModal = () => {
    setModalDay(null);
  };

  // --- Lógica de Drag and Drop ---
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

    // Actualizamos SOLO visualmente (el usuario debe dar click a Guardar después)
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

  // --- Función Guardar Lote (El botón verde) ---
  const handleGuardarCambios = async () => {
      if (Object.keys(asignaciones).length === 0) {
          alert("No has asignado ningún programa a la parrilla aún.");
          return;
      }

      setGuardando(true);
      try {
          const promesasDeGuardado = [];

          // Recorremos cada día y cada programa asignado
          Object.entries(asignaciones).forEach(([fecha, listaProgramas]) => {
              listaProgramas.forEach(prog => {
                  // Enviamos petición POST por cada asignación
                  const promesa = fetch('http://localhost:8080/api/programas/dias', {
                      method: 'POST',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify({ 
                          dia: fecha, 
                          idPrograma: prog.id 
                      })
                  }).then(res => {
                      if (!res.ok) throw new Error(`Fallo al guardar en ${fecha}`);
                      return res;
                  });
                  
                  promesasDeGuardado.push(promesa);
              });
          });

          // Esperamos a que todo termine
          await Promise.all(promesasDeGuardado);
          alert("¡Parrilla guardada exitosamente!");

      } catch (error) {
          console.error("Error guardando parrilla:", error);
          alert("Hubo un error al guardar. Revisa la consola.");
      } finally {
          setGuardando(false);
      }
  };

  return (
    <DndContext onDragStart={handleDragStart} onDragEnd={handleDragEnd}>
      <div className="armado-parrilla-container">

        {/* Barra de Programas */}
        <div className="programas-disponibles-bar">
          <h3>Programas disponibles</h3>
          
          {loading ? (
             <p>Cargando...</p>
          ) : (
            <div className="programas-lista">
                {programasDisponibles.map(prog => (
                  <DraggablePrograma key={prog.id} programa={prog} />
                ))}
                {programasDisponibles.length === 0 && <p>No hay programas creados.</p>}
            </div>
          )}
        </div>

        {/* Contenedor del Calendario */}
        <div className="calendario-container">
          
          {/* --- CABECERA CON BOTÓN --- */}
          <div className="calendario-header" style={{
              display:'flex', 
              justifyContent:'space-between', 
              alignItems: 'center'
          }}>
            <div style={{display:'flex', gap:'10px', alignItems:'center'}}>
                <button className="flecha" onClick={goToPrevMonth}>&lt;</button>
                <h2 style={{margin:0}}>
                    {format(currentMonth, "MMMM yyyy", { locale: es }).toUpperCase()}
                </h2>
                <button className="flecha" onClick={goToNextMonth}>&gt;</button>
            </div>

            {/* BOTÓN GUARDAR (Diseño compacto) */}
            <button 
                onClick={handleGuardarCambios}
                disabled={guardando}
                style={{
                    backgroundColor: '#22c55e',
                    color: 'white',
                    border: 'none',
                    padding: '8px 16px',     
                    fontSize: '14px',        
                    borderRadius: '6px',
                    cursor: 'pointer',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '6px',
                    fontWeight: '600',
                    opacity: guardando ? 0.7 : 1,
                    height: 'fit-content',   
                    boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                }}
            >
                <Save size={18} /> 
                {guardando ? "Guardando..." : "Guardar"}
            </button>
          </div>

          <div className="calendario-grid-header">
            {diasSemana.map(dia => (
              <div key={dia} className="dia-header-cell">{dia}</div>
            ))}
          </div>

          {/* Grilla de Días */}
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

      {/* Modal */}
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
            {activeDragItem.nombre}
          </div>
        ) : null}
      </DragOverlay>
    </DndContext>
  );
}