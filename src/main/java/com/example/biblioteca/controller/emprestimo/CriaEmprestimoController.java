package com.example.biblioteca.controller.emprestimo;

import com.example.biblioteca.assembler.EmprestimoResponseDTOAssembler;
import com.example.biblioteca.dto.EmprestimoResponseDTO;
import com.example.biblioteca.input.EmprestimoInput;
import com.example.biblioteca.service.emprestimo.CriaEmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Empréstimos")
@RestController
@RequestMapping("/emprestimo")
@RequiredArgsConstructor
public class CriaEmprestimoController {

    private final CriaEmprestimoService criaEmprestimoService;

    private final EmprestimoResponseDTOAssembler emprestimoResponseDTOAssembler;

    @Operation(summary = "Cria um novo empréstimo")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmprestimoResponseDTO criar(@Valid @RequestBody EmprestimoInput input) {
        return emprestimoResponseDTOAssembler.toResponseDTO(criaEmprestimoService.criar(input));
    }
}