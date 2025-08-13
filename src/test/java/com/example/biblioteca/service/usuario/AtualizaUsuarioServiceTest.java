package com.example.biblioteca.service.usuario;

import com.example.biblioteca.assembler.UsuarioDTOAssembler;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.validator.EmailRepetidoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizaUsuarioServiceTest {

    @InjectMocks
    private AtualizaUsuarioService atualizaUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailRepetidoValidator emailRepetidoValidator;

    @Mock
    private UsuarioDTOAssembler usuarioDTOAssembler;

    @Mock
    private BuscaUsuarioService buscaUsuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioArgumentCaptor;

    @Test
    void deveAtualizarUsuarioComSucessoERetornarUsuarioDTOAtualizado() {
        Long idUsuario = 1L;
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .nome("Novo Nome")
                .email("novo.email@example.com")
                .telefone("11987654321")
                .build();

        Usuario usuarioExistente = new Usuario(idUsuario, "Nome Antigo", "antigo.email@example.com", LocalDate.now(), "11123456789");
        Usuario usuarioSalvo = new Usuario(idUsuario, "Novo Nome", "novo.email@example.com", LocalDate.now(), "11987654321");

        UsuarioDTO usuarioDTORetornado = UsuarioDTO.builder()
                .id(idUsuario)
                .nome("Novo Nome")
                .email("novo.email@example.com")
                .telefone("11987654321")
                .dataCadastro(usuarioExistente.getDataCadastro())
                .build();

        doNothing().when(emailRepetidoValidator).validarEmailAtualizacaoUsuario(usuarioDTO.getEmail(), idUsuario);
        when(buscaUsuarioService.getUsuario(idUsuario)).thenReturn(usuarioExistente);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);
        when(usuarioDTOAssembler.entityToDTO(usuarioSalvo)).thenReturn(usuarioDTORetornado);

        UsuarioDTO result = atualizaUsuarioService.atualizarUsuario(idUsuario, usuarioDTO);

        assertNotNull(result);
        assertEquals(usuarioDTORetornado.getId(), result.getId());
        assertEquals(usuarioDTORetornado.getNome(), result.getNome());
        assertEquals(usuarioDTORetornado.getEmail(), result.getEmail());
        assertEquals(usuarioDTORetornado.getTelefone(), result.getTelefone());

        verify(emailRepetidoValidator).validarEmailAtualizacaoUsuario(usuarioDTO.getEmail(), idUsuario);
        verify(buscaUsuarioService).getUsuario(idUsuario);
        verify(usuarioRepository).save(usuarioArgumentCaptor.capture());
        verify(usuarioDTOAssembler).entityToDTO(usuarioSalvo);

        Usuario usuarioCapturado = usuarioArgumentCaptor.getValue();
        assertEquals("Novo Nome", usuarioCapturado.getNome());
        assertEquals("novo.email@example.com", usuarioCapturado.getEmail());
        assertEquals("11987654321", usuarioCapturado.getTelefone());
        assertEquals(idUsuario, usuarioCapturado.getId());
        assertEquals(usuarioExistente.getDataCadastro(), usuarioCapturado.getDataCadastro());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoAoAtualizar() {
        Long idUsuario = 99L;
        UsuarioDTO usuarioDTO = UsuarioDTO.builder().email("qualquer.email@example.com").build();

        doNothing().when(emailRepetidoValidator).validarEmailAtualizacaoUsuario(anyString(), anyLong());
        when(buscaUsuarioService.getUsuario(idUsuario)).thenThrow(new BibliotecaBusinessException("Usuário não encontrado"));

        assertThrows(BibliotecaBusinessException.class, () -> {
            atualizaUsuarioService.atualizarUsuario(idUsuario, usuarioDTO);
        });

        verify(buscaUsuarioService).getUsuario(idUsuario);
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(usuarioDTOAssembler, never()).entityToDTO(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicadoAoAtualizarUsuario() {
        Long idUsuario = 1L;
        String emailDuplicado = "email.existente@example.com";
        UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(emailDuplicado).build();

        doThrow(new BibliotecaBusinessException("Já existe um usuário cadastrado com este e-mail."))
                .when(emailRepetidoValidator).validarEmailAtualizacaoUsuario(emailDuplicado, idUsuario);

        assertThrows(BibliotecaBusinessException.class, () -> {
            atualizaUsuarioService.atualizarUsuario(idUsuario, usuarioDTO);
        });

        verify(emailRepetidoValidator).validarEmailAtualizacaoUsuario(emailDuplicado, idUsuario);
        verify(buscaUsuarioService, never()).getUsuario(anyLong());
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(usuarioDTOAssembler, never()).entityToDTO(any(Usuario.class));
    }
}