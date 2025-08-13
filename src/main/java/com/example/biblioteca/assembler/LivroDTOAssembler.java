package com.example.biblioteca.assembler;

import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.input.LivroInput;
import org.springframework.stereotype.Component;

@Component
public class LivroDTOAssembler {

    public LivroDTO inputToDTO(LivroInput livroInput) {
        return LivroDTO.builder()
                .titulo(livroInput.getTitulo())
                .autor(livroInput.getAutor())
                .isbn(livroInput.getIsbn())
                .dataPublicacao(livroInput.getDataPublicacao())
                .categoria(livroInput.getCategoria())
                .build();
    }

    public LivroDTO entityToDTO(Livro livro) {
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
