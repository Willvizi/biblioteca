package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.assembler.EmprestimoAssembler;
import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Emprestimo.StatusEmprestimo;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.input.AtualizaEmprestimoInput;
import com.example.biblioteca.repository.EmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizaEmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private EmprestimoAssembler emprestimoAssembler;

    @InjectMocks
    private AtualizaEmprestimoService atualizaEmprestimoService;

    @Captor
    private ArgumentCaptor<Emprestimo> emprestimoCaptor;

    private Emprestimo emprestimo;
    private EmprestimoDTO emprestimoDTO;
    private final Long EMPRESTIMO_ID = 1L;

    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Livro Teste");

        emprestimo = Emprestimo.builder()
                .id(EMPRESTIMO_ID)
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(LocalDate.now().minusDays(5))
                .dataDevolucao(LocalDate.now().plusDays(5))
                .status(StatusEmprestimo.ATIVO)
                .build();

        emprestimoDTO = EmprestimoDTO.builder()
                .id(EMPRESTIMO_ID)
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(emprestimo.getDataEmprestimo())
                .dataDevolucao(emprestimo.getDataDevolucao())
                .status(emprestimo.getStatus())
                .build();
    }

    @Test
    @DisplayName("Deve devolver um livro com sucesso")
    void devolverLivroQuandoEmprestimoExisteDeveRetornarEmprestimoDTO() {
        when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(emprestimoAssembler.toDTO(any(Emprestimo.class))).thenReturn(emprestimoDTO);

        EmprestimoDTO resultado = atualizaEmprestimoService.devolverLivro(EMPRESTIMO_ID);

        assertNotNull(resultado);
        assertEquals(EMPRESTIMO_ID, resultado.getId());

        verify(emprestimoRepository).findById(EMPRESTIMO_ID);
        verify(emprestimoRepository).save(emprestimoCaptor.capture());

        Emprestimo emprestimoSalvo = emprestimoCaptor.getValue();
        assertEquals(StatusEmprestimo.DEVOLVIDO, emprestimoSalvo.getStatus());
        assertEquals(LocalDate.now(), emprestimoSalvo.getDataDevolucao());

        verify(emprestimoAssembler).toDTO(emprestimo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar devolver um empréstimo inexistente")
    void devolverLivroQuandoEmprestimoNaoExisteDeveLancarExcecao() {
        when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.empty());

        BibliotecaBusinessException exception = assertThrows(BibliotecaBusinessException.class, () -> {
            atualizaEmprestimoService.devolverLivro(EMPRESTIMO_ID);
        });

        assertEquals("Empréstimo não encontrado", exception.getMessage());

        verify(emprestimoRepository).findById(EMPRESTIMO_ID);
        verify(emprestimoRepository, never()).save(any());
        verify(emprestimoAssembler, never()).toDTO(any());
    }

    @Test
    @DisplayName("Deve atualizar a data de devolução com sucesso")
    void atualizarDataDevolucaoQuandoEmprestimoExisteDeveRetornarEmprestimoDTO() {
        LocalDate novaDataDevolucao = LocalDate.now().plusDays(10);
        AtualizaEmprestimoInput input = new AtualizaEmprestimoInput();
        input.setDataDevolucao(novaDataDevolucao);

        when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(emprestimoAssembler.toDTO(any(Emprestimo.class))).thenReturn(emprestimoDTO);

        EmprestimoDTO resultado = atualizaEmprestimoService.atualizarDataDevolucao(EMPRESTIMO_ID, input);

        assertNotNull(resultado);
        assertEquals(EMPRESTIMO_ID, resultado.getId());

        verify(emprestimoRepository).findById(EMPRESTIMO_ID);
        verify(emprestimoRepository).save(emprestimoCaptor.capture());

        Emprestimo emprestimoSalvo = emprestimoCaptor.getValue();
        assertEquals(StatusEmprestimo.ATIVO, emprestimoSalvo.getStatus());
        assertEquals(novaDataDevolucao, emprestimoSalvo.getDataDevolucao());

        verify(emprestimoAssembler).toDTO(emprestimo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar atualizar a data de um empréstimo inexistente")
    void atualizarDataDevolucaoQuandoEmprestimoNaoExisteDeveLancarExcecao() {
        AtualizaEmprestimoInput input = new AtualizaEmprestimoInput();
        input.setDataDevolucao(LocalDate.now().plusDays(10));

        when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.empty());

        BibliotecaBusinessException exception = assertThrows(BibliotecaBusinessException.class, () -> {
            atualizaEmprestimoService.atualizarDataDevolucao(EMPRESTIMO_ID, input);
        });

        assertEquals("Empréstimo não encontrado", exception.getMessage());

        verify(emprestimoRepository).findById(EMPRESTIMO_ID);
        verify(emprestimoRepository, never()).save(any());
        verify(emprestimoAssembler, never()).toDTO(any());
    }

    @Test
    @DisplayName("Deve verificar se o status e a data de devolução são atualizados corretamente")
    void devolverLivroDeveAtualizarStatusEDataDevolucaoCorretamente() {
        LocalDate dataAntesDaTentativa = LocalDate.now().minusDays(1);
        emprestimo.setDataDevolucao(dataAntesDaTentativa);

        when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(emprestimoAssembler.toDTO(any(Emprestimo.class))).thenReturn(emprestimoDTO);

        atualizaEmprestimoService.devolverLivro(EMPRESTIMO_ID);

        verify(emprestimoRepository).save(emprestimoCaptor.capture());
        Emprestimo emprestimoSalvo = emprestimoCaptor.getValue();

        assertEquals(StatusEmprestimo.DEVOLVIDO, emprestimoSalvo.getStatus());
        assertNotEquals(dataAntesDaTentativa, emprestimoSalvo.getDataDevolucao());
        assertEquals(LocalDate.now(), emprestimoSalvo.getDataDevolucao());
    }

    @Test
    @DisplayName("Deve verificar se o status é atualizado para ATIVO ao alterar a data de devolução")
    void atualizarDataDevolucaoDeveAtualizarStatusParaAtivo() {
        emprestimo.setStatus(StatusEmprestimo.ATRASADO);

        AtualizaEmprestimoInput input = new AtualizaEmprestimoInput();
        input.setDataDevolucao(LocalDate.now().plusDays(10));

        when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(emprestimoAssembler.toDTO(any(Emprestimo.class))).thenReturn(emprestimoDTO);

        atualizaEmprestimoService.atualizarDataDevolucao(EMPRESTIMO_ID, input);

        verify(emprestimoRepository).save(emprestimoCaptor.capture());
        Emprestimo emprestimoSalvo = emprestimoCaptor.getValue();

        assertEquals(StatusEmprestimo.ATIVO, emprestimoSalvo.getStatus());
    }
}