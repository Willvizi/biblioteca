package com.example.biblioteca.assembler;

import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.input.UsuarioInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UsuarioInputAssemblerTest {

    @InjectMocks
    private UsuarioDTOAssembler assembler;

    @Test
    void deveConverterUsuarioInputParaUsuarioDTO() {
        UsuarioInput usuarioInput = UsuarioInput.builder()
                .nome("Willian Visicati")
                .email("will@teste.com")
                .telefone("987654321")
                .build();

        UsuarioDTO usuarioDTO = assembler.inputToDTO(usuarioInput);

        assertThat(usuarioDTO.getNome()).isEqualTo(usuarioInput.getNome());
        assertThat(usuarioDTO.getEmail()).isEqualTo(usuarioInput.getEmail());
        assertThat(usuarioDTO.getTelefone()).isEqualTo(usuarioInput.getTelefone());
        assertThat(usuarioDTO.getId()).isNull();
        assertThat(usuarioDTO.getDataCadastro()).isNull();
    }
}