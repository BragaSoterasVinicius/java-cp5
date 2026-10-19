package com.fiap.cp5pt.controller;

import com.fiap.cp5pt.dto.request.VendaRequest;
import com.fiap.cp5pt.dto.response.VendaResponse;
import com.fiap.cp5pt.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    public ResponseEntity<VendaResponse> criar(
            @Valid @RequestBody VendaRequest request) {

        VendaResponse response = vendaService.criar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<VendaResponse>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VendaRequest request) {

        return ResponseEntity.ok(vendaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vendaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}