package com.fiap.cp5pt.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 120)
    private String nome;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "SENHA_HASH", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "PAPEL", nullable = false, length = 20)
    private String papel = "CLIENTE";

    @Column(name = "ATIVO", nullable = false, length = 1)
    private char ativo = 'S';

    @Column(name = "CRIADO_EM", nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
        if (papel == null) {
            papel = "CLIENTE";
        }
        if (ativo == 0) {
            ativo = 'S';
        }
    }
}