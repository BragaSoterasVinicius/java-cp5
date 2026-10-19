package com.fiap.cp5pt.service;

import com.fiap.cp5pt.dto.request.ItemVendaRequest;
import com.fiap.cp5pt.dto.response.ItemVendaResponse;
import com.fiap.cp5pt.model.ItemVenda;
import com.fiap.cp5pt.model.Produto;
import com.fiap.cp5pt.model.Venda;
import com.fiap.cp5pt.repository.ItemVendaRepository;
import com.fiap.cp5pt.repository.ProdutoRepository;
import com.fiap.cp5pt.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemVendaService {

    private final ItemVendaRepository itemVendaRepository;
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public ItemVendaResponse criar(ItemVendaRequest request) {
        ItemVenda item = new ItemVenda();

        item.setVenda(buscarVenda(request.vendaId()));
        item.setProduto(buscarProduto(request.produtoId()));
        item.setQuantidade(request.quantidade());
        item.setPrecoUnitario(request.precoUnitario());

        ItemVenda salvo = itemVendaRepository.save(item);

        return ItemVendaResponse.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public List<ItemVendaResponse> listarTodos() {
        return itemVendaRepository.findAll()
                .stream()
                .map(ItemVendaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemVendaResponse buscarPorId(Long id) {
        return ItemVendaResponse.fromEntity(buscarItem(id));
    }

    @Transactional
    public ItemVendaResponse atualizar(
            Long id,
            ItemVendaRequest request) {

        ItemVenda item = buscarItem(id);

        item.setVenda(buscarVenda(request.vendaId()));
        item.setProduto(buscarProduto(request.produtoId()));
        item.setQuantidade(request.quantidade());
        item.setPrecoUnitario(request.precoUnitario());

        ItemVenda atualizado = itemVendaRepository.save(item);

        return ItemVendaResponse.fromEntity(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        ItemVenda item = buscarItem(id);
        itemVendaRepository.delete(item);
    }

    private ItemVenda buscarItem(Long id) {
        return itemVendaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Item de venda não encontrado com ID: " + id
                ));
    }

    private Venda buscarVenda(Long vendaId) {
        return vendaRepository.findById(vendaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Venda não encontrada com ID: " + vendaId
                ));
    }

    private Produto buscarProduto(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado com ID: " + produtoId
                ));
    }
}