package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.assembler.EmprestimoAssembler;
import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.input.EmprestimoInput;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.service.livro.BuscaLivroService;
import com.example.biblioteca.service.usuario.BuscaUsuarioService;
import com.example.biblioteca.validator.LivroEmprestadoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriaEmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private BuscaUsuarioService buscaUsuarioService;

    @Mock
    private BuscaLivroService buscaLivroService;

    @Mock
    private EmprestimoAssembler emprestimoAssembler;

    @Mock
    private LivroEmprestadoValidator livroEmprestadoValidator;

    @InjectMocks
    private CriaEmprestimoService criaEmprestimoService;

    private Usuario usuario;
    private Livro livro;
    private EmprestimoInput emprestimoInput;
    private Emprestimo emprestimo;
    private final Long USUARIO_ID = 1L;
    private final Long LIVRO_ID = 1L;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(USUARIO_ID);
        usuario.setNome("Usuário Teste");

        livro = new Livro();
        livro.setId(LIVRO_ID);
        livro.setTitulo("Livro Teste");

        LocalDate dataDevolucao = LocalDate.now().plusDays(7);

        emprestimoInput = new EmprestimoInput();
        emprestimoInput.setUsuarioId(USUARIO_ID);
        emprestimoInput.setLivroId(LIVRO_ID);
        emprestimoInput.setDataDevolucao(dataDevolucao);

        emprestimo = Emprestimo.builder()
                .id(1L)
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(LocalDate.now())
                .dataDevolucao(dataDevolucao)
                .status(Emprestimo.StatusEmprestimo.ATIVO)
                .build();
    }

    @Test
    @DisplayName("Deve criar um empréstimo com sucesso")
    void criarEmprestimoQuandoDadosValidos() {
        when(buscaUsuarioService.getUsuario(USUARIO_ID)).thenReturn(usuario);
        when(buscaLivroService.buscarLivroPorId(LIVRO_ID)).thenReturn(livro);
        doNothing().when(livroEmprestadoValidator).validar(LIVRO_ID);
        when(emprestimoAssembler.toEntity(emprestimoInput, usuario, livro)).thenReturn(emprestimo);
        when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);

        Emprestimo resultado = criaEmprestimoService.criar(emprestimoInput);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(USUARIO_ID, resultado.getUsuario().getId());
        assertEquals(LIVRO_ID, resultado.getLivro().getId());
        assertEquals(Emprestimo.StatusEmprestimo.ATIVO, resultado.getStatus());

        verify(buscaUsuarioService).getUsuario(USUARIO_ID);
        verify(buscaLivroService).buscarLivroPorId(LIVRO_ID);
        verify(livroEmprestadoValidator).validar(LIVRO_ID);
        verify(emprestimoAssembler).toEntity(emprestimoInput, usuario, livro);
        verify(emprestimoRepository).save(emprestimo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void lancarExcecaoQuandoUsuarioNaoExiste() {
        when(buscaUsuarioService.getUsuario(USUARIO_ID))
                .thenThrow(new BibliotecaBusinessException("Usuário não encontrado"));

        BibliotecaBusinessException exception = assertThrows(BibliotecaBusinessException.class, () -> {
            criaEmprestimoService.criar(emprestimoInput);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(buscaUsuarioService).getUsuario(USUARIO_ID);
        verify(buscaLivroService, never()).buscarLivroPorId(anyLong());
        verify(livroEmprestadoValidator, never()).validar(anyLong());
        verify(emprestimoAssembler, never()).toEntity(any(), any(), any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro não existe")
    void lancarExcecaoQuandoLivroNaoExiste() {
        when(buscaUsuarioService.getUsuario(USUARIO_ID)).thenReturn(usuario);
        when(buscaLivroService.buscarLivroPorId(LIVRO_ID))
                .thenThrow(new BibliotecaBusinessException("Livro não encontrado com o ID: " + LIVRO_ID));

        BibliotecaBusinessException exception = assertThrows(BibliotecaBusinessException.class, () -> {
            criaEmprestimoService.criar(emprestimoInput);
        });

        assertEquals("Livro não encontrado com o ID: " + LIVRO_ID, exception.getMessage());

        verify(buscaUsuarioService).getUsuario(USUARIO_ID);
        verify(buscaLivroService).buscarLivroPorId(LIVRO_ID);
        verify(livroEmprestadoValidator, never()).validar(anyLong());
        verify(emprestimoAssembler, never()).toEntity(any(), any(), any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro já está emprestado")
    void lancarExcecaoQuandoLivroJaEmprestado() {
        String mensagemErro = "Livro já está emprestado";
        when(buscaUsuarioService.getUsuario(USUARIO_ID)).thenReturn(usuario);
        when(buscaLivroService.buscarLivroPorId(LIVRO_ID)).thenReturn(livro);
        doThrow(new BibliotecaBusinessException(mensagemErro))
                .when(livroEmprestadoValidator).validar(LIVRO_ID);

        BibliotecaBusinessException exception = assertThrows(BibliotecaBusinessException.class, () -> {
            criaEmprestimoService.criar(emprestimoInput);
        });

        assertEquals(mensagemErro, exception.getMessage());

        verify(buscaUsuarioService).getUsuario(USUARIO_ID);
        verify(buscaLivroService).buscarLivroPorId(LIVRO_ID);
        verify(livroEmprestadoValidator).validar(LIVRO_ID);
        verify(emprestimoAssembler, never()).toEntity(any(), any(), any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve verificar o fluxo completo de criação de empréstimo")
    void verificarFluxoCompletoDeCriacao() {
        when(buscaUsuarioService.getUsuario(USUARIO_ID)).thenReturn(usuario);
        when(buscaLivroService.buscarLivroPorId(LIVRO_ID)).thenReturn(livro);
        doNothing().when(livroEmprestadoValidator).validar(LIVRO_ID);
        when(emprestimoAssembler.toEntity(emprestimoInput, usuario, livro)).thenReturn(emprestimo);
        when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);

        Emprestimo resultado = criaEmprestimoService.criar(emprestimoInput);

        assertNotNull(resultado);

        inOrder(buscaUsuarioService, buscaLivroService, livroEmprestadoValidator,
                emprestimoAssembler, emprestimoRepository)
                .verify(buscaUsuarioService).getUsuario(USUARIO_ID);
        inOrder(buscaUsuarioService, buscaLivroService, livroEmprestadoValidator,
                emprestimoAssembler, emprestimoRepository)
                .verify(buscaLivroService).buscarLivroPorId(LIVRO_ID);
        inOrder(buscaUsuarioService, buscaLivroService, livroEmprestadoValidator,
                emprestimoAssembler, emprestimoRepository)
                .verify(livroEmprestadoValidator).validar(LIVRO_ID);
        inOrder(buscaUsuarioService, buscaLivroService, livroEmprestadoValidator,
                emprestimoAssembler, emprestimoRepository)
                .verify(emprestimoAssembler).toEntity(emprestimoInput, usuario, livro);
        inOrder(buscaUsuarioService, buscaLivroService, livroEmprestadoValidator,
                emprestimoAssembler, emprestimoRepository)
                .verify(emprestimoRepository).save(emprestimo);
    }

    @Test
    @DisplayName("Deve verificar valores corretos no objeto salvo")
    void verificarValoresCorretosNoObjetoSalvo() {
        LocalDate dataDevolucaoEsperada = LocalDate.now().plusDays(7);

        Emprestimo emprestimoMontado = Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(LocalDate.now())
                .dataDevolucao(dataDevolucaoEsperada)
                .status(Emprestimo.StatusEmprestimo.ATIVO)
                .build();

        Emprestimo emprestimoSalvo = Emprestimo.builder()
                .id(1L)
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(LocalDate.now())
                .dataDevolucao(dataDevolucaoEsperada)
                .status(Emprestimo.StatusEmprestimo.ATIVO)
                .build();

        when(buscaUsuarioService.getUsuario(USUARIO_ID)).thenReturn(usuario);
        when(buscaLivroService.buscarLivroPorId(LIVRO_ID)).thenReturn(livro);
        doNothing().when(livroEmprestadoValidator).validar(LIVRO_ID);
        when(emprestimoAssembler.toEntity(emprestimoInput, usuario, livro)).thenReturn(emprestimoMontado);
        when(emprestimoRepository.save(emprestimoMontado)).thenReturn(emprestimoSalvo);

        Emprestimo resultado = criaEmprestimoService.criar(emprestimoInput);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(livro, resultado.getLivro());
        assertEquals(dataDevolucaoEsperada, resultado.getDataDevolucao());
        assertEquals(Emprestimo.StatusEmprestimo.ATIVO, resultado.getStatus());
    }
}