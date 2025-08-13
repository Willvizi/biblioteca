package com.example.biblioteca.validator;

import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.service.livro.BuscaLivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LivroEmprestadoValidator {
    public static final String LIVRO_JA_EMPRESTADO = "Livro já emprestado!";
    private final BuscaLivroService buscaLivroService;

    public void validar(Long livroId) {
        if (buscaLivroService.livroJaEmprestado(livroId)){
            throw new BibliotecaBusinessException(LIVRO_JA_EMPRESTADO);
        }
    }

}
