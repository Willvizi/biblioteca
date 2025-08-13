package com.example.biblioteca.assembler;

import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.input.UsuarioInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioDTOAssemblerTest {

    private UsuarioDTOAssembler usuarioDTOAssembler;
    private static final String NOME = "Willian Visicati";
    private static final String EMAIL = "willian.visicati@example.com";
    private static final String TELEFONE = "1234567890";

    @BeforeEach
    void setUp() {
        usuarioDTOAssembler = new UsuarioDTOAssembler();
    }

    @Test
    void deveConverterUsuarioInputParaUsuarioDTO() {
        var usuarioInput = new UsuarioInput(NOME, EMAIL, TELEFONE);

        UsuarioDTO result = usuarioDTOAssembler.inputToDTO(usuarioInput);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo(NOME);
        assertThat(result.getEmail()).isEqualTo(EMAIL);
        assertThat(result.getTelefone()).isEqualTo(TELEFONE);
        assertThat(result.getId()).isNull();
        assertThat(result.getDataCadastro()).isNull();
    }

    @Test
    void deveConverterUsuarioParaUsuarioDTO() {
        LocalDate now = LocalDate.now();
        var usuario = Usuario.builder()
                .id(1L)
                .nome(NOME)
                .email(EMAIL)
                .telefone(TELEFONE)
                .dataCadastro(now)
                .build();

        UsuarioDTO result = usuarioDTOAssembler.entityToDTO(usuario);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo(NOME);
        assertThat(result.getEmail()).isEqualTo(EMAIL);
        assertThat(result.getTelefone()).isEqualTo(TELEFONE);
        assertThat(result.getDataCadastro()).isEqualTo(now);
    }

    @Test
    void deveConverterListaDeUsuariosParaListaDeUsuarioDTO() {
        LocalDate now = LocalDate.now();
        var usuario1 = Usuario.builder()
                .id(1L)
                .nome(NOME)
                .email(EMAIL)
                .telefone(TELEFONE)
                .dataCadastro(now)
                .build();
        var usuario2 = Usuario.builder()
                .id(2L)
                .nome("Jane Doe")
                .email("jane.doe@example.com")
                .telefone("0987654321")
                .dataCadastro(now.minusDays(1))
                .build();
        List<Usuario> usuarios = List.of(usuario1, usuario2);

        List<UsuarioDTO> result = usuarioDTOAssembler.entityListToDTO(usuarios);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNome()).isEqualTo(NOME);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getNome()).isEqualTo("Jane Doe");
    }

    @Test
    void deveRetornarListaVaziaAoConverterListaVazia() {
        List<Usuario> usuarios = Collections.emptyList();

        List<UsuarioDTO> result = usuarioDTOAssembler.entityListToDTO(usuarios);

        assertThat(result).isEmpty();
    }
}