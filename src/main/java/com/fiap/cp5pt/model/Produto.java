package com.fiap.cp5pt.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUTOS")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(name = "NOME", nullable = false, length = 150)
    private String nome;

    @Lob
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @NotNull
    @Min(0)
    @Column(name = "ESTOQUE", nullable = false, precision = 10)
    private Long estoque = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORIA_ID")
    private Categoria categoria;

    @Column(name = "ATIVO", nullable = false, length = 1)
    private String ativo = "S";

    @Column(name = "CRIADO_EM", insertable = false, updatable = false)
    private LocalDateTime criadoEm;
}