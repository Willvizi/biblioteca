package com.example.biblioteca.input;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AtualizaEmprestimoInput {
    @FutureOrPresent(message = "A data da devolução deve ser no presente ou futuro")
    @NotNull
    private LocalDate dataDevolucao;
}
