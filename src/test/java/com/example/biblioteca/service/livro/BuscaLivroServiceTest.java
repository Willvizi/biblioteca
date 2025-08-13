package com.example.biblioteca.service.livro;

import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscaLivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private BuscaLivroService buscaLivroService;

    @Test
    void deveRetornarLivroQuandoIdExiste() {
        Livro livro = Livro.builder()
                .id(1L)
                .titulo("Título do Livro")
                .autor("Autor do Livro")
                .isbn("123-4567890123")
                .dataPublicacao(LocalDate.of(2020, 1, 1))
                .categoria("Ficção")
                .build();

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        Livro resultado = buscaLivroService.buscarLivroPorId(1L);

        assertEquals(livro, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExiste() {
        Long idInexistente = 99L;

        when(livroRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(BibliotecaBusinessException.class, () -> buscaLivroService.buscarLivroPorId(idInexistente));
    }
}