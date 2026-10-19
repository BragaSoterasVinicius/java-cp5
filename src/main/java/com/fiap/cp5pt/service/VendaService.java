package com.fiap.cp5pt.service;

import com.fiap.cp5pt.dto.request.VendaRequest;
import com.fiap.cp5pt.dto.response.VendaResponse;
import com.fiap.cp5pt.model.Usuario;
import com.fiap.cp5pt.model.Venda;
import com.fiap.cp5pt.repository.UsuarioRepository;
import com.fiap.cp5pt.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public VendaResponse criar(VendaRequest request) {
        Venda venda = new Venda();

        venda.setUsuario(buscarUsuario(request.usuarioId()));
        venda.setTotal(request.total());
        venda.setStatus(normalizarStatus(request.status(), "PENDENTE"));

        Venda salva = vendaRepository.save(venda);

        return VendaResponse.fromEntity(salva);
    }

    @Transactional(readOnly = true)
    public List<VendaResponse> listarTodas() {
        return vendaRepository.findAll()
                .stream()
                .map(VendaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public VendaResponse buscarPorId(Long id) {
        return VendaResponse.fromEntity(buscarVenda(id));
    }

    @Transactional
    public VendaResponse atualizar(Long id, VendaRequest request) {
        Venda venda = buscarVenda(id);

        // Atualiza o usuário somente se um usuarioId for enviado.
        if (request.usuarioId() != null) {
            venda.setUsuario(buscarUsuario(request.usuarioId()));
        }

        if (request.total() != null) {
            venda.setTotal(request.total());
        }

        // Preserva o status atual se nenhum novo status for enviado.
        if (request.status() != null) {
            venda.setStatus(normalizarStatus(request.status(), null));
        }

        Venda atualizada = vendaRepository.save(venda);

        return VendaResponse.fromEntity(atualizada);
    }

    @Transactional
    public void deletar(Long id) {
        Venda venda = buscarVenda(id);
        vendaRepository.delete(venda);
    }

    private Venda buscarVenda(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Venda não encontrada com ID: " + id
                ));
    }

    private Usuario buscarUsuario(Long usuarioId) {
        if (usuarioId == null) {
            return null;
        }

        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado com ID: " + usuarioId
                ));
    }

    private String normalizarStatus(String status, String valorPadrao) {
        if (status == null) {
            return valorPadrao;
        }

        String normalizado = status.trim();

        if (normalizado.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O status não pode ser vazio"
            );
        }

        return normalizado;
    }
}