package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.assembler.EmprestimoAssembler;
import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.input.EmprestimoInput;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.repository.LivroRepository;
import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.service.livro.BuscaLivroService;
import com.example.biblioteca.service.usuario.BuscaUsuarioService;
import com.example.biblioteca.validator.LivroEmprestadoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CriaEmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    private final BuscaUsuarioService buscaUsuarioService;

    private final BuscaLivroService buscaLivroService;

    private final EmprestimoAssembler emprestimoAssembler;

    private final LivroEmprestadoValidator livroEmprestadoValidator;



    @Transactional
    public Emprestimo criar(EmprestimoInput input) {
        Usuario usuario = buscaUsuarioService.getUsuario(input.getUsuarioId());

        Livro livro = buscaLivroService.buscarLivroPorId(input.getLivroId());
        livroEmprestadoValidator.validar(livro.getId());

        Emprestimo emprestimo = emprestimoAssembler.toEntity(input, usuario, livro);
        emprestimo = emprestimoRepository.save(emprestimo);

        return emprestimo;
    }
}
