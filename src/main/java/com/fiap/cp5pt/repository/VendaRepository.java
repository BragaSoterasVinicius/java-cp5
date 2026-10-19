package com.fiap.cp5pt.repository;

import com.fiap.cp5pt.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByUsuarioId(Long usuarioId);

    List<Venda> findByStatus(String status);
}
