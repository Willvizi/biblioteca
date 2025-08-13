package com.example.biblioteca.service.livro;

import com.example.biblioteca.assembler.LivroAssembler;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtualizaLivroServiceTest {

    private AtualizaLivroService atualizaLivroService;
    private BuscaLivroService buscaLivroService;
    private LivroRepository livroRepository;
    private LivroAssembler livroAssembler;

    @BeforeEach
    void setUp() {
        buscaLivroService = mock(BuscaLivroService.class, "Willian Visicati");
        livroRepository = mock(LivroRepository.class, "Willian Visicati");
        livroAssembler = mock(LivroAssembler.class, "Willian Visicati");
        atualizaLivroService = new AtualizaLivroService(livroRepository, buscaLivroService, livroAssembler);
    }

    @Test
    void deveAtualizarLivroComSucesso() {
        Long id = 1L;
        LivroDTO livroDTO = LivroDTO.builder().id(id).build();
        Livro livro = Livro.builder().id(id).build();
        Livro livroAtualizado = Livro.builder().id(id).build();
        LivroDTO livroDTOAtualizado = LivroDTO.builder().id(id).build();

        when(buscaLivroService.buscarLivroPorId(id)).thenReturn(livro);
        when(livroAssembler.toEntity(livroDTO)).thenReturn(livro);
        when(livroRepository.save(livro)).thenReturn(livroAtualizado);
        when(livroAssembler.toDTO(livroAtualizado)).thenReturn(livroDTOAtualizado);

        LivroDTO result = atualizaLivroService.atualizarLivro(id, livroDTO);

        assertNotNull(result);
        assertEquals(livroDTOAtualizado, result);
        verify(buscaLivroService).buscarLivroPorId(id);
        verify(livroAssembler).toEntity(livroDTO);
        verify(livroRepository).save(livro);
        verify(livroAssembler).toDTO(livroAtualizado);
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoExistir() {
        Long id = 1L;
        LivroDTO livroDTO = LivroDTO.builder().id(id).build();

        doThrow(new RuntimeException("Livro não encontrado"))
                .when(buscaLivroService).buscarLivroPorId(id);

        Exception exception = assertThrows(RuntimeException.class, () -> atualizaLivroService.atualizarLivro(id, livroDTO));

        assertEquals("Livro não encontrado", exception.getMessage());
        verify(buscaLivroService).buscarLivroPorId(id);
        verifyNoInteractions(livroAssembler, livroRepository);
    }
}