package com.example.biblioteca.assembler;

import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.dto.UsuarioResponseDTO;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class UsuarioResponseDTOAssembler {

public UsuarioResponseDTO toDTO(UsuarioDTO usuarioDTO){
        return UsuarioResponseDTO.builder()
                .id(usuarioDTO.getId())
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .telefone(usuarioDTO.getTelefone())
                .dataCadastro(usuarioDTO.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
    }

}
