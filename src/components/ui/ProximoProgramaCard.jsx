import React from 'react';
import { Clock } from 'lucide-react';

export default function ProximoProgramaCard({ programa }) {
    return (
        <div className="proximo-card">
            <p className="card-horario">
                {programa.hora}
            </p>
            <p className="card-titulo">{programa.titulo}</p>
            <p className="card-descripcion">{programa.descripcion}</p>
        </div>
    );
}