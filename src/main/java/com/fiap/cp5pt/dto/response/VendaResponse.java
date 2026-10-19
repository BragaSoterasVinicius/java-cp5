package com.fiap.cp5pt.dto.response;

import com.fiap.cp5pt.model.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaResponse(
        Long id,
        Long usuarioId,
        BigDecimal total,
        String status,
        LocalDateTime criadoEm
) {

    public static VendaResponse fromEntity(Venda venda) {
        return new VendaResponse(
                venda.getId(),
                venda.getUsuario() != null
                        ? venda.getUsuario().getId()
                        : null,
                venda.getTotal(),
                venda.getStatus(),
                venda.getCriadoEm()
        );
    }
}