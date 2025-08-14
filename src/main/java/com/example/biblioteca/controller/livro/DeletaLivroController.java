package com.example.biblioteca.controller.livro;

import com.example.biblioteca.service.livro.DeletaLivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Livros")
@RestController
@RequestMapping("/livro")
@RequiredArgsConstructor
public class DeletaLivroController {
    
    private final DeletaLivroService deletaLivroService;
    
    @Operation(summary = "Deleta um livro pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaLivro(@PathVariable Long id) {
        deletaLivroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

}