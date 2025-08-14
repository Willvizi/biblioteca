package com.example.biblioteca.controller.usuario;

import com.example.biblioteca.service.usuario.DeletaUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuários")
@RequestMapping("/usuario")
@RestController
@RequiredArgsConstructor
public class DeletaUsuarioController {
    private final DeletaUsuarioService deletaUsuarioService;

    @Operation(summary = "Deleta um usuário pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaUsuario(@PathVariable Long id) {
        deletaUsuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}