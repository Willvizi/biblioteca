package com.example.biblioteca.assembler;

import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.input.UsuarioInput;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioDTOAssembler {
    public UsuarioDTO inputToDTO(UsuarioInput usuarioInput) {
        return UsuarioDTO.builder()
                .nome(usuarioInput.getNome())
                .email(usuarioInput.getEmail())
                .telefone(usuarioInput.getTelefone())
                .build();
    }

    public UsuarioDTO entityToDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .dataCadastro(usuario.getDataCadastro())
                .build();
    }

    public List<UsuarioDTO> entityListToDTO(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::entityToDTO)
                .toList();
    }
}
