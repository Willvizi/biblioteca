package com.example.biblioteca.assembler;

import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import org.springframework.stereotype.Component;

@Component
public class LivroAssembler {

    public Livro toEntity(LivroDTO livroDTO) {
        return Livro.builder()
                .titulo(livroDTO.getTitulo())
                .autor(livroDTO.getAutor())
                .isbn(livroDTO.getIsbn())
                .dataPublicacao(livroDTO.getDataPublicacao())
                .categoria(livroDTO.getCategoria())
                .build();
    }

    public LivroDTO toDTO(Livro livro) {
        return LivroDTO.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .autor(livro.getAutor())
                .isbn(livro.getIsbn())
                .dataPublicacao(livro.getDataPublicacao())
                .categoria(livro.getCategoria())
                .build();
    }
}
