package com.example.biblioteca.controller.usuario;

import com.example.biblioteca.assembler.UsuarioResponseDTOAssembler;
import com.example.biblioteca.dto.UsuarioResponseDTO;
import com.example.biblioteca.service.usuario.BuscaUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Usuários")
@RequestMapping("/usuario")
@RestController
@RequiredArgsConstructor
public class BuscaUsuariosController {
    private final BuscaUsuarioService buscaUsuarioService;
    private final UsuarioResponseDTOAssembler usuarioResponseDTOAssembler;


    @Operation(summary = "Busca um usuário pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscaUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioResponseDTOAssembler.toDTO(buscaUsuarioService.buscaUsuarioById(id)));
    }

    @Operation(summary = "Lista todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> buscaUsuarios() {
        return ResponseEntity.ok(usuarioResponseDTOAssembler.toCollectionDTO(buscaUsuarioService.getUsuarios()));
    }

}