package com.fiap.cp5pt.controller;


import com.fiap.cp5pt.dto.request.ItemVendaRequest;
import com.fiap.cp5pt.dto.response.ItemVendaResponse;
import com.fiap.cp5pt.service.ItemVendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itens-venda")
@RequiredArgsConstructor
public class ItemVendaController {

    private final ItemVendaService itemVendaService;

    @PostMapping
    public ResponseEntity<ItemVendaResponse> criar(
            @Valid @RequestBody ItemVendaRequest request) {

        ItemVendaResponse response = itemVendaService.criar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ItemVendaResponse>> listarTodos() {
        return ResponseEntity.ok(itemVendaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemVendaResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(itemVendaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemVendaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemVendaRequest request) {

        return ResponseEntity.ok(
                itemVendaService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemVendaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}