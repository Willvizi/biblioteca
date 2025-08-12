package com.example.biblioteca.service.usuario;

import com.example.biblioteca.assembler.UsuarioAssembler;
import com.example.biblioteca.assembler.UsuarioDTOAssembler;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.UsuarioDTO;
import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.validator.EmailRepetidoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CriaUsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioDTOAssembler usuarioDTOAssembler;
    private final UsuarioAssembler usuarioAssembler;
    private final EmailRepetidoValidator emailRepetidoValidator;

    @Transactional
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        validarEmailDuplicado(usuarioDTO.getEmail());
        var novoUsuario = criarNovoUsuario(usuarioDTO);
        var usuarioSalvo = usuarioRepository.save(novoUsuario);
        return usuarioDTOAssembler.entityToDTO(usuarioSalvo);
    }

    private void validarEmailDuplicado(String email) {
        emailRepetidoValidator.validar(email);
    }

    private Usuario criarNovoUsuario(UsuarioDTO usuarioDTO) {
        var usuario = usuarioAssembler.toEntity(usuarioDTO);
        usuario.setDataCadastro(LocalDate.now());
        return usuario;
    }

}