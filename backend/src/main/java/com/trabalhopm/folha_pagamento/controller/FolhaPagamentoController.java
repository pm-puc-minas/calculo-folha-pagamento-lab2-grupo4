package com.trabalhopm.folha_pagamento.controller;

import com.trabalhopm.folha_pagamento.domain.FolhaPagamento;
import com.trabalhopm.folha_pagamento.service.FolhaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/folhas")
public class FolhaPagamentoController {

    @Autowired
    private FolhaPagamentoService folhaPagamentoService;

    // READ

    @GetMapping
    public ResponseEntity<?> findAll(){
        try {
            List<FolhaPagamento> list = folhaPagamentoService.findAll();
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar folhas: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            FolhaPagamento obj = folhaPagamentoService.findById(id);
            return ResponseEntity.ok().body(obj);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar folha: " + e.getMessage());
        }
    }

    @GetMapping(value = "/periodo/{periodo}")
    public ResponseEntity<?> findByPeriodo(@PathVariable String periodo){
        try {
            YearMonth ym = YearMonth.parse(periodo);
            List<FolhaPagamento> list = folhaPagamentoService.findAllByPeriodo(ym);
            return ResponseEntity.ok().body(list);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de período inválido. Use YYYY-MM.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar folhas: " + e.getMessage());
        }
    }
}
