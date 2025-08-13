package com.example.biblioteca.input;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmprestimoInput {
    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;
    
    @NotNull(message = "O ID do livro é obrigatório")
    private Long livroId;
    
    @NotNull(message = "A data de devolução é obrigatória")
    @FutureOrPresent(message = "A data de devolução deve ser no presente ou no futuro")
    private LocalDate dataDevolucao;
}
