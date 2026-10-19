package com.fiap.cp5pt.dto.response;

import com.fiap.cp5pt.model.Categoria;

public record CategoriaResponse(
        Long id,
        String nome
) {

    public static CategoriaResponse fromEntity(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome()
        );
    }
}