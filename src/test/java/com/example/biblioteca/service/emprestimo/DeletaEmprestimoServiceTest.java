package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.repository.EmprestimoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletaEmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @InjectMocks
    private DeletaEmprestimoService deletaEmprestimoService;

    @Test
    void deveDeletarEmprestimosPorLivro() {
        Livro livro = Livro.builder()
                .id(1L)
                .titulo("Livro Teste")
                .autor("Autor Teste")
                .isbn("1234567890")
                .dataPublicacao(java.time.LocalDate.of(2020, 1, 1))
                .categoria("Teste")
                .build();

        List<Emprestimo> emprestimos = List.of(Emprestimo.builder().build());
        when(emprestimoRepository.findByLivro(livro)).thenReturn(emprestimos);

        deletaEmprestimoService.deletarPorLivro(livro);

        verify(emprestimoRepository).deleteAll(emprestimos);
    }

    @Test
    void deveDeletarEmprestimosPorUsuario() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Usuario Teste")
                .email("usuario@teste.com")
                .dataCadastro(java.time.LocalDate.of(2020, 1, 1))
                .telefone("91999999999")
                .build();

        List<Emprestimo> emprestimos = List.of(Emprestimo.builder().build());
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(emprestimos);

        deletaEmprestimoService.deletarPorUsuario(usuario);

        verify(emprestimoRepository).deleteAll(emprestimos);
    }
}