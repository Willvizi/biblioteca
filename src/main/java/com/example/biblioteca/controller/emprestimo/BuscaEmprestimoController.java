package com.example.biblioteca.controller.emprestimo;

import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.service.emprestimo.BuscaEmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Empréstimos")
@RestController
@RequestMapping("/emprestimo")
@RequiredArgsConstructor
public class BuscaEmprestimoController {

    private final BuscaEmprestimoService buscaEmprestimoService;

    @Operation(summary = "Lista todos os empréstimos")
    @GetMapping
    public ResponseEntity<List<EmprestimoDTO>> listarEmprestimos() {
        List<EmprestimoDTO> emprestimos = buscaEmprestimoService.listarTodos();
        return ResponseEntity.ok(emprestimos);
    }

    @Operation(summary = "Busca um empréstimo pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> buscarEmprestimoPorId(
            @PathVariable Long id) {
        
        EmprestimoDTO emprestimoDTO = buscaEmprestimoService.buscarPorId(id);
        return ResponseEntity.ok(emprestimoDTO);
    }

    @Operation(summary = "Lista empréstimos de um usuário específico")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoDTO>> buscarEmprestimosPorUsuario(
            @PathVariable Long usuarioId) {
        
        List<EmprestimoDTO> emprestimos = buscaEmprestimoService.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(emprestimos);
    }
}