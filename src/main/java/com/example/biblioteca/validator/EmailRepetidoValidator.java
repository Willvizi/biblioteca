package com.example.biblioteca.validator;

import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailRepetidoValidator {


    public static final String EXISTE_USUARIO_COM_ESSE_EMAIL = "Já existe um usuário cadastrado com este e-mail.";
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public void validar(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new BibliotecaBusinessException(EXISTE_USUARIO_COM_ESSE_EMAIL);
        }
    }

    @Transactional(readOnly = true)
    public void validarEmailAtualizacaoUsuario(String email, Long idUsuario) {
        if (usuarioRepository.existsByEmailAndIdUsuario(email, idUsuario)) {
            throw new BibliotecaBusinessException(EXISTE_USUARIO_COM_ESSE_EMAIL);
        }
    }
}
