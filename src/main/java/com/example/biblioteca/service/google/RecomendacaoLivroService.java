package com.example.biblioteca.service.google;

import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.LivroRecomendacaoDTO;
import com.example.biblioteca.dto.google.GoogleBookIndustryIdentifier;
import com.example.biblioteca.dto.google.GoogleBookItem;
import com.example.biblioteca.dto.google.GoogleBookVolumeInfo;
import com.example.biblioteca.dto.google.GoogleBooksResponse;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.service.usuario.BuscaUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecomendacaoLivroService {
    private final EmprestimoRepository emprestimoRepository;
    private final RestTemplate restTemplate;
    private final BuscaUsuarioService buscaUsuarioService;

    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=subject:";
    private static final int MAX_RESULTS = 5;
    private static final String AUTOR_DESCONHECIDO = "Autor Desconhecido";
    private static final String CATEGORIA_GERAL = "Geral";
    private static final String ISBN_NAO_DISPONIVEL = "ISBN não disponível";

    public List<LivroRecomendacaoDTO> buscarRecomendacoesPorCategoriaMaisEmprestada(Long idUsuario) {
        Usuario usuario = buscaUsuarioService.getUsuario(idUsuario);

        return buscarCategoriaMaisEmprestada(usuario)
                .map(this::buscarRecomendacoesGoogleBooks)
                .orElse(Collections.emptyList());
    }

    private Optional<String> buscarCategoriaMaisEmprestada(Usuario usuario) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuario(usuario);

        if (emprestimos.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Long> contadorCategorias = emprestimos.stream()
                .map(emprestimo -> emprestimo.getLivro().getCategoria())
                .collect(Collectors.groupingBy(categoria -> categoria, Collectors.counting()));

        return contadorCategorias.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    private List<LivroRecomendacaoDTO> buscarRecomendacoesGoogleBooks(String categoria) {
        String url = construirUrlBusca(categoria);

        try {
            ResponseEntity<GoogleBooksResponse> response = restTemplate.getForEntity(url, GoogleBooksResponse.class);
            return extrairLivrosDeResposta(response);
        } catch (RestClientException e) {
            log.error("Erro ao consultar API do Google Books: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String construirUrlBusca(String categoria) {
        return GOOGLE_BOOKS_API_URL + categoria + "&maxResults=" + MAX_RESULTS;
    }

    private List<LivroRecomendacaoDTO> extrairLivrosDeResposta(ResponseEntity<GoogleBooksResponse> response) {
        if (response.getBody() == null || response.getBody().getItems() == null) {
            return Collections.emptyList();
        }

        return response.getBody().getItems().stream()
                .map(this::converterParaLivroRecomendacaoDTO)
                .collect(Collectors.toList());
    }

    private LivroRecomendacaoDTO converterParaLivroRecomendacaoDTO(GoogleBookItem item) {
        GoogleBookVolumeInfo volumeInfo = item.getVolumeInfo();

        return LivroRecomendacaoDTO.builder()
                .titulo(volumeInfo.getTitle())
                .autor(formatarAutores(volumeInfo.getAuthors()))
                .isbn(obterIsbn(volumeInfo.getIndustryIdentifiers()))
                .dataPublicacao(LocalDate.now())
                .categoria(formatarCategorias(volumeInfo))
                .build();
    }

    private String formatarAutores(List<String> autores) {
        return String.join(", ", Optional.ofNullable(autores)
                .orElse(Collections.singletonList(AUTOR_DESCONHECIDO)));
    }

    private String formatarCategorias(GoogleBookVolumeInfo volumeInfo) {
        if (volumeInfo.getCategories() != null) {
            return String.join(", ", volumeInfo.getCategories());
        }

        return Optional.ofNullable(volumeInfo.getMainCategory())
                .orElse(CATEGORIA_GERAL);
    }

    private String obterIsbn(List<GoogleBookIndustryIdentifier> identifiers) {
        if (identifiers == null || identifiers.isEmpty()) {
            return ISBN_NAO_DISPONIVEL;
        }

        return identifiers.stream()
                .filter(id -> "ISBN_13".equals(id.getType()) || "ISBN_10".equals(id.getType()))
                .map(GoogleBookIndustryIdentifier::getIdentifier)
                .findFirst()
                .orElse(ISBN_NAO_DISPONIVEL);
    }
}