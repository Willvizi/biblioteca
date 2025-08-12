package com.example.biblioteca.controller.usuario;

import com.example.biblioteca.assembler.UsuarioDTOAssembler;
import com.example.biblioteca.assembler.UsuarioResponseDTOAssembler;
import com.example.biblioteca.dto.UsuarioResponseDTO;
import com.example.biblioteca.input.UsuarioInput;
import com.example.biblioteca.service.usuario.CriaUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class CriaUsuarioController {
    private final UsuarioDTOAssembler usuarioDTOAssembler;
    private final CriaUsuarioService criaUsuarioService;
    private final UsuarioResponseDTOAssembler usuarioResponseDTOAssembler;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criaUsuario(@RequestBody @Valid UsuarioInput usuarioInput) {
        return ResponseEntity.ok(usuarioResponseDTOAssembler
                .toDTO(criaUsuarioService
                        .criarUsuario(usuarioDTOAssembler
                                .inputToDTO(usuarioInput))));
    }

}
