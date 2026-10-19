package com.fiap.cp5pt.service;

import com.fiap.cp5pt.dto.request.ProdutoRequest;
import com.fiap.cp5pt.dto.response.ProdutoResponse;
import com.fiap.cp5pt.model.Categoria;
import com.fiap.cp5pt.model.Produto;
import com.fiap.cp5pt.repository.CategoriaRepository;
import com.fiap.cp5pt.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        Produto produto = new Produto();

        produto.setNome(request.nome().trim());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setEstoque(
                request.estoque() != null ? request.estoque() : 0L
        );
        produto.setAtivo(
                request.ativo() != 0 ? request.ativo() : 'S'
        );
        produto.setCategoria(buscarCategoria(request.categoriaId()));

        Produto salvo = produtoRepository.save(produto);

        return ProdutoResponse.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        return ProdutoResponse.fromEntity(buscarProduto(id));
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarProduto(id);

        produto.setNome(request.nome().trim());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());

        if (request.estoque() != null) {
            produto.setEstoque(request.estoque());
        }

        if (request.ativo() != 0) {
            produto.setAtivo(request.ativo());
        }

        produto.setCategoria(buscarCategoria(request.categoriaId()));

        Produto atualizado = produtoRepository.save(produto);

        return ProdutoResponse.fromEntity(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = buscarProduto(id);
        produtoRepository.delete(produto);
    }

    private Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado com ID: " + id
                ));
    }

    private Categoria buscarCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }

        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada com ID: " + categoriaId
                ));
    }
}