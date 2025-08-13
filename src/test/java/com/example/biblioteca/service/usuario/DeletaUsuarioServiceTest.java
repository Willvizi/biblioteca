package com.example.biblioteca.service.usuario;

import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.service.emprestimo.DeletaEmprestimoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletaUsuarioServiceTest {

    @Mock
    private BuscaUsuarioService buscaUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DeletaEmprestimoService deletaEmprestimoService;

    @InjectMocks
    private DeletaUsuarioService deletaUsuarioService;

    private Usuario usuario;
    private Long idUsuario;

    @BeforeEach
    void setUp() {
        idUsuario = 1L;
        usuario = Usuario.builder()
                .id(idUsuario)
                .nome("Willian Visicati")
                .email("willian.visicati@email.com")
                .telefone("11987654321")
                .build();
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deletarUsuario_QuandoUsuarioExiste_DeveDeletarUsuario() {
        when(buscaUsuarioService.getUsuario(idUsuario)).thenReturn(usuario);
        doNothing().when(deletaEmprestimoService).deletarPorUsuario(usuario);

        deletaUsuarioService.deletarUsuario(idUsuario);

        verify(buscaUsuarioService, times(1)).getUsuario(idUsuario);
        verify(deletaEmprestimoService, times(1)).deletarPorUsuario(usuario);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("Deve seguir ordem correta das operações ao deletar usuário")
    void deletarUsuario_DeveExecutarOperacoesNaOrdemCorreta() {
        when(buscaUsuarioService.getUsuario(idUsuario)).thenReturn(usuario);

        deletaUsuarioService.deletarUsuario(idUsuario);

        var inOrder = inOrder(buscaUsuarioService, deletaEmprestimoService, usuarioRepository);
        inOrder.verify(buscaUsuarioService).getUsuario(idUsuario);
        inOrder.verify(deletaEmprestimoService).deletarPorUsuario(usuario);
        inOrder.verify(usuarioRepository).delete(usuario);
    }


}