package com.example.biblioteca.service.livro;


import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.repository.LivroRepository;
import com.example.biblioteca.service.emprestimo.DeletaEmprestimoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletaLivroServiceTest {

    @InjectMocks
    private DeletaLivroService deletaLivroService;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private BuscaLivroService buscaLivroService;

    @Mock
    private DeletaEmprestimoService deletaEmprestimoService;

    @Test
    void deveDeletarLivroQuandoExiste() {
        Long livroId = 1L;
        Livro livro = new Livro();
        livro.setId(livroId);

        when(buscaLivroService.buscarLivroPorId(livroId)).thenReturn(livro);
        doNothing().when(deletaEmprestimoService).deletarPorLivro(livro);
        doNothing().when(livroRepository).delete(livro);

        assertThatNoException().isThrownBy(() -> deletaLivroService.deletarLivro(livroId));

        verify(buscaLivroService, times(1)).buscarLivroPorId(livroId);
        verify(deletaEmprestimoService, times(1)).deletarPorLivro(livro);
        verify(livroRepository, times(1)).delete(livro);
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoExiste() {
        Long livroId = 1L;

        when(buscaLivroService.buscarLivroPorId(livroId)).thenThrow(new RuntimeException("Livro não encontrado"));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> deletaLivroService.deletarLivro(livroId));

        verify(buscaLivroService, times(1)).buscarLivroPorId(livroId);
        verify(deletaEmprestimoService, never()).deletarPorLivro(any());
        verify(livroRepository, never()).delete(any());
    }
}