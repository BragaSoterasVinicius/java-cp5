package com.fiap.cp5pt.repository;

import com.fiap.cp5pt.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemVendaRepository
        extends JpaRepository<ItemVenda, Long> {

    List<ItemVenda> findByVendaId(Long vendaId);

    List<ItemVenda> findByProdutoId(Long produtoId);

    boolean existsByVendaId(Long vendaId);
}