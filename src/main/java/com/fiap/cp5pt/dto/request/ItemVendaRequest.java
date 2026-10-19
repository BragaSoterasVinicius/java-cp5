package com.fiap.cp5pt.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemVendaRequest(

        @NotNull(message = "O ID da venda é obrigatório")
        Long vendaId,

        @NotNull(message = "O ID do produto é obrigatório")
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
        Long quantidade,

        @NotNull(message = "O preço unitário é obrigatório")
        @DecimalMin(value = "0.00", message = "O preço unitário não pode ser negativo")
        @Digits(integer = 8, fraction = 2,
                message = "O preço unitário deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal precoUnitario

) {}