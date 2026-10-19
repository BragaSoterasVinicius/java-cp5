package com.fiap.cp5pt.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record VendaRequest(

        Long usuarioId,

        @NotNull(message = "O total é obrigatório")
        @DecimalMin(value = "0.00", message = "O total não pode ser negativo")
        BigDecimal total,

        @Size(max = 20, message = "O status deve ter no máximo 20 caracteres")
        String status

) {}