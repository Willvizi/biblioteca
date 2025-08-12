package com.example.biblioteca.service.usuario;

import com.example.biblioteca.assembler.UsuarioDTOAssembler;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscaUsuarioService {
    public static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
    private final UsuarioRepository usuarioRepository;
    private final UsuarioDTOAssembler usuarioDTOAssembler;

    @Transactional(readOnly = true)
    public Usuario getUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new BibliotecaBusinessException(USUARIO_NAO_ENCONTRADO));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscaUsuarioById(Long idUsuario) {
        return usuarioDTOAssembler.entityToDTO(getUsuario(idUsuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> getUsuarios(){
        return usuarioDTOAssembler.entityListToDTO(usuarioRepository.findAll());
    }

}
