import { useState, useEffect } from 'react'

function ListagensPage() {
  const [usuarios, setUsuarios] = useState([])
  const [livros, setLivros] = useState([])
  const [emprestimos, setEmprestimos] = useState([])
  const [loading, setLoading] = useState({
    usuarios: false,
    livros: false,
    emprestimos: false
  })
  const [message, setMessage] = useState('')
  const [messageType, setMessageType] = useState('')

  const showMessage = (msg, type) => {
    setMessage(msg)
    setMessageType(type)
    setTimeout(() => {
      setMessage('')
      setMessageType('')
    }, 3000)
  }

  // Carregar usuários
  const carregarUsuarios = async () => {
    setLoading(prev => ({ ...prev, usuarios: true }))
    try {
      const response = await fetch('http://localhost:8080/api/usuario')
      if (response.ok) {
        const data = await response.json()
        setUsuarios(data)
      } else {
        showMessage('Erro ao carregar usuários', 'error')
      }
    } catch (error) {
      showMessage('Erro de conexão ao carregar usuários', 'error')
    } finally {
      setLoading(prev => ({ ...prev, usuarios: false }))
    }
  }

  // Deletar usuário
  const deletarUsuario = async (id) => {
    if (!window.confirm('Tem certeza que deseja deletar este usuário?')) {
      return
    }

    try {
      const response = await fetch(`http://localhost:8080/api/usuario/${id}`, {
        method: 'DELETE'
      })

      if (response.ok) {
        showMessage('Usuário deletado com sucesso!', 'success')
        carregarUsuarios() 
      } else {
        showMessage('Erro ao deletar usuário', 'error')
      }
    } catch (error) {
      showMessage('Erro de conexão ao deletar usuário', 'error')
    }
  }

  // Carregar livros
  const carregarLivros = async () => {
    setLoading(prev => ({ ...prev, livros: true }))
    try {
      const response = await fetch('http://localhost:8080/api/livro')
      if (response.ok) {
        const data = await response.json()
        setLivros(data)
      } else {
        showMessage('Erro ao carregar livros', 'error')
      }
    } catch (error) {
      showMessage('Erro de conexão ao carregar livros', 'error')
    } finally {
      setLoading(prev => ({ ...prev, livros: false }))
    }
  }

  // Carregar empréstimos
  const carregarEmprestimos = async () => {
    setLoading(prev => ({ ...prev, emprestimos: true }))
    try {
      const response = await fetch('http://localhost:8080/api/emprestimo')
      if (response.ok) {
        const data = await response.json()
        setEmprestimos(data)
      } else {
        showMessage('Erro ao carregar empréstimos', 'error')
      }
    } catch (error) {
      showMessage('Erro de conexão ao carregar empréstimos', 'error')
    } finally {
      setLoading(prev => ({ ...prev, emprestimos: false }))
    }
  }

  // Carregar dados ao montar o componente
  useEffect(() => {
    carregarUsuarios()
    carregarLivros()
    carregarEmprestimos()
  }, [])

  return (
    <div>
      <h1>Listagens</h1>
      
      {message && (
        <div className={`message ${messageType}`}>
          {message}
        </div>
      )}

      {/* Listagem de Usuários */}
      <div className="table-container">
        <h2>Lista de Usuários</h2>
        <button onClick={carregarUsuarios} className="btn" disabled={loading.usuarios}>
          {loading.usuarios ? 'Carregando...' : 'Atualizar Lista'}
        </button>
        
        {loading.usuarios ? (
          <div className="loading">Carregando usuários...</div>
        ) : usuarios.length === 0 ? (
          <div className="empty-state">Nenhum usuário encontrado</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Telefone</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {usuarios.map((usuario) => (
                <tr key={usuario.id}>
                  <td>{usuario.id}</td>
                  <td>{usuario.nome}</td>
                  <td>{usuario.email}</td>
                  <td>{usuario.telefone}</td>
                  <td>
                    <button 
                      onClick={() => deletarUsuario(usuario.id)}
                      className="btn-danger"
                    >
                      Deletar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Listagem de Livros */}
      <div className="table-container">
        <h2>Lista de Livros</h2>
        <button onClick={carregarLivros} className="btn" disabled={loading.livros}>
          {loading.livros ? 'Carregando...' : 'Atualizar Lista'}
        </button>
        
        {loading.livros ? (
          <div className="loading">Carregando livros...</div>
        ) : livros.length === 0 ? (
          <div className="empty-state">Nenhum livro encontrado</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Autor</th>
                <th>ISBN</th>
                <th>Data de Publicação</th>
                <th>Categoria</th>
              </tr>
            </thead>
            <tbody>
              {livros.map((livro) => (
                <tr key={livro.id}>
                  <td>{livro.id}</td>
                  <td>{livro.titulo}</td>
                  <td>{livro.autor}</td>
                  <td>{livro.isbn}</td>
                  <td>{livro.dataPublicacao}</td>
                  <td>{livro.categoria}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Listagem de Empréstimos */}
      <div className="table-container">
        <h2>Lista de Empréstimos</h2>
        <button onClick={carregarEmprestimos} className="btn" disabled={loading.emprestimos}>
          {loading.emprestimos ? 'Carregando...' : 'Atualizar Lista'}
        </button>
        
        {loading.emprestimos ? (
          <div className="loading">Carregando empréstimos...</div>
        ) : emprestimos.length === 0 ? (
          <div className="empty-state">Nenhum empréstimo encontrado</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Usuário</th>
                <th>Livro</th>
                <th>Data Empréstimo</th>
                <th>Data Devolução</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {emprestimos.map((emprestimo) => (
                <tr key={emprestimo.id}>
                  <td>{emprestimo.id}</td>
                  <td>{emprestimo.usuario.nome}</td>
                  <td>{emprestimo.livro.titulo}</td>
                  <td>{
                    emprestimo.dataEmprestimo
                      ? new Date(emprestimo.dataEmprestimo).toLocaleDateString('pt-BR')
                      : ''
                  }</td>
                  <td>{
                    emprestimo.dataDevolucao
                      ? new Date(emprestimo.dataDevolucao).toLocaleDateString('pt-BR')
                      : ''
                  }</td>
                  <td>{emprestimo.status || 'Ativo'}</td>
                  <td>
                    <button
                      className="btn-small"
                      onClick={async () => {
                        try {
                          const response = await fetch(`http://localhost:8080/api/emprestimo/${emprestimo.id}/devolucao`, { method: 'PUT' })
                          if (response.ok) {
                            showMessage('Empréstimo devolvido com sucesso!', 'success')
                            carregarEmprestimos()
                          } else {
                            showMessage('Erro ao devolver empréstimo', 'error')
                          }
                        } catch {
                          showMessage('Erro de conexão ao devolver empréstimo', 'error')
                        }
                      }}
                    >
                      Devolver
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  )
}

export default ListagensPage

