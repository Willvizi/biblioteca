package com.example.biblioteca.controller.livro;

import com.example.biblioteca.dto.LivroRecomendacaoDTO;
import com.example.biblioteca.service.google.RecomendacaoLivroService;
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
@RequestMapping("/livros/recomendacoes")
@RequiredArgsConstructor
public class RecomendacaoLivroController {

    private final RecomendacaoLivroService recomendacaoLivroService;

    @Operation(summary = "Busca recomendações de livros para um usuário")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<LivroRecomendacaoDTO>> buscarRecomendacoesPorUsuario(
            @PathVariable Long idUsuario) {
        
        List<LivroRecomendacaoDTO> recomendacoes = recomendacaoLivroService.buscarRecomendacoesPorCategoriaMaisEmprestada(idUsuario);
        return ResponseEntity.ok(recomendacoes);
    }
}