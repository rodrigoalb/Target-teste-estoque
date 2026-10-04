package com.challenge.inventory.service;

import com.challenge.inventory.dto.MovimentacaoRequest;
import com.challenge.inventory.exception.NegocioException;
import com.challenge.inventory.model.Movimentacao;
import com.challenge.inventory.model.Produto;
import com.challenge.inventory.model.TipoMovimentacao;
import com.challenge.inventory.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MovimentacaoService {

    private final EstoqueRepository estoqueRepository;
    private final AtomicLong sequencia = new AtomicLong(1);
    private final List<Movimentacao> historico = new ArrayList<>();

    public MovimentacaoService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public synchronized Movimentacao movimentar(MovimentacaoRequest request) {
        Produto produto = estoqueRepository.buscar(request.getCodigoProduto());
        int estoqueAtual = produto.getEstoque();
        int estoqueFinal;

        if (request.getTipo() == TipoMovimentacao.ENTRADA) {
            estoqueFinal = estoqueAtual + request.getQuantidade();
        } else if (request.getTipo() == TipoMovimentacao.SAIDA) {
            if (request.getQuantidade() > estoqueAtual) {
                throw new NegocioException("Estoque insuficiente. Disponível: " + estoqueAtual);
            }
            estoqueFinal = estoqueAtual - request.getQuantidade();
        } else {
            throw new NegocioException("Tipo de movimentação inválido");
        }

        produto.setEstoque(estoqueFinal);

        Movimentacao movimentacao = new Movimentacao(
                sequencia.getAndIncrement(),
                produto.getCodigoProduto(),
                request.getTipo(),
                request.getDescricao(),
                request.getQuantidade(),
                estoqueFinal,
                LocalDateTime.now()
        );
        historico.add(movimentacao);
        return movimentacao;
    }

    public List<Produto> listarEstoque() {
        return estoqueRepository.listar();
    }

    public List<Movimentacao> listarMovimentacoes() {
        return List.copyOf(historico);
    }
}
