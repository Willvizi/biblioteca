package com.example.biblioteca.controller.livro;


import com.example.biblioteca.assembler.LivroDTOAssembler;
import com.example.biblioteca.assembler.LivroResponseDTOAssembler;
import com.example.biblioteca.dto.LivroResponseDTO;
import com.example.biblioteca.input.LivroInput;
import com.example.biblioteca.service.livro.CriaLivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Livros")
@RestController
@RequestMapping("/livro")
@RequiredArgsConstructor
public class CriaLivroController {
    private final LivroDTOAssembler livroDTOAssembler;
    private final CriaLivroService criaLivroService;
    private final LivroResponseDTOAssembler livroResponseDTOAssembler;

    @Operation(summary = "Cadastra um novo livro")
    @PostMapping
    public ResponseEntity<LivroResponseDTO> criaLivro(@RequestBody @Valid LivroInput livroInput) {
        return ResponseEntity.ok(livroResponseDTOAssembler
                .toDTO(criaLivroService
                        .criarLivro(livroDTOAssembler
                                .inputToDTO(livroInput))));
    }
}