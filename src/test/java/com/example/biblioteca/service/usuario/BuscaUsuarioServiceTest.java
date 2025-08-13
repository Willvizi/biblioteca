package com.example.biblioteca.service.usuario;

import com.example.biblioteca.assembler.UsuarioDTOAssembler;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuscaUsuarioServiceTest {

    @InjectMocks
    private BuscaUsuarioService buscaUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioDTOAssembler usuarioDTOAssembler;

    @Test
    public void deveRetornarUsuarioComTodosOsCamposPreenchidos() {
        Long idUsuario = 1L;

        Usuario usuarioMock = Usuario.builder()
                .id(idUsuario)
                .nome("Willian Visicati")
                .email("willian.visicati@example.com")
                .dataCadastro(LocalDate.now())
                .telefone("9876543210")
                .build();

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuarioMock));

        Usuario result = buscaUsuarioService.getUsuario(idUsuario);

        assertNotNull(result);
        assertEquals(idUsuario, result.getId());
        assertEquals("Willian Visicati", result.getNome());
        assertEquals("willian.visicati@example.com", result.getEmail());
        assertEquals("9876543210", result.getTelefone());
        assertNotNull(result.getDataCadastro());
        verify(usuarioRepository, times(1)).findById(idUsuario);
    }

    @Test
    public void deveLancarExcecaoComIdNulo() {
        Long idUsuario = null;

        BibliotecaBusinessException exception = assertThrows(BibliotecaBusinessException.class, () -> {
            buscaUsuarioService.getUsuario(idUsuario);
        });

        assertEquals(BuscaUsuarioService.USUARIO_NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    public void deveRetornarUsuarioDTOAoBuscarPorId() {
        Long idUsuario = 1L;

        Usuario usuarioMock = Usuario.builder()
                .id(idUsuario)
                .nome("Willian Visicati")
                .email("willian.visicati@example.com")
                .dataCadastro(LocalDate.now())
                .telefone("9876543210")
                .build();

        UsuarioDTO usuarioDTOMock = UsuarioDTO.builder()
                .id(idUsuario)
                .nome("Willian Visicati")
                .email("willian.visicati@example.com")
                .dataCadastro(LocalDate.now())
                .telefone("9876543210")
                .build();

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuarioMock));
        when(usuarioDTOAssembler.entityToDTO(usuarioMock)).thenReturn(usuarioDTOMock);

        UsuarioDTO resultado = buscaUsuarioService.buscaUsuarioById(idUsuario);

        assertNotNull(resultado);
        assertEquals(idUsuario, resultado.getId());
        assertEquals("Willian Visicati", resultado.getNome());
        assertEquals("willian.visicati@example.com", resultado.getEmail());
        assertEquals("9876543210", resultado.getTelefone());
        assertNotNull(resultado.getDataCadastro());

        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioDTOAssembler, times(1)).entityToDTO(usuarioMock);
    }

    @Test
    public void deveRetornarListaDeUsuariosDTO() {
        Usuario usuario1 = Usuario.builder()
                .id(1L)
                .nome("Willian Visicati")
                .email("willian.visicati@example.com")
                .dataCadastro(LocalDate.now())
                .telefone("9876543210")
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(2L)
                .nome("Maria Silva")
                .email("maria.silva@example.com")
                .dataCadastro(LocalDate.now())
                .telefone("1234567890")
                .build();

        List<Usuario> listaUsuarios = Arrays.asList(usuario1, usuario2);

        UsuarioDTO usuarioDTO1 = UsuarioDTO.builder()
                .id(1L)
                .nome("Willian Visicati")
                .email("willian.visicati@example.com")
                .dataCadastro(LocalDate.now())
                .telefone("9876543210")
                .build();

        UsuarioDTO usuarioDTO2 = UsuarioDTO.builder()
                .id(2L)
                .nome("Maria Silva")
                .email("maria.silva@example.com")
                .dataCadastro(LocalDate.now())
                .telefone("1234567890")
                .build();

        List<UsuarioDTO> listaUsuariosDTO = Arrays.asList(usuarioDTO1, usuarioDTO2);

        when(usuarioRepository.findAll()).thenReturn(listaUsuarios);
        when(usuarioDTOAssembler.entityListToDTO(listaUsuarios)).thenReturn(listaUsuariosDTO);

        List<UsuarioDTO> resultado = buscaUsuarioService.getUsuarios();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Willian Visicati", resultado.get(0).getNome());
        assertEquals(2L, resultado.get(1).getId());
        assertEquals("Maria Silva", resultado.get(1).getNome());

        verify(usuarioRepository, times(1)).findAll();
        verify(usuarioDTOAssembler, times(1)).entityListToDTO(listaUsuarios);
    }
}