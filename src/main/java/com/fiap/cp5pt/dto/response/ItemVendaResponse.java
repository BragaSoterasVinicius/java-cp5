package com.fiap.cp5pt.dto.response;

import com.fiap.cp5pt.model.ItemVenda;

import java.math.BigDecimal;

public record ItemVendaResponse(
        Long id,
        Long vendaId,
        Long produtoId,
        Long quantidade,
        BigDecimal precoUnitario
) {

    public static ItemVendaResponse fromEntity(ItemVenda item) {
        return new ItemVendaResponse(
                item.getId(),
                item.getVenda().getId(),
                item.getProduto().getId(),
                item.getQuantidade(),
                item.getPrecoUnitario()
        );
    }
}