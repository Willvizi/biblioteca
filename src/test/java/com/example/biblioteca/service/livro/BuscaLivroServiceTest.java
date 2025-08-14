package com.example.biblioteca.service.livro;

import com.example.biblioteca.assembler.LivroAssembler;
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

    @Mock
    private LivroAssembler livroAssembler;

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

    @Test
    void deveRetornarListaDeLivrosQuandoExistiremLivros() {
        Livro livro1 = Livro.builder()
                .id(1L)
                .titulo("Título do Livro 1")
                .autor("Autor do Livro 1")
                .isbn("123-4567890123")
                .dataPublicacao(LocalDate.of(2020, 1, 1))
                .categoria("Ficção")
                .build();

        Livro livro2 = Livro.builder()
                .id(2L)
                .titulo("Título do Livro 2")
                .autor("Autor do Livro 2")
                .isbn("789-4561230789")
                .dataPublicacao(LocalDate.of(2021, 2, 2))
                .categoria("Mistério")
                .build();

        List<Livro> livros = List.of(livro1, livro2);
        List<LivroDTO> livrosDTO = livros.stream()
                .map(livro -> LivroDTO.builder().build())
                .toList();

        when(livroRepository.findAll()).thenReturn(livros);
        when(livroAssembler.toDTO(livro1)).thenReturn(LivroDTO.builder().build());
        when(livroAssembler.toDTO(livro2)).thenReturn(LivroDTO.builder().build());

        List<LivroDTO> resultado = buscaLivroService.buscarTodosLivros();

        assertEquals(livrosDTO, resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremLivros() {
        when(livroRepository.findAll()).thenReturn(List.of());

        List<LivroDTO> resultado = buscaLivroService.buscarTodosLivros();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveRetornarTrueQuandoLivroJaEstaEmprestado() {
        Long idLivroEmprestado = 1L;

        when(livroRepository.livroJaEstaEmprestado(idLivroEmprestado)).thenReturn(true);

        boolean resultado = buscaLivroService.livroJaEmprestado(idLivroEmprestado);

        assertTrue(resultado);
    }

    @Test
    void deveRetornarFalseQuandoLivroNaoEstaEmprestado() {
        Long idLivroNaoEmprestado = 2L;

        when(livroRepository.livroJaEstaEmprestado(idLivroNaoEmprestado)).thenReturn(false);

        boolean resultado = buscaLivroService.livroJaEmprestado(idLivroNaoEmprestado);

        assertFalse(resultado);
    }
}