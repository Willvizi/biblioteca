package com.example.biblioteca.controller.emprestimo;

import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.service.emprestimo.BuscaEmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimo")
@RequiredArgsConstructor
public class BuscaEmprestimoController {

    private final BuscaEmprestimoService buscaEmprestimoService;

    @GetMapping
    public ResponseEntity<List<EmprestimoDTO>> listarEmprestimos() {
        List<EmprestimoDTO> emprestimos = buscaEmprestimoService.listarTodos();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> buscarEmprestimoPorId(
            @PathVariable Long id) {
        
        EmprestimoDTO emprestimoDTO = buscaEmprestimoService.buscarPorId(id);
        return ResponseEntity.ok(emprestimoDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoDTO>> buscarEmprestimosPorUsuario(
            @PathVariable Long usuarioId) {
        
        List<EmprestimoDTO> emprestimos = buscaEmprestimoService.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(emprestimos);
    }
}
