package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.repository.LivroRepository;
import com.example.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletaEmprestimoService {
    private final EmprestimoRepository emprestimoRepository;

    @Transactional
    public void deletarPorLivro(Livro livro) {
        var emprestimos = emprestimoRepository.findByLivro(livro);

        emprestimoRepository.deleteAll(emprestimos);
    }

    @Transactional
    public void deletarPorUsuario(Usuario usuario) {
        var emprestimos = emprestimoRepository.findByUsuario(usuario);

        emprestimoRepository.deleteAll(emprestimos);
    }
}