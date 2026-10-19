package com.fiap.cp5pt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ITENS_VENDA")
@Getter
@Setter
@NoArgsConstructor
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "VENDA_ID", nullable = false)
    private Venda venda;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUTO_ID", nullable = false)
    private Produto produto;

    @NotNull
    @Min(1)
    @Column(name = "QUANTIDADE", nullable = false, precision = 10)
    private Long quantidade;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    @Column(
            name = "PRECO_UNITARIO",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal precoUnitario;
}
