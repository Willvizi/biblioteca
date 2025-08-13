package com.example.biblioteca.service.livro;

import com.example.biblioteca.repository.LivroRepository;
import com.example.biblioteca.service.emprestimo.DeletaEmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletaLivroService {

    private final LivroRepository livroRepository;
    private final BuscaLivroService buscaLivroService;
    private final DeletaEmprestimoService deletaEmprestimoService;


    @Transactional
    public void deletarLivro(Long id) {
        var livro = buscaLivroService.buscarLivroPorId(id);
        deletaEmprestimoService.deletarPorLivro(livro);

        livroRepository.delete(livro);
    }

}
