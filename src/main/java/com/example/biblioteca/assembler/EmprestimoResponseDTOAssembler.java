package com.example.biblioteca.assembler;

import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.dto.EmprestimoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmprestimoResponseDTOAssembler {

    @Autowired
    private LivroDTOAssembler livroDTOAssembler;

    @Autowired
    private UsuarioDTOAssembler usuarioDTOAssembler;

    public EmprestimoResponseDTO toResponseDTO(Emprestimo emprestimo) {
        return EmprestimoResponseDTO.builder()
                .id(emprestimo.getId())
                .usuario(usuarioDTOAssembler.entityToDTO(emprestimo.getUsuario()))
                .livro(livroDTOAssembler.entityToDTO(emprestimo.getLivro()))
                .dataEmprestimo(emprestimo.getDataEmprestimo())
                .dataDevolucao(emprestimo.getDataDevolucao())
                .status(emprestimo.getStatus())
                .build();
    }
}
