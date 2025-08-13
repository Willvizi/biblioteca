package com.example.biblioteca.service.usuario;

import com.example.biblioteca.assembler.UsuarioAssembler;
import com.example.biblioteca.assembler.UsuarioDTOAssembler;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.validator.EmailRepetidoValidator;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriaUsuarioServiceTest {

    @InjectMocks
    private CriaUsuarioService criaUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioDTOAssembler usuarioDTOAssembler;

    @Mock
    private UsuarioAssembler usuarioAssembler;

    @Mock
    private EmailRepetidoValidator emailRepetidoValidator;

    @Captor
    private ArgumentCaptor<Usuario> usuarioArgumentCaptor;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioDTO = UsuarioDTO.builder()
                .nome("Willian Visicati")
                .email("willian.visicati@example.com")
                .telefone("123456789")
                .build();

        usuario = new Usuario();
        usuario.setNome("Willian Visicati");
        usuario.setEmail("willian.visicati@example.com");
        usuario.setTelefone("123456789");
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        when(usuarioAssembler.toEntity(usuarioDTO)).thenReturn(usuario);

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        when(usuarioDTOAssembler.entityToDTO(usuarioSalvo)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = criaUsuarioService.criarUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals(usuarioDTO.getId(), resultado.getId());

        verify(emailRepetidoValidator).validar(usuarioDTO.getEmail());
        verify(usuarioAssembler).toEntity(usuarioDTO);
        verify(usuarioRepository).save(usuarioArgumentCaptor.capture());
        verify(usuarioDTOAssembler).entityToDTO(usuarioSalvo);

        Usuario usuarioCapturado = usuarioArgumentCaptor.getValue();
        assertEquals(LocalDate.now(), usuarioCapturado.getDataCadastro());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar usuário com e-mail duplicado")
    void deveLancarExcecaoAoCriarUsuarioComEmailDuplicado() {
        doThrow(new BibliotecaBusinessException("Email já cadastrado."))
                .when(emailRepetidoValidator).validar(usuarioDTO.getEmail());

        assertThrows(BibliotecaBusinessException.class, () -> criaUsuarioService.criarUsuario(usuarioDTO));

        verify(emailRepetidoValidator).validar(usuarioDTO.getEmail());
        verifyNoInteractions(usuarioAssembler, usuarioRepository, usuarioDTOAssembler);
    }
}