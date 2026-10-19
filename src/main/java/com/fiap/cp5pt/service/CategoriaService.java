package com.fiap.cp5pt.service;

import com.fiap.cp5pt.dto.request.CategoriaRequest;
import com.fiap.cp5pt.dto.response.CategoriaResponse;
import com.fiap.cp5pt.model.Categoria;
import com.fiap.cp5pt.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        if (categoriaRepository.existsByNomeIgnoreCase(request.nome().trim())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma categoria com esse nome"
            );
        }

        Categoria categoria = new Categoria();
        categoria.setNome(request.nome().trim());

        Categoria salva = categoriaRepository.save(categoria);

        return CategoriaResponse.fromEntity(salva);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        Categoria categoria = buscarEntidadePorId(id);

        return CategoriaResponse.fromEntity(categoria);
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarEntidadePorId(id);

        String novoNome = request.nome().trim();

        boolean nomeAlterado = !categoria.getNome().equalsIgnoreCase(novoNome);

        if (nomeAlterado && categoriaRepository.existsByNomeIgnoreCase(novoNome)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma categoria com esse nome"
            );
        }

        categoria.setNome(novoNome);

        Categoria atualizada = categoriaRepository.save(categoria);

        return CategoriaResponse.fromEntity(atualizada);
    }

    @Transactional
    public void deletar(Long id) {
        Categoria categoria = buscarEntidadePorId(id);

        categoriaRepository.delete(categoria);
    }

    private Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada com ID: " + id
                ));
    }
}