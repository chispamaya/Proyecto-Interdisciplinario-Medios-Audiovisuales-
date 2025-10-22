import './styles/App.css'
import {hola} from './a.jsx'
import { Routes, Route, Link } from 'react-router-dom';
import Login from './pages/Login.jsx'

function App() {

  return (  
    <>
      <Routes>
        <Route path="/" element={<Login />} />
      </Routes>
    </>
  )
}

export default App
