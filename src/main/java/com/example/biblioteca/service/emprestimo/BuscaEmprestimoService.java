package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.assembler.EmprestimoAssembler;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.service.usuario.BuscaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscaEmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final BuscaUsuarioService buscaUsuarioService;
    private final EmprestimoAssembler emprestimoAssembler;

    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarTodos() {
        return emprestimoRepository.findAll().stream()
                .map(emprestimoAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmprestimoDTO buscarPorId(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new BibliotecaBusinessException("Empréstimo não encontrado com o ID: " + id));
        
        return emprestimoAssembler.toDTO(emprestimo);
    }

    @Transactional(readOnly = true)
    public List<EmprestimoDTO> buscarPorUsuario(Long usuarioId) {
        Usuario usuario = buscaUsuarioService.getUsuario(usuarioId);

        return emprestimoRepository.findByUsuario(usuario).stream()
                .map(emprestimoAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
