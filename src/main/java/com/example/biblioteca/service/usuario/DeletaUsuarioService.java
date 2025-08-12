package com.example.biblioteca.service.usuario;

import com.example.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeletaUsuarioService {
    private final BuscaUsuarioService buscaUsuarioService;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void deletarUsuario(Long idUsuario) {
        var usuario = buscaUsuarioService.getUsuario(idUsuario);
        usuarioRepository.delete(usuario);
    }

}
