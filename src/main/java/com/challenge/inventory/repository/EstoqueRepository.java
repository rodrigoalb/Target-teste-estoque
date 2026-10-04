package com.challenge.inventory.repository;

import com.challenge.inventory.dto.EstoqueArquivo;
import com.challenge.inventory.exception.NegocioException;
import com.challenge.inventory.model.Produto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EstoqueRepository {

    private final ObjectMapper objectMapper;
    private final Map<Integer, Produto> produtos = new ConcurrentHashMap<>();

    public EstoqueRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void carregar() throws IOException {
        EstoqueArquivo arquivo = objectMapper.readValue(
                new ClassPathResource("estoque.json").getInputStream(),
                EstoqueArquivo.class
        );
        for (Produto produto : arquivo.getEstoque()) {
            produtos.put(produto.getCodigoProduto(), produto);
        }
    }

    public List<Produto> listar() {
        return new ArrayList<>(produtos.values());
    }

    public Produto buscar(Integer codigoProduto) {
        Produto produto = produtos.get(codigoProduto);
        if (produto == null) {
            throw new NegocioException("Produto não encontrado: " + codigoProduto);
        }
        return produto;
    }
}
