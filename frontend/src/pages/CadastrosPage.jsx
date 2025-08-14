import { useState, useEffect } from 'react'
import { fetchUsuarios, fetchLivros } from './apiService'

function CadastrosPage() {
  const [usuarioForm, setUsuarioForm] = useState({
    nome: '',
    email: '',
    telefone: ''
  })

  const [livroForm, setLivroForm] = useState({
    titulo: '',
    autor: '',
    isbn: '',
    dataPublicacao: '',
    categoria: ''
  })

  const [emprestimoForm, setEmprestimoForm] = useState({
    usuarioId: '',
    livroId: '',
    dataEmprestimo: '',
    dataDevolucao: ''
  })

  const [usuarios, setUsuarios] = useState([])
  const [livros, setLivros] = useState([])

  const [message, setMessage] = useState('')
  const [messageType, setMessageType] = useState('')

  useEffect(() => {
    async function carregarDados() {
      try {
        const usuariosData = await fetchUsuarios()
        setUsuarios(usuariosData)
      } catch {}
      try {
        const livrosData = await fetchLivros()
        setLivros(livrosData)
      } catch {}
    }
    carregarDados()
  }, [])

  const showMessage = (msg, type) => {
    setMessage(msg)
    setMessageType(type)
    setTimeout(() => {
      setMessage('')
      setMessageType('')
    }, 3000)
  }

  const handleUsuarioSubmit = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch('http://localhost:8080/api/usuario', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(usuarioForm)
      })

      if (response.ok) {
        showMessage('Usuário cadastrado com sucesso!', 'success')
        setUsuarioForm({ nome: '', email: '', telefone: '' })
      } else {
        try {
          const data = await response.json()
          if (data && data.erro) {
            showMessage(data.erro, 'error')
          } else {
            showMessage('Erro ao cadastrar usuário', 'error')
          }
        } catch {
          showMessage('Erro ao cadastrar usuário', 'error')
        }
      }
    } catch (error) {
      showMessage('Erro de conexão', 'error')
    }
  }

  const handleLivroSubmit = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch('http://localhost:8080/api/livro', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(livroForm)
      })

      if (response.ok) {
        showMessage('Livro cadastrado com sucesso!', 'success')
        setLivroForm({ titulo: '', autor: '', isbn: '', dataPublicacao: '', categoria: '' })
      } else {
        showMessage('Erro ao cadastrar livro', 'error')
      }
    } catch (error) {
      showMessage('Erro de conexão', 'error')
    }
  }

  const handleEmprestimoSubmit = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch('http://localhost:8080/api/emprestimo', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(emprestimoForm)
      })

      if (response.ok) {
        showMessage('Empréstimo registrado com sucesso!', 'success')
        setEmprestimoForm({ usuarioId: '', livroId: '', dataEmprestimo: '', dataDevolucao: '' })
      } else {
        try {
          const data = await response.json()
          if (data && data.erro) {
            showMessage(data.erro, 'error')
          } else {
            showMessage('Erro ao registrar empréstimo', 'error')
          }
        } catch {
          showMessage('Erro ao registrar empréstimo', 'error')
        }
      }
    } catch (error) {
      showMessage('Erro de conexão', 'error')
    }
  }

  return (
    <div>
      <h1>Cadastros</h1>
      {message && (
        <div className={`message ${messageType}`}>
          {message}
        </div>
      )}
      <div className="form-container">
        <h2>Cadastro de Usuário</h2>
        <form onSubmit={handleUsuarioSubmit}>
          <div className="form-group">
            <label>Nome:</label>
            <input
              type="text"
              value={usuarioForm.nome}
              onChange={(e) => setUsuarioForm({...usuarioForm, nome: e.target.value})}
              required
            />
          </div>
          <div className="form-group">
            <label>Email:</label>
            <input
              type="email"
              value={usuarioForm.email}
              onChange={(e) => setUsuarioForm({...usuarioForm, email: e.target.value})}
              required
            />
          </div>
          <div className="form-group">
            <label>Telefone:</label>
            <input
              type="text"
              value={usuarioForm.telefone}
              onChange={(e) => setUsuarioForm({...usuarioForm, telefone: e.target.value})}
              required
            />
          </div>
          <button type="submit" className="btn">Cadastrar Usuário</button>
        </form>
      </div>
      <div className="form-container">
        <h2>Cadastro de Livro</h2>
        <form onSubmit={handleLivroSubmit}>
          <div className="form-group">
            <label>Título:</label>
            <input
              type="text"
              value={livroForm.titulo}
              onChange={(e) => setLivroForm({...livroForm, titulo: e.target.value})}
              required
            />
          </div>
          <div className="form-group">
            <label>Autor:</label>
            <input
              type="text"
              value={livroForm.autor}
              onChange={(e) => setLivroForm({...livroForm, autor: e.target.value})}
              required
            />
          </div>
          <div className="form-group">
            <label>ISBN:</label>
            <input
              type="text"
              value={livroForm.isbn}
              onChange={(e) => setLivroForm({...livroForm, isbn: e.target.value})}
              required
            />
          </div>
          <div className="form-group">
            <label>Data de Publicação:</label>
            <input
              type="date"
              value={livroForm.dataPublicacao}
              onChange={(e) => setLivroForm({...livroForm, dataPublicacao: e.target.value})}
              required
            />
          </div>
          <div className="form-group">
            <label>Categoria:</label>
            <select
              name="categoria"
              value={livroForm.categoria}
              onChange={(e) => setLivroForm({ ...livroForm, categoria: e.target.value })}
              required
            >
              <option value="">Selecione uma categoria</option>
              <option value="Drama">Drama</option>
              <option value="Education">Educação</option>
              <option value="Fiction">Ficção</option>
              <option value="Biography & Autobiography">Biografia e Autobiografia</option>
              <option value="History">História</option>
              <option value="Comics & Graphic Novels">Quadrinhos</option>
            </select>
          </div>
          <button type="submit" className="btn">Cadastrar Livro</button>
        </form>
      </div>
      <div className="form-container">
        <h2>Registro de Empréstimo</h2>
        <form onSubmit={handleEmprestimoSubmit}>
          <div className="form-group">
            <label>Usuário:</label>
            <select
              value={emprestimoForm.usuarioId}
              onChange={e => setEmprestimoForm({ ...emprestimoForm, usuarioId: e.target.value })}
              required
            >
              <option value="">Selecione um usuário</option>
              {usuarios.map(u => (
                <option key={u.id} value={u.id}>{u.nome}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>Livro:</label>
            <select
              value={emprestimoForm.livroId}
              onChange={e => setEmprestimoForm({ ...emprestimoForm, livroId: e.target.value })}
              required
            >
              <option value="">Selecione um livro</option>
              {livros.map(l => (
                <option key={l.id} value={l.id}>{l.titulo}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>Data do Empréstimo:</label>
            <input
              type="date"
              value={emprestimoForm.dataEmprestimo}
              onChange={(e) => setEmprestimoForm({...emprestimoForm, dataEmprestimo: e.target.value})}
              required
            />
          </div>
          <div className="form-group">
            <label>Data de Devolução:</label>
            <input
              type="date"
              value={emprestimoForm.dataDevolucao}
              onChange={(e) => setEmprestimoForm({...emprestimoForm, dataDevolucao: e.target.value})}
              required
            />
          </div>
          <button type="submit" className="btn">Registrar Empréstimo</button>
        </form>
      </div>
    </div>
  )
}

export default CadastrosPage

