package com.example.biblioteca.service.usuario;

import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.service.emprestimo.DeletaEmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeletaUsuarioService {
    private final BuscaUsuarioService buscaUsuarioService;
    private final UsuarioRepository usuarioRepository;
    private final DeletaEmprestimoService deletaEmprestimoService;

    @Transactional
    public void deletarUsuario(Long idUsuario) {
        var usuario = buscaUsuarioService.getUsuario(idUsuario);
        deletaEmprestimoService.deletarPorUsuario(usuario);
        usuarioRepository.delete(usuario);
    }

}
