package com.example.biblioteca.assembler;

import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler {
    public Usuario toEntity(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .telefone(usuarioDTO.getTelefone())
                .build();
    }
}
