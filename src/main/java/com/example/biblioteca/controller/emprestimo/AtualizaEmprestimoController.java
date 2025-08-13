package com.example.biblioteca.controller.emprestimo;

import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.input.AtualizaEmprestimoInput;
import com.example.biblioteca.service.emprestimo.AtualizaEmprestimoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emprestimo")
@RequiredArgsConstructor
public class AtualizaEmprestimoController {

    private final AtualizaEmprestimoService atualizaEmprestimoService;

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoDTO> devolverEmprestimo(
            @PathVariable Long id) {
        
        EmprestimoDTO emprestimoDTO = atualizaEmprestimoService.devolverLivro(id);
        return ResponseEntity.ok(emprestimoDTO);
    }

    @PutMapping("/{id}/ajustarData")
    public ResponseEntity<EmprestimoDTO> atualizarEmprestimoData(
            @PathVariable Long id,
            @Valid @RequestBody AtualizaEmprestimoInput input) {

        EmprestimoDTO emprestimoDTO = atualizaEmprestimoService.atualizarDataDevolucao(id, input);
        return ResponseEntity.ok(emprestimoDTO);
    }
}
