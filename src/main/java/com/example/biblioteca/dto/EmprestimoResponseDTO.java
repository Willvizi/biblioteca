package com.example.biblioteca.dto;

import com.example.biblioteca.domain.Emprestimo.StatusEmprestimo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EmprestimoResponseDTO {
    private Long id;
    private UsuarioDTO usuario;
    private LivroDTO livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private StatusEmprestimo status;
}
