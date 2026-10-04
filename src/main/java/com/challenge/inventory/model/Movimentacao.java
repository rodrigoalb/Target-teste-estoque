package com.challenge.inventory.model;

import java.time.LocalDateTime;

public class Movimentacao {

    private Long id;
    private Integer codigoProduto;
    private TipoMovimentacao tipo;
    private String descricao;
    private int quantidade;
    private int estoqueFinal;
    private LocalDateTime dataHora;

    public Movimentacao(Long id, Integer codigoProduto, TipoMovimentacao tipo, String descricao,
                        int quantidade, int estoqueFinal, LocalDateTime dataHora) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.tipo = tipo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.estoqueFinal = estoqueFinal;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public Integer getCodigoProduto() {
        return codigoProduto;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getEstoqueFinal() {
        return estoqueFinal;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
