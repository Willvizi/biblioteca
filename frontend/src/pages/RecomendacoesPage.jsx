import { useState, useEffect } from 'react'
import { fetchUsuarios } from './apiService'

function RecomendacoesPage() {
  const [recomendacoes, setRecomendacoes] = useState([])
  const [usuarioId, setUsuarioId] = useState('')
  const [usuarios, setUsuarios] = useState([])
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState('')
  const [messageType, setMessageType] = useState('')

  useEffect(() => {
    async function carregarUsuarios() {
      try {
        const usuariosData = await fetchUsuarios()
        setUsuarios(usuariosData)
      } catch {}
    }
    carregarUsuarios()
  }, [])

  const showMessage = (msg, type) => {
    setMessage(msg)
    setMessageType(type)
    setTimeout(() => {
      setMessage('')
      setMessageType('')
    }, 3000)
  }

  const buscarRecomendacoes = async () => {
    if (!usuarioId) {
      showMessage('Por favor, selecione um usuário', 'error')
      return
    }

    setLoading(true)
    try {
      const response = await fetch(`http://localhost:8080/api/livros/recomendacoes/usuario/${usuarioId}`)
      
      if (response.ok) {
        const data = await response.json()
        setRecomendacoes(data)
        showMessage('Recomendações carregadas com sucesso!', 'success')
      } else {
        showMessage('Erro ao buscar recomendações', 'error')
        setRecomendacoes([])
      }
    } catch (error) {
      showMessage('Erro de conexão', 'error')
      setRecomendacoes([])
    } finally {
      setLoading(false)
    }
  }

  const cadastrarLivroRecomendacao = async (livro) => {
    try {
      const livroData = {
        titulo: livro.titulo,
        autor: livro.autor,
        isbn: livro.isbn || '',
        dataPublicacao: livro.dataPublicacao || '',
        categoria: livro.categoria || ''
      }

      const response = await fetch('http://localhost:8080/api/livro', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(livroData)
      })

      if (response.ok) {
        showMessage(`Livro "${livro.titulo}" cadastrado com sucesso!`, 'success')
      } else {
        showMessage(`Erro ao cadastrar livro "${livro.titulo}"`, 'error')
      }
    } catch (error) {
      showMessage('Erro de conexão ao cadastrar livro', 'error')
    }
  }

  return (
    <div>
      <h1>Recomendações de Livros</h1>
      
      {message && (
        <div className={`message ${messageType}`}>
          {message}
        </div>
      )}

      <div className="form-container">
        <h2>Buscar Recomendações por Usuário</h2>
        <div className="form-group">
          <label>Usuário:</label>
          <select
            value={usuarioId}
            onChange={e => setUsuarioId(e.target.value)}
            required
          >
            <option value="">Selecione um usuário</option>
            {usuarios.map(u => (
              <option key={u.id} value={u.id}>{u.nome}</option>
            ))}
          </select>
        </div>
        <button 
          onClick={buscarRecomendacoes} 
          className="btn"
          disabled={loading}
        >
          {loading ? 'Buscando...' : 'Buscar Recomendações'}
        </button>
      </div>

      <div>
        <h2>Lista de Recomendações</h2>
        {recomendacoes.length === 0 ? (
          <p>Nenhuma recomendação encontrada. Use o campo acima para buscar recomendações por usuário.</p>
        ) : (
          <div>
            {recomendacoes.map((livro, index) => (
              <div key={index} className="recomendacao-item">
                <h3>{livro.titulo}</h3>
                <p><strong>Autor:</strong> {livro.autor}</p>
                {livro.isbn && <p><strong>ISBN:</strong> {livro.isbn}</p>}
                {livro.dataPublicacao && <p><strong>Data de Publicação:</strong> {livro.dataPublicacao}</p>}
                {livro.categoria && <p><strong>Categoria:</strong> {livro.categoria}</p>}
                <button 
                  onClick={() => cadastrarLivroRecomendacao(livro)}
                  className="btn-small"
                >
                  Cadastrar este Livro
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default RecomendacoesPage

