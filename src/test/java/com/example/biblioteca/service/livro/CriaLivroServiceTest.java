package com.example.biblioteca.service.livro;

import com.example.biblioteca.assembler.LivroAssembler;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriaLivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LivroAssembler livroAssembler;

    @InjectMocks
    private CriaLivroService criaLivroService;

    @BeforeEach
    void setUp() {
        criaLivroService = new CriaLivroService(livroRepository, livroAssembler);
    }

    @Test
    void deveCriarLivroComSucesso() {
        LivroDTO livroDTO = new LivroDTO();
        Livro livro = new Livro();
        Livro livroSalvo = new Livro();
        LivroDTO livroDTOResponse = new LivroDTO();

        when(livroAssembler.toEntity(livroDTO)).thenReturn(livro);
        when(livroRepository.save(livro)).thenReturn(livroSalvo);
        when(livroAssembler.toDTO(livroSalvo)).thenReturn(livroDTOResponse);

        LivroDTO result = criaLivroService.criarLivro(livroDTO);

        assertNotNull(result);
        assertEquals(livroDTOResponse, result);
        verify(livroAssembler).toEntity(livroDTO);
        verify(livroRepository).save(livro);
        verify(livroAssembler).toDTO(livroSalvo);
    }
}