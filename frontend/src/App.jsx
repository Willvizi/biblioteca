import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'
import CadastrosPage from './pages/CadastrosPage'
import RecomendacoesPage from './pages/RecomendacoesPage'
import ListagensPage from './pages/ListagensPage'
import './App.css'

function App() {
  return (
    <Router>
      <div className="app">
        <nav className="navbar">
          <h1>Sistema de Biblioteca</h1>
          <div className="nav-links">
            <Link to="/">Cadastros</Link>
            <Link to="/recomendacoes">Recomendações</Link>
            <Link to="/listagens">Listagens</Link>
          </div>
        </nav>
        
        <main className="main-content">
          <Routes>
            <Route path="/" element={<CadastrosPage />} />
            <Route path="/recomendacoes" element={<RecomendacoesPage />} />
            <Route path="/listagens" element={<ListagensPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}

export default App

