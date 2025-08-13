package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.assembler.EmprestimoAssembler;
import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Emprestimo.StatusEmprestimo;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.service.usuario.BuscaUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscaEmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private BuscaUsuarioService buscaUsuarioService;

    @Mock
    private EmprestimoAssembler emprestimoAssembler;

    @InjectMocks
    private BuscaEmprestimoService buscaEmprestimoService;

    private Usuario usuario;
    private Livro livro;
    private Emprestimo emprestimo1;
    private Emprestimo emprestimo2;
    private EmprestimoDTO emprestimoDTO1;
    private EmprestimoDTO emprestimoDTO2;
    private final Long EMPRESTIMO_ID = 1L;
    private final Long USUARIO_ID = 1L;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(USUARIO_ID);
        usuario.setNome("Usuário Teste");

        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Livro Teste");

        LocalDate dataEmprestimo = LocalDate.now().minusDays(5);
        LocalDate dataDevolucao = LocalDate.now().plusDays(5);

        emprestimo1 = Emprestimo.builder()
                .id(EMPRESTIMO_ID)
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(dataEmprestimo)
                .dataDevolucao(dataDevolucao)
                .status(StatusEmprestimo.ATIVO)
                .build();

        emprestimoDTO1 = EmprestimoDTO.builder()
                .id(EMPRESTIMO_ID)
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(dataEmprestimo)
                .dataDevolucao(dataDevolucao)
                .status(StatusEmprestimo.ATIVO)
                .build();

        emprestimo2 = Emprestimo.builder()
                .id(2L)
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(dataEmprestimo.minusDays(2))
                .dataDevolucao(dataDevolucao.minusDays(2))
                .status(StatusEmprestimo.DEVOLVIDO)
                .build();

        emprestimoDTO2 = EmprestimoDTO.builder()
                .id(2L)
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(emprestimo2.getDataEmprestimo())
                .dataDevolucao(emprestimo2.getDataDevolucao())
                .status(StatusEmprestimo.DEVOLVIDO)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os empréstimos")
    void deveListarTodosEmprestimos() {
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo1, emprestimo2);
        List<EmprestimoDTO> emprestimosDTO = Arrays.asList(emprestimoDTO1, emprestimoDTO2);

        when(emprestimoRepository.findAll()).thenReturn(emprestimos);
        when(emprestimoAssembler.toDTO(emprestimo1)).thenReturn(emprestimoDTO1);
        when(emprestimoAssembler.toDTO(emprestimo2)).thenReturn(emprestimoDTO2);

        List<EmprestimoDTO> resultado = buscaEmprestimoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(EMPRESTIMO_ID, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());

        verify(emprestimoRepository).findAll();
        verify(emprestimoAssembler, times(2)).toDTO(any(Emprestimo.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem empréstimos")
    void deveRetornarListaVaziaQuandoNaoExistemEmprestimos() {
        when(emprestimoRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmprestimoDTO> resultado = buscaEmprestimoService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(emprestimoRepository).findAll();
        verify(emprestimoAssembler, never()).toDTO(any(Emprestimo.class));
    }

    @Test
    @DisplayName("Deve buscar empréstimo por ID")
    void deveBuscarEmprestimoPorId() {
        when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.of(emprestimo1));
        when(emprestimoAssembler.toDTO(emprestimo1)).thenReturn(emprestimoDTO1);

        EmprestimoDTO resultado = buscaEmprestimoService.buscarPorId(EMPRESTIMO_ID);

        assertNotNull(resultado);
        assertEquals(EMPRESTIMO_ID, resultado.getId());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(livro, resultado.getLivro());
        assertEquals(StatusEmprestimo.ATIVO, resultado.getStatus());

        verify(emprestimoRepository).findById(EMPRESTIMO_ID);
        verify(emprestimoAssembler).toDTO(emprestimo1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar empréstimo inexistente por ID")
    void deveLancarExcecaoAoBuscarEmprestimoInexistente() {
        when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.empty());

        BibliotecaBusinessException exception = assertThrows(BibliotecaBusinessException.class, () -> {
            buscaEmprestimoService.buscarPorId(EMPRESTIMO_ID);
        });

        assertEquals("Empréstimo não encontrado com o ID: " + EMPRESTIMO_ID, exception.getMessage());

        verify(emprestimoRepository).findById(EMPRESTIMO_ID);
        verify(emprestimoAssembler, never()).toDTO(any(Emprestimo.class));
    }

    @Test
    @DisplayName("Deve buscar empréstimos por usuário")
    void deveBuscarEmprestimosPorUsuario() {
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo1, emprestimo2);
        List<EmprestimoDTO> emprestimosDTO = Arrays.asList(emprestimoDTO1, emprestimoDTO2);

        when(buscaUsuarioService.getUsuario(USUARIO_ID)).thenReturn(usuario);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(emprestimos);
        when(emprestimoAssembler.toDTO(emprestimo1)).thenReturn(emprestimoDTO1);
        when(emprestimoAssembler.toDTO(emprestimo2)).thenReturn(emprestimoDTO2);

        List<EmprestimoDTO> resultado = buscaEmprestimoService.buscarPorUsuario(USUARIO_ID);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(EMPRESTIMO_ID, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());

        verify(buscaUsuarioService).getUsuario(USUARIO_ID);
        verify(emprestimoRepository).findByUsuario(usuario);
        verify(emprestimoAssembler, times(2)).toDTO(any(Emprestimo.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando usuário não tem empréstimos")
    void deveRetornarListaVaziaQuandoUsuarioNaoTemEmprestimos() {
        when(buscaUsuarioService.getUsuario(USUARIO_ID)).thenReturn(usuario);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(Collections.emptyList());

        List<EmprestimoDTO> resultado = buscaEmprestimoService.buscarPorUsuario(USUARIO_ID);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(buscaUsuarioService).getUsuario(USUARIO_ID);
        verify(emprestimoRepository).findByUsuario(usuario);
        verify(emprestimoAssembler, never()).toDTO(any(Emprestimo.class));
    }

    @Test
    @DisplayName("Deve propagar exceção quando usuário não existe")
    void devePropararExcecaoQuandoUsuarioNaoExiste() {
        when(buscaUsuarioService.getUsuario(USUARIO_ID))
                .thenThrow(new BibliotecaBusinessException("Usuário não encontrado"));

        BibliotecaBusinessException exception = assertThrows(BibliotecaBusinessException.class, () -> {
            buscaEmprestimoService.buscarPorUsuario(USUARIO_ID);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(buscaUsuarioService).getUsuario(USUARIO_ID);
        verify(emprestimoRepository, never()).findByUsuario(any(Usuario.class));
        verify(emprestimoAssembler, never()).toDTO(any(Emprestimo.class));
    }
}