import './styles/App.css'
import {hola} from './a.jsx'
import { Routes, Route, Link } from 'react-router-dom';
import Login from './pages/Login.jsx'
import { Header } from './components/layout/header.jsx';
function App() {

  return (  
    <>
      <Header/>
      <Routes>
        <Route path="/" element={<Login />} />
      </Routes>
    </>
  )
}

export default App
