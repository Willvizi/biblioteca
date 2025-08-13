package com.example.biblioteca.controller.livro;

import com.example.biblioteca.assembler.LivroDTOAssembler;
import com.example.biblioteca.assembler.LivroResponseDTOAssembler;
import com.example.biblioteca.dto.LivroResponseDTO;
import com.example.biblioteca.input.LivroInput;
import com.example.biblioteca.service.livro.AtualizaLivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/livro")
public class AtualizaLivroController {
    
    private final AtualizaLivroService atualizaLivroService;
    private final LivroDTOAssembler livroDTOAssembler;
    private final LivroResponseDTOAssembler livroResponseDTOAssembler;
    

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizaLivro(
            @PathVariable Long id, 
            @RequestBody @Valid LivroInput livroInput) {
        
        return ResponseEntity.ok(livroResponseDTOAssembler
                .toDTO(atualizaLivroService
                        .atualizarLivro(id, livroDTOAssembler
                                .inputToDTO(livroInput))));
    }
}
