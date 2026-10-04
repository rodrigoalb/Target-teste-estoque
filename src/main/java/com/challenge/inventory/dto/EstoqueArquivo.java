package com.challenge.inventory.dto;

import com.challenge.inventory.model.Produto;

import java.util.List;

public class EstoqueArquivo {

    private List<Produto> estoque;

    public List<Produto> getEstoque() {
        return estoque;
    }

    public void setEstoque(List<Produto> estoque) {
        this.estoque = estoque;
    }
}
