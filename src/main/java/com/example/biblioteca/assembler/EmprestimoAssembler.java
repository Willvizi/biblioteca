package com.example.biblioteca.assembler;

import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Emprestimo.StatusEmprestimo;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.input.EmprestimoInput;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmprestimoAssembler {

    public Emprestimo toEntity(EmprestimoInput input, Usuario usuario, Livro livro) {
        return Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(LocalDate.now())
                .dataDevolucao(input.getDataDevolucao())
                .status(StatusEmprestimo.ATIVO)
                .build();
    }

    public EmprestimoDTO toDTO(Emprestimo emprestimo) {
        return EmprestimoDTO.builder()
                .id(emprestimo.getId())
                .usuario(emprestimo.getUsuario())
                .livro(emprestimo.getLivro())
                .dataEmprestimo(emprestimo.getDataEmprestimo())
                .dataDevolucao(emprestimo.getDataDevolucao())
                .status(emprestimo.getStatus())
                .build();
    }
}
