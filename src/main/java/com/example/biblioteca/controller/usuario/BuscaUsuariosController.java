package com.example.biblioteca.controller.usuario;

import com.example.biblioteca.assembler.UsuarioResponseDTOAssembler;
import com.example.biblioteca.dto.UsuarioResponseDTO;
import com.example.biblioteca.service.usuario.BuscaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/usuario")
@RestController
@RequiredArgsConstructor
public class BuscaUsuariosController {
    private final BuscaUsuarioService buscaUsuarioService;
    private final UsuarioResponseDTOAssembler usuarioResponseDTOAssembler;


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscaUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioResponseDTOAssembler.toDTO(buscaUsuarioService.buscaUsuarioById(id)));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> buscaUsuarios() {
        return ResponseEntity.ok(usuarioResponseDTOAssembler.toCollectionDTO(buscaUsuarioService.getUsuarios()));
    }

}
