package com.example.biblioteca.controller.usuario;

import com.example.biblioteca.service.usuario.DeletaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/usuario")
@RestController
@RequiredArgsConstructor
public class DeletaUsuarioController {
    private final DeletaUsuarioService deletaUsuarioService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaUsuario(@PathVariable Long id) {
        deletaUsuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
