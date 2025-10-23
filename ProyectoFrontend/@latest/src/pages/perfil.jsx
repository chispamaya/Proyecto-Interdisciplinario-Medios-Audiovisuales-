import logoImage from '../assets/logo.png'
import '../styles/pages/perfil.css'

export default function Perfil({idUsuario}) { 

    const handleSubmit = (e) => {
        e.preventDefault();
        // Aquí iría la lógica para guardar los cambios (ej: actualizar contraseña)
        console.log('Contraseña actualizada');
    }

    const handleLogout = () => {
        // Aquí iría la lógica de cerrar sesión
        console.log('Cerrar Sesión');
    }

    return (
        <>
            <div className="contenedor-principal">
                <main className="contenido-perfil">

                    <div className="logo-contenedor">
                        <img className="logo-perfil" src={logoImage} alt="Logo" />
                    </div>
                    
                    <div className="datos-perfil"> 
                        <h1>Martín Romero</h1>
                        <h2>ADMINISTRADOR</h2> 
                    </div>

                    <form className="formulario-perfil" onSubmit={handleSubmit}>
                        <div className="campo-form">
                            <label htmlFor="password">Contraseña</label>
                            <input 
                                type="password" 
                                id="password" 
                                name="pass" 
                                placeholder="Cambiar contraseña"
                            />
                        </div>


                        <div className="acciones-form-centrado">
                            <button type="button" className="btn-descartar" onClick={handleLogout}>Cerrar Sesión</button>
                            <button type="submit" className="btn-guardar">Guardar</button>
                        </div>
                    </form>

                </main>
            </div>
        </>
    )
}