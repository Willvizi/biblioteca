package com.example.biblioteca.service.google;

import com.example.biblioteca.domain.Emprestimo;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.domain.Usuario;
import com.example.biblioteca.dto.LivroRecomendacaoDTO;
import com.example.biblioteca.dto.google.GoogleBookIndustryIdentifier;
import com.example.biblioteca.dto.google.GoogleBookItem;
import com.example.biblioteca.dto.google.GoogleBookVolumeInfo;
import com.example.biblioteca.dto.google.GoogleBooksResponse;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.service.usuario.BuscaUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecomendacaoLivroServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BuscaUsuarioService buscaUsuarioService;

    @InjectMocks
    private RecomendacaoLivroService recomendacaoLivroService;

    private Usuario usuario;
    private List<Emprestimo> emprestimos;
    private GoogleBooksResponse googleBooksResponse;
    private ResponseEntity<GoogleBooksResponse> responseEntity;

    @BeforeEach
    void setUp() {
        // Configuração do usuário
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        // Configuração de livros e empréstimos
        Livro livro1 = new Livro();
        livro1.setCategoria("Ficção Científica");

        Livro livro2 = new Livro();
        livro2.setCategoria("Ficção Científica");

        Livro livro3 = new Livro();
        livro3.setCategoria("Romance");

        Emprestimo emprestimo1 = new Emprestimo();
        emprestimo1.setLivro(livro1);
        emprestimo1.setUsuario(usuario);

        Emprestimo emprestimo2 = new Emprestimo();
        emprestimo2.setLivro(livro2);
        emprestimo2.setUsuario(usuario);

        Emprestimo emprestimo3 = new Emprestimo();
        emprestimo3.setLivro(livro3);
        emprestimo3.setUsuario(usuario);

        emprestimos = Arrays.asList(emprestimo1, emprestimo2, emprestimo3);

        // Configuração da resposta da API do Google Books
        GoogleBookVolumeInfo volumeInfo = new GoogleBookVolumeInfo();
        volumeInfo.setTitle("Livro de Teste");
        volumeInfo.setAuthors(Arrays.asList("Autor 1", "Autor 2"));
        volumeInfo.setCategories(Arrays.asList("Ficção Científica", "Aventura"));
        
        GoogleBookIndustryIdentifier identifier = new GoogleBookIndustryIdentifier();
        identifier.setType("ISBN_13");
        identifier.setIdentifier("9788501112331");
        volumeInfo.setIndustryIdentifiers(Collections.singletonList(identifier));

        GoogleBookItem bookItem = new GoogleBookItem();
        bookItem.setVolumeInfo(volumeInfo);

        googleBooksResponse = new GoogleBooksResponse();
        googleBooksResponse.setItems(Collections.singletonList(bookItem));
        
        responseEntity = ResponseEntity.ok(googleBooksResponse);
    }

    @Test
    void buscarRecomendacoesPorCategoriaMaisEmprestada_QuandoUsuarioTemEmprestimos_RetornaRecomendacoes() {
        // Arrange
        when(buscaUsuarioService.getUsuario(1L)).thenReturn(usuario);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(emprestimos);
        when(restTemplate.getForEntity(anyString(), eq(GoogleBooksResponse.class))).thenReturn(responseEntity);

        // Act
        List<LivroRecomendacaoDTO> resultado = recomendacaoLivroService.buscarRecomendacoesPorCategoriaMaisEmprestada(1L);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Livro de Teste", resultado.get(0).getTitulo());
        assertEquals("Autor 1, Autor 2", resultado.get(0).getAutor());
        assertEquals("9788501112331", resultado.get(0).getIsbn());
        assertEquals("Ficção Científica, Aventura", resultado.get(0).getCategoria());
        
        verify(buscaUsuarioService).getUsuario(1L);
        verify(emprestimoRepository).findByUsuario(usuario);
        verify(restTemplate).getForEntity(contains("subject:Ficção Científica"), eq(GoogleBooksResponse.class));
    }

    @Test
    void buscarRecomendacoesPorCategoriaMaisEmprestada_QuandoUsuarioNaoTemEmprestimos_RetornaListaVazia() {
        // Arrange
        when(buscaUsuarioService.getUsuario(1L)).thenReturn(usuario);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(Collections.emptyList());

        // Act
        List<LivroRecomendacaoDTO> resultado = recomendacaoLivroService.buscarRecomendacoesPorCategoriaMaisEmprestada(1L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(buscaUsuarioService).getUsuario(1L);
        verify(emprestimoRepository).findByUsuario(usuario);
        verify(restTemplate, never()).getForEntity(anyString(), eq(GoogleBooksResponse.class));
    }

    @Test
    void buscarRecomendacoesPorCategoriaMaisEmprestada_QuandoAPIRetornaErro_RetornaListaVazia() {
        // Arrange
        when(buscaUsuarioService.getUsuario(1L)).thenReturn(usuario);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(emprestimos);
        when(restTemplate.getForEntity(anyString(), eq(GoogleBooksResponse.class)))
                .thenThrow(new RestClientException("Erro de conexão"));

        // Act
        List<LivroRecomendacaoDTO> resultado = recomendacaoLivroService.buscarRecomendacoesPorCategoriaMaisEmprestada(1L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(buscaUsuarioService).getUsuario(1L);
        verify(emprestimoRepository).findByUsuario(usuario);
        verify(restTemplate).getForEntity(contains("subject:Ficção Científica"), eq(GoogleBooksResponse.class));
    }

    @Test
    void buscarRecomendacoesPorCategoriaMaisEmprestada_QuandoAPIRetornaRespostaVazia_RetornaListaVazia() {
        // Arrange
        when(buscaUsuarioService.getUsuario(1L)).thenReturn(usuario);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(emprestimos);
        
        GoogleBooksResponse respostaVazia = new GoogleBooksResponse();
        respostaVazia.setItems(null);
        ResponseEntity<GoogleBooksResponse> respostaVaziaEntity = ResponseEntity.ok(respostaVazia);
        
        when(restTemplate.getForEntity(anyString(), eq(GoogleBooksResponse.class))).thenReturn(respostaVaziaEntity);

        // Act
        List<LivroRecomendacaoDTO> resultado = recomendacaoLivroService.buscarRecomendacoesPorCategoriaMaisEmprestada(1L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(buscaUsuarioService).getUsuario(1L);
        verify(emprestimoRepository).findByUsuario(usuario);
        verify(restTemplate).getForEntity(contains("subject:Ficção Científica"), eq(GoogleBooksResponse.class));
    }

}