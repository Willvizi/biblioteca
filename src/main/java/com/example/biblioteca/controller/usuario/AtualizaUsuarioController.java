package com.example.biblioteca.controller.usuario;

import com.example.biblioteca.assembler.UsuarioDTOAssembler;
import com.example.biblioteca.assembler.UsuarioResponseDTOAssembler;
import com.example.biblioteca.dto.UsuarioResponseDTO;
import com.example.biblioteca.input.UsuarioInput;
import com.example.biblioteca.service.usuario.AtualizaUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
public class AtualizaUsuarioController {
    private final AtualizaUsuarioService atualizaUsuarioService;
    private final UsuarioDTOAssembler usuarioDTOAssembler;
    private final UsuarioResponseDTOAssembler usuarioResponseDTOAssembler;

    @Operation(summary = "Atualiza um usuário")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizaUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioInput usuarioInput) {
        return ResponseEntity.ok(usuarioResponseDTOAssembler
                .toDTO(atualizaUsuarioService
                        .atualizarUsuario(id, usuarioDTOAssembler
                                .inputToDTO(usuarioInput))));
    }

}
