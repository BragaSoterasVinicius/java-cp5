package com.fiap.cp5pt.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,

        String descricao,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.00", message = "O preço não pode ser negativo")
        @Digits(integer = 8, fraction = 2,
                message = "O preço deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal preco,

        @Min(value = 0, message = "O estoque não pode ser negativo")
        Long estoque,

        Long categoriaId,

        @Pattern(regexp = "[SN]", message = "O campo ativo deve ser S ou N")
        char ativo

) {}