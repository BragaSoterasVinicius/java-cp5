package com.fiap.cp5pt.repository;


import com.fiap.cp5pt.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {

    boolean existsByNomeIgnoreCase(String nome);
}