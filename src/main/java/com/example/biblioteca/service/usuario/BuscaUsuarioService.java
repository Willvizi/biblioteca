package com.example.biblioteca.service.usuario;

import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuscaUsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Usuario getUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new BibliotecaBusinessException("Usuário não encontrado"));
    }
}
