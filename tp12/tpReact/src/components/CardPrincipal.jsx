// src/pages/Dashboard.jsx
// ... otras importaciones
import CardPrincipal from '../components/CardPrincipal.jsx'; 
// ...

function Dashboard({ user }) {
  // ...
  return (
    <div className="dashboard-layout">
      {/* ... Navbar (que hará tu compañero) ... */}

      <div className="dashboard-main">
        {/* ... Sidebar (que hará tu compañero) ... */}
        
        <div className="dashboard-content">
          <h1>INICIO - {user.rol.toUpperCase()}</h1>
          
          {/* Aquí está el componente CardPrincipal */}
          <CardPrincipal user={user} />
          
          {/* ... resto del contenido ... */}
        </div>
      </div>
    </div>
  )
}

export default Dashboard