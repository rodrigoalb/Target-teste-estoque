package com.challenge.inventory.controller;

import com.challenge.inventory.dto.MovimentacaoRequest;
import com.challenge.inventory.model.Movimentacao;
import com.challenge.inventory.model.Produto;
import com.challenge.inventory.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping("/estoque")
    public List<Produto> listarEstoque() {
        return movimentacaoService.listarEstoque();
    }

    @GetMapping("/movimentacoes")
    public List<Movimentacao> listarMovimentacoes() {
        return movimentacaoService.listarMovimentacoes();
    }

    @PostMapping("/movimentacoes")
    public ResponseEntity<Movimentacao> lancar(@Valid @RequestBody MovimentacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoService.movimentar(request));
    }
}
