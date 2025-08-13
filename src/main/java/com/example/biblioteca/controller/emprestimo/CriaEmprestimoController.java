package com.example.biblioteca.controller.emprestimo;

import com.example.biblioteca.assembler.EmprestimoResponseDTOAssembler;
import com.example.biblioteca.dto.EmprestimoResponseDTO;
import com.example.biblioteca.input.EmprestimoInput;
import com.example.biblioteca.service.emprestimo.CriaEmprestimoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emprestimo")
public class CriaEmprestimoController {

    @Autowired
    private CriaEmprestimoService criaEmprestimoService;

    @Autowired
    private EmprestimoResponseDTOAssembler emprestimoResponseDTOAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmprestimoResponseDTO criar(@Valid @RequestBody EmprestimoInput input) {
        return emprestimoResponseDTOAssembler.toResponseDTO(criaEmprestimoService.criar(input));
    }
}
