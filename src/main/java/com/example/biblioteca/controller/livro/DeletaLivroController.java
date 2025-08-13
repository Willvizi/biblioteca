package com.example.biblioteca.controller.livro;

import com.example.biblioteca.service.livro.DeletaLivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/livro")
@RequiredArgsConstructor
public class DeletaLivroController {
    
    private final DeletaLivroService deletaLivroService;
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaLivro(@PathVariable Long id) {
        deletaLivroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

}
