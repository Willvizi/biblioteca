package com.example.biblioteca.service.livro;

import com.example.biblioteca.assembler.LivroAssembler;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscaLivroService {

    private final LivroRepository livroRepository;
    private final LivroAssembler livroAssembler;

    @Transactional
    public Livro buscarLivroPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new BibliotecaBusinessException("Livro não encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<LivroDTO> buscarTodosLivros() {
        return livroRepository.findAll().stream()
                .map(livroAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean livroJaEmprestado(Long idLivro) {
        return livroRepository.livroJaEstaEmprestado(idLivro);
    }

}
