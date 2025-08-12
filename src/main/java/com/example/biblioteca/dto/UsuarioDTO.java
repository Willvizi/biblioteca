package com.example.biblioteca.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataCadastro;
}
