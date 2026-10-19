package com.fiap.cp5pt.dto.response;


import com.fiap.cp5pt.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Long estoque,
        Long categoriaId,
        char ativo,
        LocalDateTime criadoEm
) {

    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque(),
                produto.getCategoria() != null
                        ? produto.getCategoria().getId()
                        : null,
                produto.getAtivo(),
                produto.getCriadoEm()
        );
    }
}