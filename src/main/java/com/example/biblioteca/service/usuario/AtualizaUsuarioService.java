package com.example.biblioteca.service.usuario;

import com.example.biblioteca.assembler.UsuarioDTOAssembler;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.validator.EmailRepetidoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AtualizaUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmailRepetidoValidator emailRepetidoValidator;
    private final UsuarioDTOAssembler usuarioDTOAssembler;
    private final BuscaUsuarioService buscaUsuarioService;

    @Transactional
    public UsuarioDTO atualizarUsuario(Long idUsuario, UsuarioDTO usuarioDTO) {
        validarEmail(usuarioDTO.getEmail(), idUsuario);

        Usuario usuario = buscarUsuario(idUsuario);

        atualizarDadosUsuario(usuario, usuarioDTO);

        Usuario usuarioAtualizado = salvarUsuario(usuario);

        return converterParaDTO(usuarioAtualizado);
    }

    private void validarEmail(String email, Long idUsuario) {
        emailRepetidoValidator.validarEmailAtualizacaoUsuario(email, idUsuario);
    }

    private Usuario buscarUsuario(Long idUsuario) {
        return buscaUsuarioService.getUsuario(idUsuario);
    }

    private Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    private UsuarioDTO converterParaDTO(Usuario usuario) {
        return usuarioDTOAssembler.entityToDTO(usuario);
    }

    private void atualizarDadosUsuario(Usuario usuario, UsuarioDTO usuarioDTO) {
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefone(usuarioDTO.getTelefone());
    }

}