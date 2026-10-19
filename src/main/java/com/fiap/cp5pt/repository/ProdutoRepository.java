package com.fiap.cp5pt.repository;

import com.fiap.cp5pt.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository
        extends JpaRepository<Produto, Long> {

    List<Produto> findByAtivo(String ativo);

    List<Produto> findByCategoriaId(Long categoriaId);
}
