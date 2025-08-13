package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.repository.EmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletaEmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepositoryWillianVisicati;

    private DeletaEmprestimoService deletaEmprestimoService;

    @BeforeEach
    void setUp() {
        deletaEmprestimoService = new DeletaEmprestimoService(emprestimoRepositoryWillianVisicati);
    }

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
        when(emprestimoRepositoryWillianVisicati.findByLivro(livro)).thenReturn(emprestimos);

        deletaEmprestimoService.deletarPorLivro(livro);

        verify(emprestimoRepositoryWillianVisicati).deleteAll(emprestimos);
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
        when(emprestimoRepositoryWillianVisicati.findByUsuario(usuario)).thenReturn(emprestimos);

        deletaEmprestimoService.deletarPorUsuario(usuario);

        verify(emprestimoRepositoryWillianVisicati).deleteAll(emprestimos);
    }
}