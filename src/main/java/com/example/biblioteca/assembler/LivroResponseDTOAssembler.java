package com.example.biblioteca.assembler;

import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.dto.LivroResponseDTO;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LivroResponseDTOAssembler {

    public LivroResponseDTO toDTO(LivroDTO livroDTO) {
        if (livroDTO == null) {
            return null;
        }

        return LivroResponseDTO.builder()
                .id(livroDTO.getId())
                .titulo(livroDTO.getTitulo())
                .autor(livroDTO.getAutor())
                .isbn(livroDTO.getIsbn())
                .dataPublicacao(livroDTO.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .categoria(livroDTO.getCategoria())
                .build();
    }

    public List<LivroResponseDTO> toCollectionDTO(List<LivroDTO> livrosDTO) {
        return livrosDTO.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}
