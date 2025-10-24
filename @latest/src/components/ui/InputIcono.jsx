// src/components/form/InputIcono.jsx
import React from 'react';

export default function InputIcono({ icon: Icon, ...props }) {
    
    return (
        <div className="input-detalle"> 
            {Icon && <Icon size={20} />} 
            
            <input 
                {...props} 
                required // Assuming all these fields are required
            />
        </div>
    );
}