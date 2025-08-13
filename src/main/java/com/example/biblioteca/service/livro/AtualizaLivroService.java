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
public class AtualizaLivroService {

    private final LivroRepository livroRepository;
    private final BuscaLivroService buscaLivroService;
    private final LivroAssembler livroAssembler;

    @Transactional
    public LivroDTO atualizarLivro(Long id, LivroDTO livroDTO) {
        buscaLivroService.buscarLivroPorId(id);

        livroDTO.setId(id);

        Livro livroAtualizado = livroAssembler.toEntity(livroDTO);
        livroAtualizado = livroRepository.save(livroAtualizado);

        return livroAssembler.toDTO(livroAtualizado);
    }
}