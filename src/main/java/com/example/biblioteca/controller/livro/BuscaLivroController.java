package com.example.biblioteca.controller.livro;

import com.example.biblioteca.assembler.LivroAssembler;
import com.example.biblioteca.assembler.LivroResponseDTOAssembler;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.dto.LivroResponseDTO;
import com.example.biblioteca.service.livro.BuscaLivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Livros")
@RestController
@RequestMapping("/livro")
@RequiredArgsConstructor
public class BuscaLivroController {

    private final BuscaLivroService buscaLivroService;
    private final LivroAssembler livroAssembler;
    private final LivroResponseDTOAssembler livroResponseDTOAssembler;

    @Operation(summary = "Lista todos os livros cadastrados")
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> buscarTodosLivros() {
        List<LivroDTO> livros = buscaLivroService.buscarTodosLivros();

        return ResponseEntity.ok(livroResponseDTOAssembler.toCollectionDTO(livros));
    }

    @Operation(summary = "Busca um livro pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarLivroPorId(@PathVariable Long id) {
        Livro livro = buscaLivroService.buscarLivroPorId(id);
        LivroDTO livroDTO = livroAssembler.toDTO(livro);

        return ResponseEntity.ok(livroResponseDTOAssembler.toDTO(livroDTO));
    }
}