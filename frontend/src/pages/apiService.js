// Serviço para buscar usuários e livros
export async function fetchUsuarios() {
  const response = await fetch('http://localhost:8080/api/usuario');
  if (!response.ok) throw new Error('Erro ao buscar usuários');
  return response.json();
}

export async function fetchLivros() {
  const response = await fetch('http://localhost:8080/api/livro');
  if (!response.ok) throw new Error('Erro ao buscar livros');
  return response.json();
}
