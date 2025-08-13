package com.example.biblioteca.service.emprestimo;

import com.example.biblioteca.assembler.EmprestimoAssembler;
import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Emprestimo.StatusEmprestimo;
import com.example.biblioteca.dto.EmprestimoDTO;
import com.example.biblioteca.exception.BibliotecaBusinessException;
import com.example.biblioteca.input.AtualizaEmprestimoInput;
import com.example.biblioteca.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AtualizaEmprestimoService {

    public static final String EMPRESTIMO_NAO_ENCONTRADO = "Empréstimo não encontrado";
    private final EmprestimoRepository emprestimoRepository;
    private final EmprestimoAssembler emprestimoAssembler;

    @Transactional
    public EmprestimoDTO devolverLivro(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new BibliotecaBusinessException(EMPRESTIMO_NAO_ENCONTRADO));

        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucao(LocalDate.now());

        emprestimoRepository.save(emprestimo);

        return emprestimoAssembler.toDTO(emprestimo);
    }

    @Transactional
    public EmprestimoDTO atualizarDataDevolucao(Long emprestimoId, AtualizaEmprestimoInput input) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new BibliotecaBusinessException(EMPRESTIMO_NAO_ENCONTRADO));

        emprestimo.setDataDevolucao(input.getDataDevolucao());
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        emprestimoRepository.save(emprestimo);

        return emprestimoAssembler.toDTO(emprestimo);
    }
}