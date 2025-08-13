package com.example.biblioteca.service.livro;

import com.example.biblioteca.assembler.LivroAssembler;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CriaLivroService {

    private final LivroRepository livroRepository;
    private final LivroAssembler livroAssembler;

    @Transactional
    public LivroDTO criarLivro(LivroDTO livroDTO) {
        Livro livro = livroAssembler.toEntity(livroDTO);

        Livro livroSalvo = livroRepository.save(livro);

        return livroAssembler.toDTO(livroSalvo);
    }
}