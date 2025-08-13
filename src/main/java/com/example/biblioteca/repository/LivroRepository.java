package com.example.biblioteca.repository;

import com.example.biblioteca.domain.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Emprestimo e WHERE e.livro.id = :idLivro AND e.status = 'ATIVO'")
    boolean livroJaEstaEmprestado(Long idLivro);
}
