package com.trabalhopm.folha_pagamento.controller;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.FolhaPagamento;
import com.trabalhopm.folha_pagamento.domain.Funcionario;

import com.trabalhopm.folha_pagamento.dto.FuncionarioDTO;

import com.trabalhopm.folha_pagamento.service.FolhaPagamentoService;
import com.trabalhopm.folha_pagamento.service.FuncionarioService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping(value = "/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FolhaPagamentoService folhaPagamentoService;

    //CREATE

    @PostMapping
    public ResponseEntity<?> createFuncionario(@Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        try {
            Funcionario novoFuncionario = funcionarioService.create(funcionarioDTO);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(novoFuncionario.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(novoFuncionario);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao criar funcionário: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/folhas/{periodo}") // Ex: POST /funcionarios/1/folhas/2025-10
    public ResponseEntity<?> gerarFolhaPagamento(@PathVariable Long id, @PathVariable @DateTimeFormat(pattern="yyyy-MM") YearMonth periodo) {
        try {
            Funcionario funcionario = funcionarioService.findById(id);

            FolhaPagamento folha = folhaPagamentoService.getFolha(funcionario, periodo);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/funcionarios/{id}/folhas/{periodo}")
                    .buildAndExpand(id, periodo.toString())
                    .toUri();

            return ResponseEntity.ok().body(folha);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Funcionário não encontrado com ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao gerar folha de pagamento: " + e.getMessage());
        }
    }

    // READ

    @GetMapping
    public ResponseEntity<List<Funcionario>> findAll(){
        try {
            List<Funcionario> list = funcionarioService.findAll();
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Funcionario> findById(@PathVariable Long id){
        try {
            Funcionario obj = funcionarioService.findById(id);
            return ResponseEntity.ok().body(obj);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}/financeiro")
    public ResponseEntity<Financeiro> getFinanceiro(@PathVariable Long id){
        try {
            Funcionario obj = funcionarioService.findById(id);
            Financeiro financeiro = obj.getFinanceiro();

            if (financeiro == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().body(financeiro);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}/folhas")
    public ResponseEntity<List<FolhaPagamento>> getFolhas(@PathVariable Long id) {
        try {
            Funcionario funcionario = funcionarioService.findById(id);
            List<FolhaPagamento> folhas = folhaPagamentoService.findAllByFuncionario(funcionario);

            return ResponseEntity.ok().body(folhas);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}/folhas/{periodo}") // Ex: /funcionarios/1/folhas/2025-10
    public ResponseEntity<FolhaPagamento> getFolhaPagamentoPorPeriodo(@PathVariable Long id, @PathVariable @DateTimeFormat(pattern="yyyy-MM") YearMonth periodo) {
        try {
            Funcionario funcionario = funcionarioService.findById(id);

            FolhaPagamento folha = folhaPagamentoService.findByFuncionarioAndPeriodo(funcionario, periodo);
            return ResponseEntity.ok().body(folha);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE

    @DeleteMapping(value = "/{id}/folhas/{periodo}")
    public ResponseEntity<?> deleteFolhaPagamento(@PathVariable Long id, @PathVariable @DateTimeFormat(pattern="yyyy-MM") YearMonth periodo) {
        try {
            Funcionario funcionario = funcionarioService.findById(id);
            folhaPagamentoService.deleteByFuncionarioAndPeriodo(funcionario, periodo);

            return ResponseEntity.noContent().build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao deletar a folha de pagamento: " + e.getMessage());
        }
    }
}
