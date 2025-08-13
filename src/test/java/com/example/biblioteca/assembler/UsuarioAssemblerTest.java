package com.example.biblioteca.assembler;

import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class UsuarioAssemblerTest {

    @Test
    void deveConverterUsuarioDTOParaEntidade() {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .nome("João da Silva")
                .email("joao.silva@example.com")
                .telefone("11999999999")
                .build();

        UsuarioAssembler usuarioAssembler = new UsuarioAssembler();

        Usuario usuario = usuarioAssembler.toEntity(usuarioDTO);

        assertEquals("João da Silva", usuario.getNome());
        assertEquals("joao.silva@example.com", usuario.getEmail());
        assertEquals("11999999999", usuario.getTelefone());
    }
}