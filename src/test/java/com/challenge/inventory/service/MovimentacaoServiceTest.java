package com.challenge.inventory.service;

import com.challenge.inventory.dto.MovimentacaoRequest;
import com.challenge.inventory.exception.NegocioException;
import com.challenge.inventory.model.Movimentacao;
import com.challenge.inventory.model.Produto;
import com.challenge.inventory.model.TipoMovimentacao;
import com.challenge.inventory.repository.EstoqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MovimentacaoServiceTest {

    private EstoqueRepository repository;
    private MovimentacaoService service;
    private Produto produto;

    @BeforeEach
    void setUp() {
        repository = mock(EstoqueRepository.class);
        service = new MovimentacaoService(repository);
        produto = new Produto();
        produto.setCodigoProduto(101);
        produto.setDescricaoProduto("Caneta Azul");
        produto.setEstoque(150);
        when(repository.buscar(101)).thenReturn(produto);
    }

    @Test
    void entradaAumentaEstoqueEGeraIdUnico() {
        MovimentacaoRequest request = request(TipoMovimentacao.ENTRADA, 10, "Compra de reposição");
        Movimentacao movimentacao = service.movimentar(request);

        assertEquals(1L, movimentacao.getId());
        assertEquals(160, movimentacao.getEstoqueFinal());
        assertEquals("Compra de reposição", movimentacao.getDescricao());
    }

    @Test
    void saidaDiminuiEstoque() {
        MovimentacaoRequest request = request(TipoMovimentacao.SAIDA, 20, "Venda balcão");
        Movimentacao movimentacao = service.movimentar(request);

        assertEquals(130, movimentacao.getEstoqueFinal());
    }

    @Test
    void saidaMaiorQueEstoqueFalha() {
        MovimentacaoRequest request = request(TipoMovimentacao.SAIDA, 200, "Venda inválida");
        assertThrows(NegocioException.class, () -> service.movimentar(request));
    }

    private MovimentacaoRequest request(TipoMovimentacao tipo, int quantidade, String descricao) {
        MovimentacaoRequest request = new MovimentacaoRequest();
        request.setCodigoProduto(101);
        request.setTipo(tipo);
        request.setQuantidade(quantidade);
        request.setDescricao(descricao);
        return request;
    }
}
